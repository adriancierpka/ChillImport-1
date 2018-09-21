package com.chillimport.server.utility;

import com.chillimport.server.config.Configuration;


/**
 * Represents an Upload
 */
public class Upload {

    private String fileName;
    private Configuration cfg;

    /**
     * Creates a new Upload with a filename and the Configuration to use
     *
     * @param fileName the filename, identifies the uploaded file
     * @param cfg      the complete configuration to use to process the file
     */
    public Upload(String fileName, Configuration cfg) {
        this.fileName = fileName;
        this.cfg = cfg;
    }


    /**
     * Gets and returns the value of fileName
     *
     * @return the value of fileName
     */
    public String getFileName() {
        return fileName;
    }


    /**
     * Gets and returns the value of cfg
     *
     * @return the value of cfg
     */
    public Configuration getCfg() {
        return cfg;
    }
}
