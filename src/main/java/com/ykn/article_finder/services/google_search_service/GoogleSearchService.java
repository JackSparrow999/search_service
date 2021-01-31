package com.ykn.article_finder.services.google_search_service;

import com.ykn.article_finder.dtos.googlesearch.GoogleSearchRequest;
import com.ykn.article_finder.dtos.googlesearch.GoogleSearchResponse;
import reactor.core.publisher.Mono;

public interface GoogleSearchService {

    Mono<GoogleSearchResponse> googleSearch(GoogleSearchRequest googleSearchRequest);

}
