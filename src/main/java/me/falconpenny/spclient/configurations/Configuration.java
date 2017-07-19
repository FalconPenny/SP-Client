package me.falconpenny.spclient.configurations;

import java.io.File;

public class Configuration implements IConfiguration {
    private final net.minecraftforge.common.config.Configuration configuration;
    private final File file;

    public Configuration(File file) {
        this.file = file;
        configuration = new net.minecraftforge.common.config.Configuration(file);
    }

    @Override
    public net.minecraftforge.common.config.Configuration config() {
        return configuration;
    }

    @Override
    public void init() {
        if (file.exists()) {
            configuration.load();
        }
    }

    @Override
    public void exit() {
        configuration.save();
    }

    @Override
    public File file() {
        return file;
    }
}
