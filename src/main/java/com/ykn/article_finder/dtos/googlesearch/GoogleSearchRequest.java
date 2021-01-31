package com.ykn.article_finder.dtos.googlesearch;

import com.ykn.article_finder.dtos.search.SearchRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class GoogleSearchRequest {

    String searchString;

    Long offset;

    public GoogleSearchRequest(SearchRequest searchRequest){
        this.searchString = searchRequest.getQuery();
        this.offset = searchRequest.getOffset();
    }

}
