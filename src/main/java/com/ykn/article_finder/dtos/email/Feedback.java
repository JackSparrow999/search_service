package com.ykn.article_finder.dtos.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class Feedback {

    EmailRequest emailRequest;

    Boolean success;

}
