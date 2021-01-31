package com.ykn.article_finder.dtos.search;

import com.ykn.article_finder.dtos.email.Feedback;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class SearchRequest {

    String query;

    Long offset;

    EmailContext emailContext;

    public SearchRequest(Feedback feedback){
        this.query = feedback.getEmailRequest().getQuery();
        this.offset = feedback.getEmailRequest().getOffset();
        this.emailContext = new EmailContext(feedback);
    }

    public SearchRequest(FlatSearchRequest flatReq){
        this.query = flatReq.getQuery();
        this.offset = flatReq.getOffset();
        this.emailContext = new EmailContext(flatReq);
    }

}
