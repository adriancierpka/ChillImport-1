package com.chillimport.server.config;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class StringColumnTest {

    @Test
    public void equals() {
        StringColumn x = new StringColumn("Hello", 21);
        assertEquals(x, x);
        assertNotEquals(x, null);
    }


}