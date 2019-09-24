package com.chillimport.server.config;

import com.chillimport.server.converter.ConverterException;
import com.chillimport.server.errors.LogManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;


/**
 * This class saves all important settings
 */
public class Configuration {


    private int id;
    private String name;
    private String delimiter;
    private int numberOfHeaderlines;
    private String timezone;
    private StringColumn[] dateTime;
    private StreamObservation[] streamData;
    private MagicNumberMap[] mapOfMagicNumbers;
    private DataType dataType;
    private URL frostURL;

    /**
     * A constructor for the Configuration class
     *
     * @param id                  The id of the Configuration
     * @param name                The name
     * @param delimiter           The delimiter between columns
     * @param numberOfHeaderlines Number of Headerlines
     * @param timezone            a timezone as String
     * @param dateTime            An Array of Regex-Strings and Columns to find and interpret the date and time
     * @param streamData          An array of StreamObservation-Objects. A StreamObservation-Object saves the id of a datastream, a boolean to make
     *                            sure its a multi or single datastream and an array of columns (int)
     * @param mapOfMagicNumbers   A MagicNumberMap-Object. Needed to map special tokens to another. Example: 12th November -> 12.11
     * @param dataType            CSV or EXCEL
     */
    public Configuration(int id,
                         String name,
                         String delimiter,
                         int numberOfHeaderlines,
                         String timezone,
                         StringColumn[] dateTime,
                         StreamObservation[] streamData,
                         MagicNumberMap[] mapOfMagicNumbers,
                         DataType dataType,
                         URL frostURL) {
        this.setId(id);
        this.setName(name);
        this.setDelimiter(delimiter);
        this.setNumberOfHeaderlines(numberOfHeaderlines);
        this.setTimezone(timezone);
        this.setDateTime(dateTime);
        this.setStreamData(streamData);
        this.setMapOfMagicNumbers(mapOfMagicNumbers);
        this.setDataType(dataType);
        this.setFrostURL(frostURL);
    }

    public Configuration() {
    }


    /**
     * This constructor creates a dummy Configuration for file preview. Only the number of header lines, the delimiter and the data type can be changed.
     *
     * @param headerLines how many headerlines to use
     * @param delimiter   the delimiter
     * @param dType       the file data type
     * @throws MalformedURLException 
     */
    public Configuration(int headerLines, String delimiter, DataType dType) throws MalformedURLException {
        StringColumn[] dateTime1 = {new StringColumn("TT-HH", 10), new StringColumn("MM-SS", 12)};
        int[] arr1 = {2, 8, 5};
        StreamObservation[] streamData1 = {new StreamObservation(123456, true, arr1)};
        MagicNumberMap[] map1 = new MagicNumberMap[3];

        map1[0] = new MagicNumberMap(1, "true", "wahr");
        map1[1] = new MagicNumberMap(2, "wahr", "true");
        map1[2] = new MagicNumberMap(4, "trueheit", "wahrheit");


        this.id = 12;
        this.name = "dummy";
        this.delimiter = delimiter;
        this.numberOfHeaderlines = headerLines;
        this.timezone = "EAT";
        this.dateTime = dateTime1;
        this.streamData = streamData1;
        this.mapOfMagicNumbers = map1;
        this.dataType = dType;
        this.frostURL = new URL("https://www.google.com");
    }


    /**
     * This method converts a JSON-Object into a Java-Object
     *
     * @param jsonString JSON-Object to be converted into a java class
     *
     * @return A Configuration generated from the input-String
     */
    public static Configuration convertToJava(String jsonString) throws ConverterException {
        ObjectMapper mapper = new ObjectMapper();
        Configuration config;
        try {
            config = mapper.readValue(jsonString, Configuration.class);
        } catch (IOException e) {
            throw new ConverterException("Configuration could not be converted from json to java.");
        }
        return config;
    }

    /**
     * This method converts a Configuration into a JSON-Object
     *
     * @param config The Configuration to convert
     *
     * @return The generated JSON-Object
     */
    public static String serialize(Configuration config) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString;
        jsonString = mapper.writeValueAsString(config);
        LogManager.getInstance().writeToLog("Serialized Configuration", false);
        return jsonString;
    }

    /**
     * Gets and returns the value of id
     *
     * @return the value of id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the value of id to id
     *
     * @param id the input variable
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets and returns the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of name to name
     *
     * @param name the input variable
     */
    private void setName(String name) {
        this.name = name;
    }

    /**
     * Gets and returns the value of delimiter
     *
     * @return the value of delimiter
     */
    public String getDelimiter() {
        return delimiter;
    }

    /**
     * Sets the value of delimiter to delimiter
     *
     * @param delimiter the input variable
     */
    private void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * Gets and returns the value of numberOfHeaderlines
     *
     * @return the value of numberOfHeaderlines
     */
    public int getNumberOfHeaderlines() {
        return numberOfHeaderlines;
    }

    /**
     * Sets the value of numberOfHeaderlines to numberOfHeaderlines
     *
     * @param numberOfHeaderlines the input variable
     */
    private void setNumberOfHeaderlines(int numberOfHeaderlines) {
        this.numberOfHeaderlines = numberOfHeaderlines;
    }

    /**
     * Gets and returns the value of timezone
     *
     * @return the value of timezone
     */
    public String getTimezone() {
        return timezone;
    }

    /**
     * Sets the value of timezone to timezone
     *
     * @param timezone the input variable
     */
    private void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    /**
     * Gets and returns the value of dateTime
     *
     * @return the value of dateTime
     */
    public StringColumn[] getDateTime() {
        return dateTime;
    }

    /**
     * Sets the value of dateTime to dateTime
     *
     * @param dateTime the input variable
     */
    private void setDateTime(StringColumn[] dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Gets and returns the value of streamData
     *
     * @return the value of streamData
     */
    public StreamObservation[] getStreamData() {
        return streamData;
    }

    /**
     * Sets the value of streamData to streamData
     *
     * @param streamData the input variable
     */
    private void setStreamData(StreamObservation[] streamData) {
        this.streamData = streamData;
    }

    /**
     * Gets and returns the value of mapOfMagicNumbers
     *
     * @return the value of mapOfMagicNumbers
     */
    public MagicNumberMap[] getMapOfMagicNumbers() {
        return mapOfMagicNumbers;
    }

    /**
     * Sets the value of mapOfMagicNumbers to mapOfMagicNumbers
     *
     * @param mapOfMagicNumbers the input variable
     */
    private void setMapOfMagicNumbers(MagicNumberMap[] mapOfMagicNumbers) {
        this.mapOfMagicNumbers = mapOfMagicNumbers;
    }

    /**
     * Gets and returns the value of dataType
     *
     * @return the value of dataType
     */
    public DataType getDataType() {
        return dataType;
    }

    /**
     * Sets the value of dataType to dataType
     *
     * @param dataType the input variable
     */
    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }
    
    public URL getFrostURL() {
    	return frostURL;
    }
    
    public void setFrostURL(URL frostURL) {
    	this.frostURL = frostURL;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Configuration that = (Configuration) o;
        return id == that.id &&
                numberOfHeaderlines == that.numberOfHeaderlines &&
                Objects.equals(name, that.name) &&
                Objects.equals(delimiter, that.delimiter) &&
                Objects.equals(timezone, that.timezone) &&
                Arrays.equals(dateTime, that.dateTime) &&
                Arrays.equals(streamData, that.streamData) &&
                Arrays.equals(mapOfMagicNumbers, that.mapOfMagicNumbers) &&
                dataType == that.dataType &&
                frostURL.equals(that.frostURL);
    }
}

