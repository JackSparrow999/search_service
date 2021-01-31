package com.ykn.article_finder.exceptions;

public class ApiFailedException extends RuntimeException {

    public ApiFailedException(String message){
        super(message);
    }

}
