package com.example.medrese.Core.Util.Exceptions;

import com.msys.esm.Core.Util.Exceptions.Global.NotFoundException;

public class AuthorNotFoundException extends NotFoundException {
    public AuthorNotFoundException(String message) {
        super(message);
    }
}
