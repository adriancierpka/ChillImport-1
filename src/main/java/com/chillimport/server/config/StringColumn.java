package com.chillimport.server.config;

import java.util.Objects;


/**
 * This is a wrapper-class. It combines a string with an integer.
 */
public class StringColumn {

    private String string;
    private int column;

    /**
     * empty constructor
     */
    public StringColumn() {
    }

    /**
     * A constructor
     *
     * @param string a string
     * @param column a column
     */
    public StringColumn(String string, int column) {
        this.string = string;
        this.column = column;
    }

    /**
     * Getter for the string
     *
     * @return
     */
    public String getString() {
        return string;
    }

    /**
     * Setter for the string
     *
     * @param string
     */
    public void setString(String string) {
        this.string = string;
    }

    /**
     * Getter for the column
     *
     * @return
     */
    public int getColumn() {
        return column;
    }

    /**
     * Setter for the column
     *
     * @param column
     */
    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StringColumn that = (StringColumn) o;
        return column == that.column &&
                Objects.equals(string, that.string);
    }

    @Override
    public int hashCode() {

        return Objects.hash(string, column);
    }
}
