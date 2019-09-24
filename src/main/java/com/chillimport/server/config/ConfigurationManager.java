package com.chillimport.server.config;


import com.chillimport.server.FileManager;
import com.chillimport.server.errors.ErrorHandler;
import com.chillimport.server.errors.LogManager;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


/**
 * This class offers static methods to manage configurations
 */
public class ConfigurationManager {

    /**
     * Loads the configuration with the given id
     *
     * @param id of the Configuration
     *
     * @return a Configuration
     *
     * @throws IOException if convertion to java or reading from file fails
     */
    public static Configuration loadConfig(int id) throws IOException {
        String tmpConfigPath = FileManager.getConfigPath().toString();

        File file = new File(tmpConfigPath + "/" + Integer.toString(id) + ".json");
        Configuration config;


        List<String> configAsJSON;
        try {
            configAsJSON = Files.readAllLines(Paths.get(file.getPath()));
        } catch (IOException e) {
            throw new IOException("File of configuration could not be read.");
        }

        //configAsJSON can have multiple Strings for 1 Configuration
        // -> concat needed
        config = Configuration.convertToJava(concat(configAsJSON));

        return config;
    }

    //concatenates strings of a list
    private static String concat(List<String> list) {
        StringBuilder result = new StringBuilder();
        for (String aString : list) {
            result.append(aString);
        }

        return result.toString();
    }


    /**
     * Saves the given configuration
     *
     * @param config the configuration
     */
    public static void saveConfig(Configuration config) throws IOException {
        String tmpConfigPath = FileManager.getConfigPath().toString();


        //create new file in server/configurations/
        File file = new File(tmpConfigPath + "/" + config.getId() + ".json");
        if (!(file.createNewFile())) {
            throw new IOException("File with the Id of this Configuration already exists. Did not overwrite.");
        }
        try {
            //open FileWriter and save config in file
            FileWriter writer = new FileWriter(file);
            writer.write(Configuration.serialize(config));
            writer.flush();
            writer.close();
        } catch (JsonProcessingException e) {
            LogManager.getInstance().writeToLog("Failed to serialize the configuration", true);
            ErrorHandler.getInstance().addRows(-1, e);
            throw new IOException("Failed to serialize the configuration.");
        } catch (IOException e) {
            LogManager.getInstance().writeToLog("Failed to write config to file", true);
            ErrorHandler.getInstance().addRows(-1, e);
            throw new IOException("Failed to write config to file.");
        }
    }

    /**
     * Returns a list of all Configurations
     *
     * @return a list
     */
    public static List<Configuration> listAll() {
        String tmpConfigPath = FileManager.getConfigPath().toString();

        String[] entries = new File(tmpConfigPath).list();
        List<Configuration> allConfigs = new ArrayList<>();
        if (entries == null) {
            return allConfigs;
        }
        for (String entry : entries) {
            if (entry.matches(".*\\.json")) {
                entry = entry.replaceAll("\\.json", "");
                try {
                    Configuration config = ConfigurationManager.loadConfig(Integer.parseInt(entry));
                    if (config != null) {
                        allConfigs.add(config);
                    }
                } catch (IOException e) {
                    System.out.println("Config could not be read: " + entry);
                    LogManager.getInstance().writeToLog("Config could not be read: " + entry, true);
                    ErrorHandler.getInstance().addRows(-1, e);
                }

            }
        }

        return allConfigs;
    }

}
