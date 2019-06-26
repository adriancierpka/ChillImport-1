package com.chillimport.server.config;

import java.util.Arrays;


/**
 * This class represents a Datastream with its observations
 */
public class StreamObservation {

    private int dsID;
    private boolean isMultiStream;
    private int[] observations;

    /**
     * empty Constructor
     */
    public StreamObservation() {
    }

    /**
     * a constructor
     *
     * @param dsID
     * @param isMultiStream
     * @param observations
     */
    public StreamObservation(int dsID, boolean isMultiStream, int[] observations) {
        setDsID(dsID);
        setMultiStream(isMultiStream);
        setObservations(observations);
    }

    /**
     * Getter for the datasream-id
     *
     * @return the id
     */
    public int getDsID() {
        return dsID;
    }

    /**
     * Setter for the datastream-id
     *
     * @param dsID the id
     */
    private void setDsID(int dsID) {
        this.dsID = dsID;
    }

    /**
     * Getter for the columns of the observations
     *
     * @return
     */
    public int[] getObservations() {
        return observations;
    }

    /**
     * Getter for the columns of the observations
     *
     * @param observations
     */
    private void setObservations(int[] observations) {
        this.observations = observations;
    }

    /**
     * Getter for isMultiStream
     *
     * @return true if the datastream is a multi-ds
     */
    public boolean isMultiStream() {
        return isMultiStream;
    }

    /**
     * Setter for isMultiStream
     *
     * @param multiStream
     */
    private void setMultiStream(boolean multiStream) {
        isMultiStream = multiStream;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StreamObservation that = (StreamObservation) o;
        return dsID == that.dsID &&
                isMultiStream == that.isMultiStream &&
                Arrays.equals(observations, that.observations);
    }
}
