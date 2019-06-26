package com.chillimport.server.config;

import java.util.HashMap;
import java.util.Objects;


/**
 * This class saves the mappings of special tokens example: false -> 0 or: not a number -> NaN
 */
public class MagicNumberMap {

    /**
     * column of the mapping
     */
    private int column;

    /**
     * The String to replace
     */
    private String first;

    /**
     * The String which replaces the first
     */
    private String second;

    /**
     * empty constructor
     */
    public MagicNumberMap() {
    }

    /**
     * Constructor
     *
     * @param column Column where the mapping should be applied
     * @param first  the string to replace
     * @param second the string, which replaces first
     */
    public MagicNumberMap(int column, String first, String second) {
        setColumn(column);
        setFirst(first);
        setSecond(second);
    }

    /**
     * creates a Hashmap from an array of MagicNumberMap s
     *
     * @param map the array of MagicNumberMap
     *
     * @return The resulting hashmap
     */
    public static HashMap<StringColumn, String> arrayToHashMap(MagicNumberMap[] map) {
        HashMap<StringColumn, String> resultMap = new HashMap<StringColumn, String>();
        for (MagicNumberMap element : map) {
            StringColumn tempStringColumn = new StringColumn(element.first, element.column);
            resultMap.put(tempStringColumn, element.second);
        }
        return resultMap;
    }

    /**
     * Getter for the column
     *
     * @return the column
     */
    public int getColumn() {
        return column;
    }

    /**
     * Setter For the column
     *
     * @param column
     */
    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * Getter for the string which will be replaced
     *
     * @return the string
     */
    public String getFirst() {
        return first;
    }

    /**
     * Setter for the string which will be replaced
     *
     * @param first
     */
    public void setFirst(String first) {
        this.first = first;
    }

    /**
     * Getter for the string which replaces the first string
     *
     * @return
     */
    public String getSecond() {
        return second;
    }

    /**
     * Setter for the sting which replaces the first string
     *
     * @param second
     */
    public void setSecond(String second) {
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MagicNumberMap that = (MagicNumberMap) o;
        return column == that.column &&
                Objects.equals(first, that.first) &&
                Objects.equals(second, that.second);
    }
}


