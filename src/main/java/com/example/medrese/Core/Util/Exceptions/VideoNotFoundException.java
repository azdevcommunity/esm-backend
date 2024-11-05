package com.example.medrese.Core.Util.Exceptions;


import com.example.medrese.Core.Util.Exceptions.Global.NotFoundException;

public class VideoNotFoundException extends NotFoundException {
    public VideoNotFoundException(String message) {
        super(message);
    }
}
