package com.chillimport.server;

import java.net.*;
import java.text.*;
import java.util.Date;
import java.util.Locale;


/**
 * This class represents a Cell, the base part of a Table. Because there are many different data types in a single Table, this class combines all
 * types into a single object, where only one of the values is not null. This determines the data type of the Cell.
 */
public class Cell {

    /**
     * The cells current data type, is always set at the initialization of the Cell
     */
    private TableDataTypes currentDataType;

    /**
     * All of the different possible data types of a Cell
     */
    private Integer _Integer = null;
    private String _String = null;
    private Date _Date = null;
    private Boolean _Bool = null;
    private Float _Float = null;
    private Double _Double = null;
    private URI _Uri = null;
    private URL _Url = null;

    /**
     * Constructor for creating a Cell of type URL
     *
     * @param _Url the URL that will be stored in the Cell
     */
    public Cell(URL _Url) {
        this._Url = _Url;
        this.currentDataType = TableDataTypes.URL;
    }

    /**
     * Constructor for creating a Cell of type URI
     *
     * @param _Uri the URI that will be stored in the Cell
     */
    public Cell(URI _Uri) {
        this._Uri = _Uri;
        this.currentDataType = TableDataTypes.URI;
    }

    /**
     * Constructor for creating a Cell of type Double
     *
     * @param _Double the double value that will be stored in the Cell
     */
    public Cell(Double _Double) {
        this._Double = _Double;
        this.currentDataType = TableDataTypes.DOUBLE;
    }

    /**
     * Constructor for creating a Cell of type Float
     *
     * @param _Float the floating point number that will be stored in the Cell
     */
    public Cell(Float _Float) {
        this._Float = _Float;
        this.currentDataType = TableDataTypes.FLOAT;
    }

    /**
     * Constructor for creating a Cell of type Boolean
     *
     * @param _Bool the boolean value that will be stored in the Cell
     */
    public Cell(Boolean _Bool) {
        this._Bool = _Bool;
        this.currentDataType = TableDataTypes.BOOL;
    }

    /**
     * Constructor for creating a Cell of type Date
     *
     * @param _Date the Date that will be stored in the Cell
     */
    public Cell(Date _Date) {
        this._Date = (Date) _Date.clone();
        this.currentDataType = TableDataTypes.DATE;
    }

    /**
     * Constructor for creating a Cell of type String
     *
     * @param _String the URL that will be stored in the Cell
     */
    public Cell(String _String) {
        this._String = _String;
        this.currentDataType = TableDataTypes.STRING;
    }

    /**
     * Constructor for creating a Cell of type integer
     *
     * @param _Integer the int value that will be sored in the Cell
     */
    public Cell(Integer _Integer) {
        this._Integer = _Integer;
        this.currentDataType = TableDataTypes.INT;
    }

    /**
     * Constructor for creating an empty Cell (a.k.a. a Cell with value "null")
     */
    public Cell() {
        this.currentDataType = TableDataTypes.NULL;
    }

    /**
     * Returns the raw value of the Cell as an Object. It needs to be converted/casted into the rigth data type by hand.
     *
     * @return the value of the Cell
     */
    public Object get() {
        switch (currentDataType) {
            case INT:
                return _Integer;
            case BOOL:
                return _Bool;
            case DOUBLE:
                return _Double;
            case STRING:
                return _String;
            case URI:
                return _Uri;
            case URL:
                return _Url;
            case DATE:
                return _Date;
            case FLOAT:
                return _Float;
            case NULL:
                return null;
        }

        return null;
    }

    /**
     * returns the value of the Cell as a String (if possible)
     *
     * @return the value of the Cell as String
     *
     * @throws ClassCastException when the Cell Type cannot be converted
     */
    public String toString() {
        switch (currentDataType) {
            case INT:
                return _Integer.toString();
            case BOOL:
                return _Bool.toString();
            case DOUBLE:
                return _Double.toString();
            case STRING:
                return _String;
            case URI:
                return _Uri.toString();
            case URL:
                return _Url.toString();
            case DATE:
                return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(this._Date);
            case FLOAT:
                return _Float.toString();
            case NULL:
                return "null";
        }

        return "null";
    }

    /**
     * returns the value of the Cell as a int (if possible)
     *
     * @return the value of the Cell as Integer
     *
     * @throws ClassCastException when the Cell Type cannot be converted
     */
    public Integer toInteger() throws ClassCastException {
        switch (currentDataType) {
            case INT:
                return _Integer;
            case BOOL:
                if (_Bool) {
                    return 1;
                }
                else {
                    return 0;
                }
            case DOUBLE:
                return _Double.intValue();
            case STRING:
                try {
                    String trimmed = _String.replaceAll("^\"|^'|'$|\"$", "");
                    return Integer.parseInt(trimmed);
                } catch (NumberFormatException e) {
                    throw new ClassCastException("String in this entry represents no Integer.");
                }
            case URI:
                throw new ClassCastException("Cannot convert URI to Integer.");
            case URL:
                throw new ClassCastException("Cannot convert URL to Integer.");
            case DATE:
                throw new ClassCastException("Cannot convert Date to Integer.");
            case FLOAT:
                return _Float.intValue();
            case NULL:
                return null;
        }

        return null;
    }

    /**
     * returns the value of the Cell as a boolean (if possible)
     *
     * @return the value of the Cell as Boolean
     *
     * @throws ClassCastException when the Cell Type cannot be converted
     */
    public Boolean toBoolean() throws ClassCastException {
        switch (currentDataType) {
            case INT:
                if (_Integer.equals(1)) {
                    return true;
                }
                else {
                    if (_Integer.equals(0)) {
                        return false;
                    }
                }
                throw new ClassCastException("Integer has to be 0 or 1 to be converted to Boolean.");
            case BOOL:
                return _Bool;
            case DOUBLE:
                if (_Double.equals(1.0)) {
                    return true;
                }
                else {
                    if (_Double.equals(0.0)) {
                        return false;
                    }
                }
                throw new ClassCastException("Double has to be 0 or 1 to be converted to Boolean.");
            case STRING:
                _String = _String.replaceAll("^\"|^'|'$|\"$", "");
                if (_String.equals("1") || _String.equals("true")
                        || _String.equals("TRUE") || _String.equals("True")
                        || _String.equals("WAHR")) {
                    return true;
                }
                else {
                    if (_String.equals("0") || _String.equals("false")
                            || _String.equals("FALSE") || _String.equals("False")
                            || _String.equals("FALSCH")) {
                        return false;
                    }
                }
                throw new ClassCastException("String cannot be converted to Boolean (has to be true/false or 0/1).");
            case URI:
                throw new ClassCastException("Cannot convert URI to Boolean.");
            case URL:
                throw new ClassCastException("Cannot convert URL to Boolean.");
            case DATE:
                throw new ClassCastException("Cannot convert Date to Boolean.");
            case FLOAT:
                if (_Float.equals(1.0f)) {
                    return true;
                }
                else {
                    if (_Float.equals(0.0f)) {
                        return false;
                    }
                }
                throw new ClassCastException("Float has to be 0 or 1 to be converted to Boolean.");
            case NULL:
                return null;
        }

        return null;
    }

    /**
     * returns the value of the Cell as an URI (if possible)
     *
     * @return the value of the Cell as URI
     *
     * @throws ClassCastException when the Cell Type cannot be converted
     */
    public URI toURI() throws ClassCastException {
        switch (currentDataType) {
            case INT:
                throw new ClassCastException("Cannot convert Integer to URI.");
            case BOOL:
                throw new ClassCastException("Cannot convert Boolean to URI.");
            case DOUBLE:
                throw new ClassCastException("Cannot convert Double to URI.");
            case STRING:

                try {
                    return new URI(_String.replaceAll("^\"|^'|'$|\"$", ""));
                } catch (URISyntaxException e) {
                    throw new ClassCastException("Cannot convert the String to URI.");
                }
            case URI:
                return _Uri;
            case URL:
                try {
                    return _Url.toURI();
                } catch (URISyntaxException e) {
                    throw new ClassCastException("Cannot convert the URL to URI.");
                }

            case DATE:
                throw new ClassCastException("Cannot convert Date to URI.");
            case FLOAT:
                throw new ClassCastException("Cannot convert Float to URI.");
            case NULL:
                return null;
        }

        return null;
    }

    /**
     * returns the value of the Cell as an URL (if possible)
     *
     * @return the value of the Cell as URL
     *
     * @throws ClassCastException when the Cell Type cannot be converted
     */
    public URL toURL() throws ClassCastException {
        switch (currentDataType) {
            case INT:
                throw new ClassCastException("Cannot convert Integer to URL.");
            case BOOL:
                throw new ClassCastException("Cannot convert Boolean to URL.");
            case DOUBLE:
                throw new ClassCastException("Cannot convert Double to URL.");
            case STRING:
                try {
                    return new URL(_String.replaceAll("^\"|^'|'$|\"$", ""));
                } catch (MalformedURLException e) {
                    throw new ClassCastException("Cannot convert the String to URL.");
                }
            case URI:
                try {
                    return _Uri.toURL();
                } catch (MalformedURLException e) {
                    throw new ClassCastException("Cannot convert the URI to URL.");
                }
            case URL:
                return _Url;
            case DATE:
                throw new ClassCastException("Cannot convert Date to URL.");
            case FLOAT:
                throw new ClassCastException("Cannot convert Float to URL.");
            case NULL:
                return null;
        }

        return null;
    }

    /**
     * returns the value of the Cell as a Date (if possible)
     *
     * @return the value of the Cell as Date
     *
     * @throws ClassCastException when the Cell Type cannot be converted
     */
    public Date toDate() throws ClassCastException {
        switch (currentDataType) {
            case DATE:
                return _Date;
            case INT:
                throw new ClassCastException("Cannot convert Integer to Date.");
            case BOOL:
                throw new ClassCastException("Cannot convert Boolean to Date.");
            case DOUBLE:
                throw new ClassCastException("Cannot convert Double to Date.");
            case STRING:
                //TODO String zu Date? Oder nur via DateParser?
                throw new ClassCastException("Cannot convert String to Date.");
            case URI:
                throw new ClassCastException("Cannot convert URI to Date.");
            case URL:
                throw new ClassCastException("Cannot convert URL to Date.");
            case FLOAT:
                throw new ClassCastException("Cannot convert Float to Date.");
            case NULL:
                return null;
        }

        return null;
    }

    /**
     * returns the value of the Cell as a floating point number (if possible)
     *
     * @return the value of the Cell as Float
     *
     * @throws ClassCastException when the Cell Type cannot be converted
     */
    public Float toFloat() throws ClassCastException {
        switch (currentDataType) {
            case INT:
                return _Integer.floatValue();
            case BOOL:
                if (_Bool) {
                    return 1f;
                }
                else {
                    return 0f;
                }
            case DOUBLE:
                return _Double.floatValue();
            case STRING:
                NumberFormat nf;
                String trimmed = _String.replaceAll("^\"|^'|'$|\"$", "");
                if (_String.matches("[0-9]+((\\.[0-9]{3})*,[0-9]*|(\\.[0-9]{3}){2,})?")) {
                    nf = NumberFormat.getInstance(Locale.GERMAN);
                }
                else {
                    if (_String.matches("[0-9]+(,[0-9]{3})*\\.[0-9]*")) {
                        nf = NumberFormat.getInstance(Locale.US);
                    }
                    else {
                        nf = NumberFormat.getInstance();
                    }
                }

                try {
                    return nf.parse(trimmed).floatValue();
                } catch (ParseException e) {
                    throw new ClassCastException("Cannot convert String to Float.");
                }
            case URI:
                throw new ClassCastException("Cannot convert URI to Float.");
            case URL:
                throw new ClassCastException("Cannot convert URL to Float.");
            case DATE:
                throw new ClassCastException("Cannot convert Date to Float.");
            case FLOAT:
                return _Float;
            case NULL:
                return null;
        }

        return null;
    }

    /**
     * returns the value of the Cell as a double (if possible)
     *
     * @return the value of the Cell as Double
     *
     * @throws ClassCastException when the Cell Type cannot be converted
     */
    public Double toDouble() throws ClassCastException {
        switch (currentDataType) {
            case INT:
                return _Integer.doubleValue();
            case BOOL:
                if (_Bool) {
                    return 1.0;
                }
                else {
                    return 0.0;
                }
            case DOUBLE:
                return _Double;
            case STRING:
                NumberFormat nf;
                String trimmed = _String.replaceAll("^\"|^'|'$|\"$", "");
                if (_String.matches("[0-9]+((\\.[0-9]{3})*,[0-9]*|(\\.[0-9]{3}){2,})?")) {
                    nf = NumberFormat.getInstance(Locale.GERMAN);
                }
                else {
                    if (_String.matches("[0-9]+(,[0-9]{3})*\\.[0-9]*")) {
                        nf = NumberFormat.getInstance(Locale.US);
                    }
                    else {
                        nf = NumberFormat.getInstance();
                    }
                }
                try {
                    return nf.parse(trimmed).doubleValue();
                } catch (ParseException e) {
                    throw new ClassCastException("Cannot convert String to Double.");
                }
            case URI:
                throw new ClassCastException("Cannot convert URI to Double.");
            case URL:
                throw new ClassCastException("Cannot convert URL to Double.");
            case DATE:
                throw new ClassCastException("Cannot convert Date to Double.");
            case FLOAT:
                return _Float.doubleValue();
            case NULL:
                return null;
        }

        return null;
    }

    /**
     * Creates and returns a copy of this object.  The precise meaning of "copy" may depend on the class of the object. The general intent is that,
     * for any object {@code x}, the expression:
     * <blockquote>
     * <pre>
     * x.clone() != x</pre></blockquote>
     * will be true, and that the expression:
     * <blockquote>
     * <pre>
     * x.clone().getClass() == x.getClass()</pre></blockquote>
     * will be {@code true}, but these are not absolute requirements. While it is typically the case that:
     * <blockquote>
     * <pre>
     * x.clone().equals(x)</pre></blockquote>
     * will be {@code true}, this is not an absolute requirement.
     * <p>
     *
     * @return the cloned Cell
     */
    public Cell clone() {
        switch (currentDataType) {
            case INT:
                return new Cell(this._Integer);
            case BOOL:
                return new Cell(this._Bool);
            case DOUBLE:
                return new Cell(this._Double);
            case STRING:
                return new Cell(this._String);
            case URI:
                try {
                    return new Cell(new URI(this._Uri.toString()));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                return new Cell(this._Uri);
            case URL:
                try {
                    return new Cell(new URL(this._Url.toString()));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return new Cell(this._Url);
            case DATE:
                return new Cell((Date) this._Date.clone());
            case FLOAT:
                return new Cell(this._Float);
            case NULL:
                return new Cell();
        }
        return new Cell();
    }

    /**
     * returns the data types of this Cell
     *
     * @return the data type as one of the entries in TableDataTypes enum
     */
    public TableDataTypes getCellType() {
        return this.currentDataType;
    }

    public boolean equals(Object o) {

        if (o instanceof Cell) {
            Cell castedO = (Cell) o;

            if (castedO.getCellType().equals(this.getCellType())) {
                switch (currentDataType) {
                    case INT:
                        if (this.toInteger().equals(castedO.toInteger())) {
                            return true;
                        }
                        return false;
                    case BOOL:
                        if (this.toBoolean().equals(castedO.toBoolean())) {
                            return true;
                        }
                        return false;
                    case DOUBLE:
                        if (this.toDouble().equals(castedO.toDouble())) {
                            return true;
                        }
                        return false;
                    case STRING:
                        if (this.toString().equals(castedO.toString())) {
                            return true;
                        }
                        return false;
                    case URI:
                        if (this.toURI().equals(castedO.toURI())) {
                            return true;
                        }
                        return false;
                    case URL:
                        if (this.toURL().equals(castedO.toURL())) {
                            return true;
                        }
                        return false;
                    case DATE:
                        if (this.toDate().equals(castedO.toDate())) {
                            return true;
                        }
                        return false;
                    case FLOAT:
                        if (this.toFloat().equals(castedO.toFloat())) {
                            return true;
                        }
                        return false;
                    case NULL:
                        return true;
                }
            }
        }


        return false;
    }
}
