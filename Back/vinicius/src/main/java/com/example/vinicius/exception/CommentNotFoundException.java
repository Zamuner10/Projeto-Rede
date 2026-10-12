package com.example.vinicius.exception;

public class CommentNotFoundException extends RuntimeException{
    public CommentNotFoundException(String message){
            super (message);
        }
}
