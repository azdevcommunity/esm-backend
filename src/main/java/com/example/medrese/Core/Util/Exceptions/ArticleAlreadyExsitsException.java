package com.example.medrese.Core.Util.Exceptions;

public class ArticleAlreadyExsitsException extends RuntimeException{
    public ArticleAlreadyExsitsException(String message) {
        super(message);
    }
}
