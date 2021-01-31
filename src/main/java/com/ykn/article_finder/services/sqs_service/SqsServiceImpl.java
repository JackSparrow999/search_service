package com.ykn.article_finder.services.sqs_service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ykn.article_finder.dtos.email.Article;
import com.ykn.article_finder.dtos.email.EmailRequest;
import com.ykn.article_finder.dtos.email.Feedback;
import com.ykn.article_finder.dtos.googlesearch.GoogleSearchRequest;
import com.ykn.article_finder.dtos.googlesearch.SiteResponse;
import com.ykn.article_finder.dtos.search.EmailContext;
import com.ykn.article_finder.dtos.search.SearchRequest;
import com.ykn.article_finder.dtos.search.SearchResponse;
import com.ykn.article_finder.exceptions.AppException;
import com.ykn.article_finder.exceptions.ParseException;
import com.ykn.article_finder.services.google_search_service.GoogleSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.Instant;

@Slf4j
@Service
public class SqsServiceImpl implements SqsService{

    @Autowired
    private AmazonSQS sqs;

    @Value("${cloud.aws.end-point.search.uri}")
    private String searchQueueUrl;

    @Value("${cloud.aws.end-point.mail.uri}")
    private String mailQueueUrl;

    @Value("${cloud.aws.end-point.feedback.uri}")
    private String feedbackQueueUrl;

    @Value("${app.linksUpperLimit}")
    private Integer linksUpperlimit;

    @Value("${app.emailDelay}")
    private Long mailDelayInSeconds;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GoogleSearchService googleSearchService;

    private static final String yknGroupId = "ykn";

    @Override
    public Mono<SearchResponse> sendSearchRequestToSqs(SearchRequest searchRequest) {

        if(searchRequest.getEmailContext() == null)
            searchRequest.setEmailContext(new EmailContext());

        if(searchRequest.getEmailContext().getSendAfterDate() == null)
            searchRequest.getEmailContext().setSendAfterDate(Instant.now().toEpochMilli());

        try {
            String payload = objectMapper.writeValueAsString(searchRequest);
            return sqsProducer(payload, searchQueueUrl)
                    .map(b -> {
                        if(Boolean.TRUE.equals(b))
                            return new SearchResponse(true, "Request accepted!");
                        else
                            return new SearchResponse(false, "Request failed!");
                    });
        } catch (JsonProcessingException e) {
            throw new ParseException("Error parsing object!");
        }
    }

    public Mono<Boolean> sendEmailRequestToSqs(EmailRequest emailRequest){
        String payload = null;
        try {
            payload = objectMapper.writeValueAsString(emailRequest);
        } catch (JsonProcessingException e) {
            throw new ParseException("Error parsing object!");
        }
        return sqsProducer(payload, mailQueueUrl);
    }

    public Mono<Boolean> sqsProducer(String payload, String queueUrl){

        log.debug("Producer: {}", payload);

        try {
            SendMessageRequest sendMessageRequest
                    = new SendMessageRequest()
                    .withMessageGroupId(yknGroupId)
                    .withQueueUrl(queueUrl)
                    .withMessageBody(payload);
            sqs.sendMessage(sendMessageRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return Mono.just(false);
        }
        return Mono.just(true);
    }

    @PostConstruct
    public void consumeSearchEvent() {
        ReceiveMessageRequest request = new ReceiveMessageRequest(searchQueueUrl);

        Flux.interval(Duration.ofSeconds(5))
                .onBackpressureDrop()
                .flatMap(
                        w ->
                            Mono.just(sqs.receiveMessage(request))
                                    .flatMapMany(receiveMessageResult -> Flux.fromIterable(receiveMessageResult.getMessages())))
                .flatMap(message -> {
                    String body = message.getBody();
                    SearchRequest searchRequest = null;
                    try {
                        searchRequest = objectMapper.readValue(body, SearchRequest.class);

                    } catch (JsonProcessingException e) {
                        throw new ParseException("Parsing failed for message received from sqs!");
                    }
                    if(searchRequest == null)
                        throw new AppException("Failed to get search request from sqs!");

                    if(searchRequest.getOffset() == null || searchRequest.getOffset() == 0)
                        searchRequest.setOffset(1L);
                    searchRequest.setOffset(searchRequest.getOffset()%101);

                    if(searchRequest.getOffset() == 0)
                        searchRequest.setOffset(1L);

                    EmailRequest emailRequest = new EmailRequest(searchRequest);

                    return googleSearchService.googleSearch(new GoogleSearchRequest(searchRequest))
                            .flatMap(googleSearchResponse -> {

                                int limit = Integer.min(linksUpperlimit, googleSearchResponse.getItems().size());

                                for(SiteResponse siteResponse: googleSearchResponse.getItems()){
                                    if(limit > 0){
                                        emailRequest.getArticles().add(new Article(siteResponse));
                                    }
                                    limit--;
                                }

                                return sendEmailRequestToSqs(emailRequest)
                                        .map(success -> {
                                            if(!success)
                                                throw new AppException("Could not send email request to email queue!");
                                            sqs.deleteMessage(searchQueueUrl, message.getReceiptHandle());
                                            return success;
                                        });
                            });
                })
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
    }

    @PostConstruct
    public void consumeFeedbackEvent() {
        ReceiveMessageRequest request = new ReceiveMessageRequest(feedbackQueueUrl);

        Flux.interval(Duration.ofSeconds(5))
                .onBackpressureDrop()
                .flatMap(
                        w ->
                                Mono.just(sqs.receiveMessage(request))
                                        .flatMapMany(receiveMessageResult -> Flux.fromIterable(receiveMessageResult.getMessages())))
                .flatMap(message -> {
                    String body = message.getBody();
                    Feedback feedback = null;
                    try {
                        feedback = objectMapper.readValue(body, Feedback.class);

                    } catch (JsonProcessingException e) {
                        throw new ParseException("Parsing failed for message received from sqs!");
                    }
                    if(feedback == null)
                        throw new AppException("Null feedback received!");

                    SearchRequest searchRequest = new SearchRequest(feedback);

                    searchRequest.setOffset(searchRequest.getOffset() + mailDelayInSeconds*1000);

                    return sendSearchRequestToSqs(searchRequest)
                            .map(searchResponse -> {
                                if(!searchResponse.getSuccess())
                                    throw new AppException("Could not send search request to search queue!");
                                sqs.deleteMessage(feedbackQueueUrl, message.getReceiptHandle());
                                return true;
                            });
                })
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
    }

}
