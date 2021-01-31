package com.ykn.article_finder.services.google_search_service;

import com.ykn.article_finder.clients.GoogleSearchClient;
import com.ykn.article_finder.dtos.googlesearch.GoogleSearchRequest;
import com.ykn.article_finder.dtos.googlesearch.GoogleSearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GoogleSearchServiceImpl implements GoogleSearchService{

    @Value("${google.search_engine_id}")
    String searchEngineId;

    @Value("${google.api_key}")
    String apiKey;

    @Autowired
    private GoogleSearchClient googleSearchClient;

    @Override
    public Mono<GoogleSearchResponse> googleSearch(GoogleSearchRequest googleSearchRequest) {
        return googleSearchClient.googleSearch(searchEngineId, apiKey, googleSearchRequest.getSearchString(), googleSearchRequest.getOffset());
    }
}
