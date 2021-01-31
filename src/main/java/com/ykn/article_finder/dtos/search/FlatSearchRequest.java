package com.ykn.article_finder.dtos.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class FlatSearchRequest {

    String query;

    Long offset;

    String trackerId;

    String userId;

    String name;

    String email;

    //like knowledge
    String fieldType;

    //like learning
    String fieldAction;

    Long sendAfterDate;

}
