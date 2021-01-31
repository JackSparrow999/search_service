package com.ykn.article_finder.dtos.search;

import com.ykn.article_finder.dtos.email.Feedback;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class EmailContext {

    String trackerId;

    String userId;

    String name;

    String email;

    //like knowledge
    String fieldType;

    //like learning
    String fieldAction;

    Long sendAfterDate;

    public EmailContext(Feedback feedback){
        this.trackerId = feedback.getEmailRequest().getTrackerId();
        this.userId = feedback.getEmailRequest().getUserId();
        this.name = feedback.getEmailRequest().getName();
        this.email = feedback.getEmailRequest().getEmail();
        this.fieldType = feedback.getEmailRequest().getFieldType();
        this.fieldAction = feedback.getEmailRequest().getFieldAction();
        this.sendAfterDate = feedback.getEmailRequest().getSendAfterDate();
    }

    public EmailContext(FlatSearchRequest flatReq){
        this.trackerId = flatReq.getTrackerId();
        this.userId = flatReq.getUserId();
        this.name = flatReq.getName();
        this.email = flatReq.getEmail();
        this.fieldType = flatReq.getFieldType();
        this.fieldAction = flatReq.getFieldAction();
        this.sendAfterDate = flatReq.getSendAfterDate();
    }

}
