package com.chillimport.server.converter;

import com.chillimport.server.Cell;
import com.chillimport.server.Table;
import com.chillimport.server.config.Configuration;

import java.io.*;
import java.util.*;


/**
 * Reads a CSV file and coverts it into a Table instance
 */
public class CSVConverter implements Converter {


    /**
     * Converts a File representing a 2-dimensional table to an instance of the class Table representing the same table.
     * <p>
     * The converter uses a configuration which tells the mthod how to precess certain elements of the table, for example how to choose a delimiter on
     * CSV-style tables.
     *
     * @param file the CSV file containing the table
     * @param cfg  The configuration to use while processing the File
     *
     * @return the Table representing the File
     *
     * @throws IOException when the File has not been found or is corrupt
     */
    public static Table convert(File file, Configuration cfg) throws IOException {

        Scanner inputStream;
        try {
            inputStream = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new IOException("Conversion to internal table representation failed: Did not find sourcefile.", e);
        }

        Table table = new Table();

        for (int i = 1; i <= cfg.getNumberOfHeaderlines(); i++) {
            inputStream.nextLine();
        }


        while (inputStream.hasNext()) {
            ArrayList<String> extractedLine = new ArrayList<>(Arrays.asList(inputStream.nextLine().split(cfg.getDelimiter())));
            ArrayList<Cell> list = new ArrayList<>();
            for (String stringCell : extractedLine) {
                list.add(new Cell(stringCell)); //Zellen erstellen und zur Liste hinzufügen, alles Strings
            }

            table.appendRow(list);
        }


        return table;
    }


    /**
     * This method converts a Table back to a File.
     * <p>
     * The File should after completion represent a 2-dim. table so that a call to convert() with the same configuration yields an identical instance
     * of Table (i.e. compare(...) == true). The Configuration used is necessary to get an exact output that could have matched an input File from
     * before convert(...) was called on this File.
     *
     * @param table The Table to convert
     * @param cfg   The Configuration to use
     * @param path  The output file path
     *
     * @return the File representing the table with it's form given by the Configuration
     */
    public static void convertBack(Table table, Configuration cfg, String path) throws IOException {
        Iterator<ArrayList<Cell>> rowIterator = table.rowIterator();

        File file = new File(path);
        file.getParentFile().mkdirs();
        if (!file.exists()) {
            file.createNewFile();
        }


        FileWriter writer = new FileWriter(file);

        for (int i = 0; i < cfg.getNumberOfHeaderlines(); i++) {
            if (i == 0) {
                writer.write("#There are no easter eggs up here\n");
            }
            else {
                writer.write("#\n");
            }
        }
        String del = cfg.getDelimiter();
        while (rowIterator.hasNext()) {
            ArrayList<Cell> row = rowIterator.next();
            String rowAsString = "";

            Iterator<Cell> cellIterator = row.iterator();

            while (cellIterator.hasNext()) {
                rowAsString = rowAsString.concat(cellIterator.next().toString());
                rowAsString += del;
            }
            rowAsString = rowAsString.substring(0, rowAsString.length() - 1);

            writer.write(rowAsString + "\n");
        }
        writer.flush();
        writer.close();
    }


    /**
     * Returns the first x rows of a CSV file (or less if it is shorter) for a file preview as a 2-dimensional List of Strings
     *
     * @param file   The file to convert, must be a Table of some sort
     * @param cfg    the configuration after which to Convert the file
     * @param rownum the number of rows to convert
     *
     * @return the first three rows as a LinkedList of LinkedLists
     *
     * @throws IOException when the file is not found
     */
    public static LinkedList<LinkedList<String>> filePreview(File file, Configuration cfg, int rownum) throws IOException {
        Table table = convert(file, cfg);
        LinkedList<LinkedList<String>> firstThreeRowsOfTable = new LinkedList<>();
        Iterator<ArrayList<Cell>> rowIterator = table.rowIterator();
        int maxsize = rownum;

        if (table.getRowCount() < maxsize || maxsize <= 0) {
            maxsize = 3;
        }

        for (int i = 0; i < maxsize; i++) {
            if (rowIterator.hasNext()) {
                ArrayList<Cell> row = rowIterator.next();
                Iterator<Cell> iterator = row.iterator();
                LinkedList<String> stringRow = new LinkedList<>();

                while (iterator.hasNext()) {
                    stringRow.add(iterator.next().toString());
                }

                firstThreeRowsOfTable.add(stringRow);
            }
        }


        return firstThreeRowsOfTable;
    }

}
