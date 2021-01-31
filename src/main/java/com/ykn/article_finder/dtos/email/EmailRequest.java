package com.ykn.article_finder.dtos.email;

import com.ykn.article_finder.dtos.search.SearchRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class EmailRequest {

    String trackerId;

    String userId;

    String name;

    String email;

    //like knowledge
    String fieldType;

    //like learning
    String fieldAction;

    Long sendAfterDate;

    List<Article> articles = new ArrayList<>();

    String query;

    Long offset;

    public EmailRequest(SearchRequest searchRequest){
        this.name = searchRequest.getEmailContext().getName();
        this.email = searchRequest.getEmailContext().getEmail();
        this.fieldType = searchRequest.getEmailContext().getFieldType();
        this.fieldAction = searchRequest.getEmailContext().getFieldAction();
        this.sendAfterDate = searchRequest.getEmailContext().getSendAfterDate();
        this.query = searchRequest.getQuery();
        this.offset = searchRequest.getOffset();
    }

}
