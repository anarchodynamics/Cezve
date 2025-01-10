package com.anarchodynamics.cezvegateway;

import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LogAgent {
    private static boolean isConfigured = false;

    static {
        configureLogger();
    }

    private static void configureLogger() {
        if (isConfigured) return;

        try {
            // Load the logging.properties file from the classpath
            LogManager.getLogManager().readConfiguration(
                LogAgent.class.getResourceAsStream("/logging.properties")
            );
            isConfigured = true;
        } catch (IOException | NullPointerException e) {
            System.err.println("Failed to load logging.properties configuration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Logger getLogger(Class<?> localclass) {

        return Logger.getLogger(localclass.getName());
    }
}

