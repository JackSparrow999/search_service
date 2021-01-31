package com.ykn.article_finder.controllers;

import com.ykn.article_finder.dtos.googlesearch.GoogleSearchRequest;
import com.ykn.article_finder.dtos.googlesearch.GoogleSearchResponse;
import com.ykn.article_finder.dtos.search.FlatSearchRequest;
import com.ykn.article_finder.dtos.search.SearchRequest;
import com.ykn.article_finder.dtos.search.SearchResponse;
import com.ykn.article_finder.services.google_search_service.GoogleSearchService;
import com.ykn.article_finder.services.sqs_service.SqsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
public class SearchController {

    @Autowired
    private SqsService sqsService;

    @Autowired
    private GoogleSearchService googleSearchService;

    @PostMapping("/search")
    public Mono<SearchResponse> articleSearch(@RequestBody FlatSearchRequest flatSearchRequest){
        return sqsService.sendSearchRequestToSqs(new SearchRequest(flatSearchRequest));
    }

    @PostMapping("/test")
    public Mono<GoogleSearchResponse> googleIt(@RequestBody GoogleSearchRequest googleSearchRequest){
        return googleSearchService.googleSearch(googleSearchRequest);
    }

}
