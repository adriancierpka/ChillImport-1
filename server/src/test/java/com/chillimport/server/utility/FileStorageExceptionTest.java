package com.chillimport.server.utility;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class FileStorageExceptionTest {

    @Test
    public void constructors() {
        FileStorageException test = new FileStorageException("hallo");
        assertTrue(test instanceof FileStorageException);
        assertEquals(test.getMessage(), "hallo");
        assertEquals(test.getCause(), null);
        FileStorageException test2 = new FileStorageException("hallo", new IOException());
        assertTrue(test2.getCause() instanceof IOException);
    }

}
