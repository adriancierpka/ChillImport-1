package com.chillimport.server.converter;

import java.io.IOException;


public class ConverterException extends IOException {

    public ConverterException() {

    }

    public ConverterException(String message) {
        super(message);
    }
}
