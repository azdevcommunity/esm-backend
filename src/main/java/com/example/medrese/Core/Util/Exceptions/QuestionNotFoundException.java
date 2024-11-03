package com.example.medrese.Core.Util.Exceptions;

import com.msys.esm.Core.Util.Exceptions.Global.NotFoundException;

public class QuestionNotFoundException extends NotFoundException {
    public QuestionNotFoundException(String message) {
        super(message);
    }
}
