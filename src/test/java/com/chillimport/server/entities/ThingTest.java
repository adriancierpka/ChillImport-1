package com.chillimport.server.entities;


import de.fraunhofer.iosb.ilt.sta.model.IdLong;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.chillimport.server.FrostSetup;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class ThingTest {

    private Thing t0;
    private Thing t11;
    private Thing t12;
    private Thing t2;
    private Thing t3;

    private Map<String, Object> pmap1;
    private Map<String, Object> pmap2;

    private Location loc11;
    private Location loc12;
    private Location loc2;
    
    private static String url;
    
    @BeforeClass 
    public static void beforeClass() {
    	url = FrostSetup.getFrostURL();
    }


    @Before
    public void setUp() {
        pmap1 = new HashMap<>();
        pmap1.put("s1", "o1");
        pmap1.put("s2", "o1");
        pmap1.put("s3", "o2");
        pmap1.put("s4", 67);
        pmap1.put("s5", true);

        pmap2 = new HashMap<>();
        pmap2.put("x", pmap1);

        String locString = "{\n" +
                "       \"type\": \"Point\",\n" +
                "       \"coordinates\": [170.0, 45.0]" +
                "}";


        loc11 = new Location("name", "desc", "encType", "loc");
        loc12 = new Location("name", "desc", "encType", "loc");
        loc2 = new Location("name2", "desc", "application/vnd.geo+json", locString); //valid location

        t0 = new Thing("name", "desc", null, null);
        t11 = new Thing("name", "desc", pmap1, loc11);
        t12 = new Thing("name", "desc", pmap1, loc12);
        t2 = new Thing("name", "desc", pmap1, loc2);
        t3 = new Thing("name", "desc", pmap2, loc11);
    }

    @Test
    public void getProperties() {
        assertNull(t0.getProperties());
        assertEquals(t11.getProperties(), pmap1);
        assertEquals(t12.getProperties(), pmap1);
        assertEquals(t2.getProperties(), pmap1);
        assertEquals(t3.getProperties(), pmap2);
    }

    @Test
    public void setProperties() {
        Thing thing = new Thing();
        assertNull(thing.getProperties());
        thing.setProperties(pmap1);
        assertEquals(thing.getProperties(), pmap1);
    }

    @Test
    public void getLocation() {
        assertNull(t0.getLocation());
        assertEquals(t11.getLocation(), loc11);
        assertEquals(t12.getLocation(), loc12);
        assertEquals(t2.getLocation(), loc2);
        assertEquals(t3.getLocation(), loc11);
    }

    @Test
    public void setLocation() {
        Thing thing = new Thing();
        assertNull(thing.getLocation());
        thing.setLocation(loc2);
        assertEquals(thing.getLocation(), loc2);
    }

    @Test
    public void convertToFrostStandard() throws IOException {
        de.fraunhofer.iosb.ilt.sta.model.Thing convertedThing = new de.fraunhofer.iosb.ilt.sta.model.Thing();
        try {
            convertedThing = t2.convertToFrostStandard(new URL(url));
        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        }

        assertEquals(convertedThing.getName(), t2.getName());
        assertEquals(convertedThing.getDescription(), t2.getDescription());
        assertEquals(convertedThing.getProperties(), t2.getProperties());
        assertEquals(convertedThing.getLocations().fullIterator().next(), t2.getLocation().convertToFrostStandard(new URL(url)));

        //set frostIds
        convertedThing.setId(new IdLong((long) 42));

        for (de.fraunhofer.iosb.ilt.sta.model.Location l : convertedThing.getLocations()) {
            l.setId(new IdLong((long) 42));
        }
        Thing reconvertedThing = new Thing(convertedThing);

        assertEquals(reconvertedThing, t2);
    }

    @Test
    public void convertBack() throws IOException {
        de.fraunhofer.iosb.ilt.sta.model.Thing thing = new de.fraunhofer.iosb.ilt.sta.model.Thing("name", "desc");
        thing.setProperties(pmap1);
        thing.getLocations().add(loc2.convertToFrostStandard(new URL(url)));

        //set frostIds
        thing.setId(new IdLong((long) 42));
        for (de.fraunhofer.iosb.ilt.sta.model.Location l : thing.getLocations()) {
            l.setId(new IdLong((long) 42));
        }

        Thing reconvertedThing = new Thing(thing);

        assertEquals(reconvertedThing, t2);
    }
    
    @Ignore  //was wird hier getestet?
    @Test
    public void convertBackwithoutLoc() {
        de.fraunhofer.iosb.ilt.sta.model.Thing thing = new de.fraunhofer.iosb.ilt.sta.model.Thing("name", "desc", pmap1);
        thing.setId(new IdLong((long) 1));
        new Thing(thing);
    }

    @Test
    public void equals() {
        assertEquals(t11, t12);
        assertNotEquals(t11, t0);
        assertNotEquals(t11, t2);
        assertNotEquals(t11, t3);
        assertNotEquals(t11, "");
    }
}