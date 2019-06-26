package com.chillimport.server.errors;

import com.chillimport.server.FileManager;
import com.chillimport.server.utility.SingleLineFormatter;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.logging.*;


/**
 * The LogManager class saves all messages produced by other classes. It also creates the DateTime String used for identifying the current instance.
 */
public class LogManager {

    private static LogManager logManager;
    private Logger logger;
    private FileHandler txtFile;
    private SingleLineFormatter txtFormatter;
    private String fileName;
    private String logPath;
    private String date;

    /**
     * Private Constructor so that only one Instance can be created.
     *
     * @throws IOException Thrown if an error occurs while opening files (setup->new Filehandler() )
     */
    private LogManager() {
        logger = Logger.getLogger("LogManager");
        txtFormatter = new SingleLineFormatter();
        fileName = ZonedDateTime.now().toString();
        date = fileName.substring(0, 19).replaceAll(":", "-");
        fileName = date + " Logging.txt";
        logPath = FileManager.getLogPath().toString();
        logPath += "/" + fileName;
        initLog();
    }

    /**
     * Returns the LogManager Instance. Creates it first if none was created yet.
     *
     * @return The only LogManager Instance
     *
     * @throws IOException Thrown if an error occurs while opening files (Constructor->Setup->new FileHandler() )
     */
    public static LogManager getInstance() {
        if (logManager == null) {
            logManager = new LogManager();
        }
        return logManager;
    }

    public String getDate() {
        return date;
    }

    /**
     * Setup the LogManager. Logs all message levels , will write to Logging.txt (txtFile) . Formatted by SimpleFormatter
     *
     * @throws IOException Thrown if an error occurs while opening files
     */
    private void initLog() {
        try {
            txtFile = new FileHandler(logPath);
        } catch (IOException e) {
            System.out.println("Could not initialize LogManager");
            e.printStackTrace();
        }
        logger.setLevel(Level.ALL);
        txtFile.setFormatter(txtFormatter);
        logger.addHandler(txtFile);
    }

    /**
     * Writes to the log, message will be written as "severe" when severe is true, as "info" else.
     *
     * @param message The message to be written
     * @param severe  Whether the message shouldnt be written as "severe" or not
     */
    public void writeToLog(String message, boolean severe) {
        if (severe) {
            logger.severe(message);
        }
        else {
            logger.info(message);
        }
    }

    /**
     * Returns the log's full Name
     *
     * @return FileName as String
     */
    public String getLogPath() {
        return logPath;
    }

    /**
     * Clean up at the end of Upload
     */
    public void clear() {
        txtFile.close();
        logger.removeHandler(txtFile);
        this.logManager = null;
    }
}

