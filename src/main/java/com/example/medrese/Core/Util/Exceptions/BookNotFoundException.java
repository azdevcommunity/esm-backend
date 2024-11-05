package com.example.medrese.Core.Util.Exceptions;


import com.example.medrese.Core.Util.Exceptions.Global.NotFoundException;

public class BookNotFoundException extends NotFoundException {
    public BookNotFoundException(String message) {
        super(message);
    }
}
