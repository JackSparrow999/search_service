package com.ykn.article_finder.clients;

import com.ykn.article_finder.dtos.googlesearch.GoogleSearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@Component
public class GoogleSearchClient {

    WebClient webclient;

    @Value("${google.base_url}")
    String baseUrl;

    @PostConstruct
    public void init(){
        webclient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Mono<GoogleSearchResponse> googleSearch(String searchEngineId,
                                                   String apiKey,
                                                   String searchString,
                                                   Long offset){
        return webclient.get()
                .uri(uri -> uri.path("/customsearch/v1")
                        .queryParam("cx", searchEngineId)
                        .queryParam("key", apiKey)
                        .queryParam("q", searchString)
                        .queryParam("start", offset)
                        .build())
                .retrieve()
                .bodyToMono(GoogleSearchResponse.class);
    }

}
