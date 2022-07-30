package dev.hcr.yuni.configurations;

import dev.hcr.yuni.Yuni;

import java.io.File;
import java.io.IOException;

public abstract class ConfigurationType {
    private final File file;
    private final String fileName;

    public ConfigurationType(String fileName) {
        this.fileName = fileName;
        this.file = new File(Yuni.getPlugin().getDataFolder(), fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            file.createNewFile();
            preset(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ConfigurationType(String fileName, String directory) {
        this.fileName = fileName;
        File fileDirectory = new File(Yuni.getPlugin().getDataFolder(), directory);
        fileDirectory.mkdirs();
        this.file = new File(fileDirectory, fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            file.createNewFile();
            preset(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFileName() {
        return fileName;
    }

    public abstract String getString(String key);

    public abstract Boolean getBoolean(String key);

    public abstract Integer getInteger(String key);

    public abstract Double getDouble(String key);

    public abstract Float getFloat(String key);

    public abstract Long getLong(String key);

    public abstract Object get(String key);

    public abstract void write(String key, Object object);

    public abstract void preset(File file);

    public File getFile() {
        return file;
    }

}
