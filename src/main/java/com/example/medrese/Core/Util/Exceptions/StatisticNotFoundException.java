package com.example.medrese.Core.Util.Exceptions;


import com.example.medrese.Core.Util.Exceptions.Global.NotFoundException;

public class StatisticNotFoundException extends NotFoundException {
    public StatisticNotFoundException(String message) {
        super(message);
    }
}
