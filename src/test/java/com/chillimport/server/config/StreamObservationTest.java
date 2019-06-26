package com.chillimport.server.config;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class StreamObservationTest {

    @Test
    public void equals() {
        int[] i = {0, 1, 20};
        StreamObservation obs = new StreamObservation(12, false, i);
        assertEquals(obs, obs);
        assertNotEquals(obs, null);
    }
}