package com.example.medrese.Core.Util.Exceptions;


import com.example.medrese.Core.Util.Exceptions.Global.NotFoundException;

public class AuthorNotFoundException extends NotFoundException {
    public AuthorNotFoundException(String message) {
        super(message);
    }
}
