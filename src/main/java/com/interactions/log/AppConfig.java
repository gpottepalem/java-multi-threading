package com.interactions.log;

import java.io.IOException;
import java.util.Properties;

/**
 * A simple singleton class.
 * Holds parsed application configuration properties.
 *
 * @author gpottepalem
 * Created on Sep 07, 2019
 */
public class AppConfig {
    private static AppConfig INSTANCE; // singleton instance
    private static final String APP_CONFIG__FILENAME = "application.properties";

    public static final String PROPERTY_KEY_N_CID_CATEGORY_A = "A";
    public static final String PROPERTY_KEY_N_CID_CATEGORY_B = "B";
    public static final String PROPERTY_KEY_COMMIT_LOGFILE_NAME = "name";

    // read only
    private int numberOfCidAWriters;
    private int numberOfCidBWriters;
    private String commitLogFileName;

    /**
     * Constructs and instance of {@link AppConfig}
     */
    private AppConfig() {
        // empty
    }

    /**
     * Creates, initializes and returns the only instance of this object.
     * @return singleton instance of {@link AppConfig}
     * @throws IOException if an exception occurs while initializing the instance
     */
    public static AppConfig getInstance() throws IOException {
        if(INSTANCE == null) {
            INSTANCE = new AppConfig();
            INSTANCE.load();
        }
        return INSTANCE;
    }

    /**
     * Loads application properties from the configuration file and populates the object.
     * @throws IOException if an exception occurs reading properties from the config file
     */
    public static void load() throws IOException {
        Properties properties = new Properties();
        AppConfig appConfig = AppConfig.getInstance();
        properties.load(CommitLogApplication.class.getClassLoader().getResourceAsStream(APP_CONFIG__FILENAME));
        appConfig.numberOfCidAWriters = Integer.parseInt(properties.getProperty(PROPERTY_KEY_N_CID_CATEGORY_A));
        appConfig.numberOfCidBWriters = Integer.parseInt(properties.getProperty(PROPERTY_KEY_N_CID_CATEGORY_B));
        appConfig.commitLogFileName = properties.getProperty(PROPERTY_KEY_COMMIT_LOGFILE_NAME);
    }

    // IDE spit out getters
    public int getNumberOfCidAWriters() {
        return numberOfCidAWriters;
    }

    public int getNumberOfCidBWriters() {
        return numberOfCidBWriters;
    }

    public String getCommitLogFileName() {
        return commitLogFileName;
    }
}
