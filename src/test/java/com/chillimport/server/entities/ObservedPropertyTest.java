package com.chillimport.server.entities;

import de.fraunhofer.iosb.ilt.sta.model.IdLong;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class ObservedPropertyTest {

    private ObservedProperty op0;
    private ObservedProperty op11;
    private ObservedProperty op12;
    private ObservedProperty op2;

    @Before
    public void setUp() {
        op0 = new ObservedProperty("name", "desc", null);
        op11 = new ObservedProperty("name", "desc", "def1");
        op12 = new ObservedProperty("name", "desc", "def1");
        op2 = new ObservedProperty("name", "desc", "def2");
    }

    @Test
    public void getDefinition() {
        assertNull(op0.getDefinition());
        assertEquals(op11.getDefinition(), "def1");
        assertEquals(op12.getDefinition(), "def1");
        assertEquals(op2.getDefinition(), "def2");
    }

    @Test
    public void setDefinition() {
        ObservedProperty observedProperty = new ObservedProperty();
        assertNull(observedProperty.getDefinition());
        observedProperty.setDefinition("def");
        assertEquals(observedProperty.getDefinition(), "def");
    }

    @Test
    public void convertToFrostStandard() {
        de.fraunhofer.iosb.ilt.sta.model.ObservedProperty convertedOP = new de.fraunhofer.iosb.ilt.sta.model.ObservedProperty();
        try {
            convertedOP = op2.convertToFrostStandard();
        } catch (URISyntaxException e) {
            System.out.println("URISyntaxException");
            e.printStackTrace();
        }

        assertEquals(convertedOP.getName(), op2.getName());
        assertEquals(convertedOP.getDescription(), op2.getDescription());
        assertEquals(convertedOP.getDefinition(), op2.getDefinition());

        //set frostId
        IdLong id = new IdLong((long) 42);
        convertedOP.setId(id);
        ObservedProperty reconvertedOP = new ObservedProperty(convertedOP);
        assertEquals(reconvertedOP, op2);
    }

    @Test
    public void convertBack() throws URISyntaxException {
        de.fraunhofer.iosb.ilt.sta.model.ObservedProperty opsprop = new de.fraunhofer.iosb.ilt.sta.model.ObservedProperty("name",
                                                                                                                          new URI("def2"),
                                                                                                                          "desc");

        //set frostId
        IdLong id = new IdLong((long) 42);
        opsprop.setId(id);
        ObservedProperty reconvertedOP = new ObservedProperty(opsprop);
        assertEquals(reconvertedOP, op2);
    }

    @Test
    public void equals() {
        assertEquals(op11, op12);
        assertNotEquals(op11, op0);
        assertNotEquals(op11, op2);
        assertNotEquals(op11, "");
    }
}