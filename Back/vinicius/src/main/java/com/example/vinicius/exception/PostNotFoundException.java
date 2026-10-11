package com.example.vinicius.exception;

public class PostNotFoundException  extends RuntimeException{

    public PostNotFoundException(String message){
        super(message);
    }
}
