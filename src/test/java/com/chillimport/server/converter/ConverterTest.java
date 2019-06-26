package com.chillimport.server.converter;

import com.chillimport.server.Table;
import com.chillimport.server.config.Configuration;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;


public class ConverterTest {

    @Test
    public void nullTests() throws IOException {
        assertTrue(Converter.convert(new File("x"), new Configuration()) == null);
        assertTrue(Converter.convertBack(new Table(), new Configuration(), "x") == null);
        assertTrue(Converter.filePreview(new File("x"), new Configuration(), 0) == null);
    }

}
