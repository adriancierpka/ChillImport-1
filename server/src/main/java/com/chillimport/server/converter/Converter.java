package com.chillimport.server.converter;

import com.chillimport.server.Table;
import com.chillimport.server.config.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.List;


public interface Converter {

    /**
     * Converts a File representing a 2-dimensional table to an instance of the class Table representing the same table.
     * <p>
     * The converter uses a configuration which tells the mthod how to precess certain elements of the table, for example how to choose a delimiter on
     * CSV-style tables.
     *
     * @param file the File containing the table
     * @param cfg  The configuration to use while processing the File
     *
     * @return the Table representing the File
     *
     * @throws IOException when the File has not been found or is corrupt
     */
    static Table convert(File file, Configuration cfg) throws IOException {
        return null;
    }

    /**
     * This method converts a Table back to a File.
     * <p>
     * The File should after completion represent a 2-dim. table so that a call to convert() with the same configuration yields an identical instance
     * of Tabe (i.e. compare(...) == true). The Configuration used is necessary to get an exact output that could have matched an input File from
     * before convert(...) was called on this File.
     *
     * @param table the Table to convert
     * @param cfg   the Configuration to use
     *
     * @return the File representing the table with it's form given by the Configuration
     */
    static File convertBack(Table table, Configuration cfg, String path) throws IOException {
        return null;
    }


    /**
     * Returns the first x rows of a File (or less if it is shorter) for a file preview as a 2-dimensional List of Strings
     *
     * @param file   The file to convert, must be a Table of some sort
     * @param cfg    the configuration after which to Convert the file
     * @param rownum the number of rows to convert
     *
     * @return the first rownum rows as a LinkedList of LinkedLists
     *
     * @throws IOException when the file is not found
     */
    static List<List<String>> filePreview(File file, Configuration cfg, int rownum) throws IOException {
        return null;
    }


}
