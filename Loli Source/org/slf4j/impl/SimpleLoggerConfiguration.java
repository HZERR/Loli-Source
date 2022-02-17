/*
 * Decompiled with CFR 0.150.
 */
package org.slf4j.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import org.slf4j.helpers.Util;
import org.slf4j.impl.OutputChoice;

public class SimpleLoggerConfiguration {
    private static final String CONFIGURATION_FILE = "simplelogger.properties";
    static int DEFAULT_LOG_LEVEL_DEFAULT = 20;
    int defaultLogLevel = DEFAULT_LOG_LEVEL_DEFAULT;
    private static final boolean SHOW_DATE_TIME_DEFAULT = false;
    boolean showDateTime = false;
    private static final String DATE_TIME_FORMAT_STR_DEFAULT;
    private static String dateTimeFormatStr;
    DateFormat dateFormatter = null;
    private static final boolean SHOW_THREAD_NAME_DEFAULT = true;
    boolean showThreadName = true;
    static final boolean SHOW_LOG_NAME_DEFAULT = true;
    boolean showLogName = true;
    private static final boolean SHOW_SHORT_LOG_NAME_DEFAULT = false;
    boolean showShortLogName = false;
    private static final boolean LEVEL_IN_BRACKETS_DEFAULT = false;
    boolean levelInBrackets = false;
    private static String LOG_FILE_DEFAULT;
    private String logFile = LOG_FILE_DEFAULT;
    OutputChoice outputChoice = null;
    private static final boolean CACHE_OUTPUT_STREAM_DEFAULT = false;
    private boolean cacheOutputStream = false;
    private static final String WARN_LEVELS_STRING_DEFAULT = "WARN";
    String warnLevelString = "WARN";
    private final Properties properties = new Properties();

    void init() {
        this.loadProperties();
        String defaultLogLevelString = this.getStringProperty("org.slf4j.simpleLogger.defaultLogLevel", null);
        if (defaultLogLevelString != null) {
            this.defaultLogLevel = SimpleLoggerConfiguration.stringToLevel(defaultLogLevelString);
        }
        this.showLogName = this.getBooleanProperty("org.slf4j.simpleLogger.showLogName", true);
        this.showShortLogName = this.getBooleanProperty("org.slf4j.simpleLogger.showShortLogName", false);
        this.showDateTime = this.getBooleanProperty("org.slf4j.simpleLogger.showDateTime", false);
        this.showThreadName = this.getBooleanProperty("org.slf4j.simpleLogger.showThreadName", true);
        dateTimeFormatStr = this.getStringProperty("org.slf4j.simpleLogger.dateTimeFormat", DATE_TIME_FORMAT_STR_DEFAULT);
        this.levelInBrackets = this.getBooleanProperty("org.slf4j.simpleLogger.levelInBrackets", false);
        this.warnLevelString = this.getStringProperty("org.slf4j.simpleLogger.warnLevelString", WARN_LEVELS_STRING_DEFAULT);
        this.logFile = this.getStringProperty("org.slf4j.simpleLogger.logFile", this.logFile);
        this.cacheOutputStream = this.getBooleanProperty("org.slf4j.simpleLogger.cacheOutputStream", false);
        this.outputChoice = SimpleLoggerConfiguration.computeOutputChoice(this.logFile, this.cacheOutputStream);
        if (dateTimeFormatStr != null) {
            try {
                this.dateFormatter = new SimpleDateFormat(dateTimeFormatStr);
            }
            catch (IllegalArgumentException e2) {
                Util.report("Bad date format in simplelogger.properties; will output relative time", e2);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private void loadProperties() {
        InputStream in = AccessController.doPrivileged(new PrivilegedAction<InputStream>(){

            @Override
            public InputStream run() {
                ClassLoader threadCL = Thread.currentThread().getContextClassLoader();
                if (threadCL != null) {
                    return threadCL.getResourceAsStream(SimpleLoggerConfiguration.CONFIGURATION_FILE);
                }
                return ClassLoader.getSystemResourceAsStream(SimpleLoggerConfiguration.CONFIGURATION_FILE);
            }
        });
        if (null == in) return;
        this.properties.load(in);
        try {
            in.close();
            return;
        }
        catch (IOException e2) {
            return;
        }
        catch (IOException e3) {
            try {
                in.close();
                return;
            }
            catch (IOException iOException) {
                return;
            }
            catch (Throwable throwable) {
                try {
                    in.close();
                    throw throwable;
                }
                catch (IOException e4) {
                    // empty catch block
                }
                throw throwable;
            }
        }
    }

    String getStringProperty(String name, String defaultValue) {
        String prop = this.getStringProperty(name);
        return prop == null ? defaultValue : prop;
    }

    boolean getBooleanProperty(String name, boolean defaultValue) {
        String prop = this.getStringProperty(name);
        return prop == null ? defaultValue : "true".equalsIgnoreCase(prop);
    }

    String getStringProperty(String name) {
        String prop = null;
        try {
            prop = System.getProperty(name);
        }
        catch (SecurityException securityException) {
            // empty catch block
        }
        return prop == null ? this.properties.getProperty(name) : prop;
    }

    static int stringToLevel(String levelStr) {
        if ("trace".equalsIgnoreCase(levelStr)) {
            return 0;
        }
        if ("debug".equalsIgnoreCase(levelStr)) {
            return 10;
        }
        if ("info".equalsIgnoreCase(levelStr)) {
            return 20;
        }
        if ("warn".equalsIgnoreCase(levelStr)) {
            return 30;
        }
        if ("error".equalsIgnoreCase(levelStr)) {
            return 40;
        }
        if ("off".equalsIgnoreCase(levelStr)) {
            return 50;
        }
        return 20;
    }

    private static OutputChoice computeOutputChoice(String logFile, boolean cacheOutputStream) {
        if ("System.err".equalsIgnoreCase(logFile)) {
            if (cacheOutputStream) {
                return new OutputChoice(OutputChoice.OutputChoiceType.CACHED_SYS_ERR);
            }
            return new OutputChoice(OutputChoice.OutputChoiceType.SYS_ERR);
        }
        if ("System.out".equalsIgnoreCase(logFile)) {
            if (cacheOutputStream) {
                return new OutputChoice(OutputChoice.OutputChoiceType.CACHED_SYS_OUT);
            }
            return new OutputChoice(OutputChoice.OutputChoiceType.SYS_OUT);
        }
        try {
            FileOutputStream fos = new FileOutputStream(logFile);
            PrintStream printStream = new PrintStream(fos);
            return new OutputChoice(printStream);
        }
        catch (FileNotFoundException e2) {
            Util.report("Could not open [" + logFile + "]. Defaulting to System.err", e2);
            return new OutputChoice(OutputChoice.OutputChoiceType.SYS_ERR);
        }
    }

    static {
        dateTimeFormatStr = DATE_TIME_FORMAT_STR_DEFAULT = null;
        LOG_FILE_DEFAULT = "System.err";
    }
}

