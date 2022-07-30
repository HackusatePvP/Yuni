package dev.hcr.yuni.configurations.types;

import dev.hcr.yuni.configurations.ConfigurationType;

import java.io.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;

public class PropertiesConfiguration extends ConfigurationType {
    private Properties properties;

    private static final Collection<PropertiesConfiguration> configurations = new HashSet<>();

    public PropertiesConfiguration(String fileName) {
        super(fileName);
        this.properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(getFile());
            try {
                properties.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        configurations.add(this);
    }


    public PropertiesConfiguration(String fileName, String directory) {
        super(fileName, directory);
        this.properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(getFile());
            try {
                properties.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        configurations.add(this);

    }

    @Override
    public String getString(String key) {
        return (String) properties.get(key);
    }

    @Override
    public Boolean getBoolean(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }

    @Override
    public Integer getInteger(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    @Override
    public Double getDouble(String key) {
        return Double.parseDouble(properties.getProperty(key));
    }

    @Override
    public Float getFloat(String key) {
        return (Float) properties.get(key);
    }

    @Override
    public Long getLong(String key) {
        return (Long) properties.get(key);
    }

    @Override
    public Object get(String key) {
        return properties.get(key);
    }

    @Override
    public void write(String key, Object object) {
        if (properties == null) {
            properties = new Properties();
        }
        try {
            OutputStream outputStream = new FileOutputStream(getFile());
            properties.setProperty(key, object.toString());
            properties.store(outputStream, null);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void preset(File file) {
        if (file.getName().toLowerCase().contains("database")) {
            write("main-loader", "mongo");
            write("redis-communication", false);
            write("redis-channel", "yuni");
        }
        if (file.getName().toLowerCase().contains("mongo")) {
            write("host", "127.0.0.1");
            write("port", 27017);
            write("database", "yuni");
            write("db-auth", false);
            write("db-auth-db", "admin");
            write("db-auth-user", "admin");
            write("db-auth-password", "password");
        }
    }

    public static PropertiesConfiguration getPropertiesConfiguration(String fileName) {
        return configurations.stream().filter(propertiesConfiguration -> propertiesConfiguration.getFileName().equalsIgnoreCase(fileName)).findAny().orElse(null);
    }
}
