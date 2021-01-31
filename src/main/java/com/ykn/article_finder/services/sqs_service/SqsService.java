package com.ykn.article_finder.services.sqs_service;

import com.ykn.article_finder.dtos.search.SearchRequest;
import com.ykn.article_finder.dtos.search.SearchResponse;
import reactor.core.publisher.Mono;

public interface SqsService {

    Mono<SearchResponse> sendSearchRequestToSqs(SearchRequest searchRequest);

}
