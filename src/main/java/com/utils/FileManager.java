package com.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Properties;

public class FileManager {
    private static FileManager instance;
    private final Properties properties = new Properties();

    public static FileManager getInstance() {
        if (instance == null) {
            instance = new FileManager();
        }
        return instance;
    }

    /**
     * Get value from .properties file
     * @param key The key to get value
     * @return The value of .properties file in String format
     */
    public String getProperties(String key) {
        loadFile("config.properties");
        return properties.getProperty(key);
    }

    /**
     * Load properties from custom file to Properties object
     * @param src File, contains properties to load
     */
    private void loadFile(String src) {
        try (FileInputStream fileInputStream = new FileInputStream(getResourcePath(src))) {
            properties.load(fileInputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Get path in String format to resources files or directories
     * @param file The file or directory to find path to
     * @return path to file or directory
     */
    private String getResourcePath(String file) {
        String path = null;
        try {
            path = new File(Objects.requireNonNull(getClass().getClassLoader().getResource(file)).toURI()).getPath();
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
        return path;
    }
}
