package com.example.medrese.Core.Util.Exceptions;


import com.example.medrese.Core.Util.Exceptions.Global.NotFoundException;

public class PlaylistNotFoundException extends NotFoundException {
    public PlaylistNotFoundException(String message) {
        super(message);
    }
}
