package com.chillimport.server;

import org.junit.Test;

import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class CellTest {

    Cell dbl = new Cell(0.0);
    Cell dbl2 = new Cell(1.0);
    Cell str = new Cell("true");
    Cell str2 = new Cell("false");
    Cell flt = new Cell(0.0f);
    Cell flt2 = new Cell(1.0f);
    Cell in = new Cell(1);
    Cell in2 = new Cell(0);
    Date date = new Date();
    Cell dt = new Cell(date);
    URI x = new URI("ftp://ftp.is.co.za/rfc/rfc1808.txt");
    Cell uri = new Cell(x);
    Cell url = new Cell(new URL("https://lol.com"));
    Cell bl = new Cell(true);
    Cell bl2 = new Cell(false);
    Cell istr = new Cell("0");
    Cell istr2 = new Cell("1");

    public CellTest() throws URISyntaxException, MalformedURLException {
    }

    @Test
    public void toBoolean() {
        assertThrows(ClassCastException.class, () -> dt.toBoolean());
        assertThrows(ClassCastException.class, () -> uri.toBoolean());
        assertThrows(ClassCastException.class, () -> url.toBoolean());
        assertTrue(!dbl.toBoolean() && dbl2.toBoolean() && str.toBoolean() && !str2.toBoolean() && !flt.toBoolean() &&
                           flt2.toBoolean() && in.toBoolean() && !in2.toBoolean() && bl.toBoolean() && !bl2.toBoolean() && !istr.toBoolean() && istr2.toBoolean());
    }

    @Test
    public void toInteger() {
        assertEquals(1, (int) in.toInteger());
        assertEquals(0, (int) in2.toInteger());
        assertEquals(1, (int) flt2.toInteger());
        assertEquals(0, (int) flt.toInteger());
        assertEquals(1, (int) dbl2.toInteger());
        assertEquals(0, (int) dbl.toInteger());
        assertEquals(1, (int) istr2.toInteger());
        assertEquals(0, (int) istr.toInteger());
        assertEquals(1, (int) bl.toInteger());
        assertEquals(0, (int) bl2.toInteger());
        assertThrows(ClassCastException.class, () -> dt.toInteger());
        assertThrows(ClassCastException.class, () -> uri.toInteger());
        assertThrows(ClassCastException.class, () -> url.toInteger());
    }

    @Test
    public void toFloat() {
        assertEquals(1f, (float) in.toFloat());
        assertEquals(0f, (float) in2.toFloat());
        assertEquals(1f, (float) flt2.toFloat());
        assertEquals(0f, (float) flt.toFloat());
        assertEquals(1f, (float) dbl2.toFloat());
        assertEquals(0f, (float) dbl.toFloat());
        assertEquals(1f, (float) istr2.toFloat());
        assertEquals(0f, (float) istr.toFloat());
        assertEquals(1f, (float) bl.toFloat());
        assertEquals(0f, (float) bl2.toFloat());
        assertThrows(ClassCastException.class, () -> dt.toFloat());
        assertThrows(ClassCastException.class, () -> uri.toFloat());
        assertThrows(ClassCastException.class, () -> url.toFloat());
    }

    @Test
    public void toDouble() {
        assertEquals(1d, (double) in.toDouble());
        assertEquals(0d, (double) in2.toDouble());
        assertEquals(1d, (double) flt2.toDouble());
        assertEquals(0d, (double) flt.toDouble());
        assertEquals(1d, (double) dbl2.toDouble());
        assertEquals(0d, (double) dbl.toDouble());
        assertEquals(1d, (double) istr2.toDouble());
        assertEquals(0d, (double) istr.toDouble());
        assertEquals(1d, (double) bl.toDouble());
        assertEquals(0d, (double) bl2.toDouble());
        assertThrows(ClassCastException.class, () -> dt.toDouble());
        assertThrows(ClassCastException.class, () -> uri.toDouble());
        assertThrows(ClassCastException.class, () -> url.toDouble());
    }

    @Test
    public void toStr() {
        assertEquals("1", in.toString());
        assertEquals("0", in2.toString());
        assertEquals("1.0", flt2.toString());
        assertEquals("0.0", flt.toString());
        assertEquals("1.0", dbl2.toString());
        assertEquals("0.0", dbl.toString());
        assertEquals("1", istr2.toString());
        assertEquals("0", istr.toString());
        assertEquals("true", bl.toString());
        assertEquals("false", bl2.toString());
        assertEquals(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(date), dt.toString());
        assertEquals("ftp://ftp.is.co.za/rfc/rfc1808.txt", uri.toString());
        assertEquals("https://lol.com", url.toString());
    }

    @Test
    public void toDate() {
        assertThrows(ClassCastException.class, () -> uri.toDate());
        assertThrows(ClassCastException.class, () -> url.toDate());
        assertThrows(ClassCastException.class, () -> in.toDate());
        assertThrows(ClassCastException.class, () -> in2.toDate());
        assertThrows(ClassCastException.class, () -> flt.toDate());
        assertThrows(ClassCastException.class, () -> flt2.toDate());
        assertThrows(ClassCastException.class, () -> dbl.toDate());
        assertThrows(ClassCastException.class, () -> dbl2.toDate());
        assertThrows(ClassCastException.class, () -> istr.toDate());
        assertThrows(ClassCastException.class, () -> istr2.toDate());
        assertThrows(ClassCastException.class, () -> bl.toDate());
        assertThrows(ClassCastException.class, () -> bl2.toDate());
        assertEquals(dt.toDate(), date);
    }

    @Test
    public void toURL() throws MalformedURLException {
        assertEquals(uri.toURL(), new URL("ftp://ftp.is.co.za/rfc/rfc1808.txt"));
        assertThrows(ClassCastException.class, () -> dt.toURL());
        assertThrows(ClassCastException.class, () -> in.toURL());
        assertThrows(ClassCastException.class, () -> in2.toURL());
        assertThrows(ClassCastException.class, () -> flt.toURL());
        assertThrows(ClassCastException.class, () -> flt2.toURL());
        assertThrows(ClassCastException.class, () -> dbl.toURL());
        assertThrows(ClassCastException.class, () -> dbl2.toURL());
        assertThrows(ClassCastException.class, () -> istr.toURL());
        assertThrows(ClassCastException.class, () -> istr2.toURL());
        assertThrows(ClassCastException.class, () -> bl.toURL());
        assertThrows(ClassCastException.class, () -> bl2.toURL());
        assertEquals(url.toURL(), new URL("https://lol.com"));
    }

    @Test
    public void toURI() {
        assertThrows(ClassCastException.class, () -> dt.toURL());
        assertThrows(ClassCastException.class, () -> in.toURL());
        assertThrows(ClassCastException.class, () -> in2.toURL());
        assertThrows(ClassCastException.class, () -> flt.toURL());
        assertThrows(ClassCastException.class, () -> flt2.toURL());
        assertThrows(ClassCastException.class, () -> dbl.toURL());
        assertThrows(ClassCastException.class, () -> dbl2.toURL());
        assertThrows(ClassCastException.class, () -> istr.toURL());
        assertThrows(ClassCastException.class, () -> istr2.toURL());
        assertThrows(ClassCastException.class, () -> bl.toURL());
        assertThrows(ClassCastException.class, () -> bl2.toURL());
        // URI's werden dynamisch erzeugt, assertEquals daher nicht möglich
    }
}
