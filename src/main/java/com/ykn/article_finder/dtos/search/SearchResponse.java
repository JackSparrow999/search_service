package com.ykn.article_finder.dtos.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class SearchResponse {

    Boolean success;

    String message;

}
