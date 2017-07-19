package me.falconpenny.spclient.configurations;

import lombok.NonNull;
import net.minecraftforge.common.config.Configuration;

public interface IConfiguration extends FileStore {
    @NonNull
    Configuration config();

    void init();

    void exit();
}
