package com.chillimport.server.utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;


public class SingleLineFormatter extends Formatter {

    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss(XXX)";

    /**
     * Formats a single line with a given record so that the log lines are more readable
     *
     * @param record the log line to use
     *
     * @return a String containing only the necessary information of the error
     */
    @Override
    public String format(final LogRecord record) {
        return String.format(
                "%1$s %2$-7s %3$s%n",
                new SimpleDateFormat(PATTERN).format(
                        new Date(record.getMillis())),
                record.getLevel().getName(), formatMessage(record));
    }
}