package me.falconpenny.spclient.configurations;

import lombok.Getter;
import me.falconpenny.spclient.Logging;
import net.minecraft.client.Minecraft;

import java.io.File;

public class RootConfiguration {
    @Getter
    private static transient final RootConfiguration instance = new RootConfiguration();
    @Getter
    private transient final File dataFolder = new File(Minecraft.getMinecraft().mcDataDir, "SP-Client");
    @Getter
    private Configuration keybinds = new Configuration(new File(dataFolder, "Keybinds.conf"));
    @Getter
    private Configuration messages = new Configuration(new File(dataFolder, "Messages.conf"));

    public void init() {
        if (!dataFolder.exists() || !dataFolder.isDirectory()) {
            Logging.info("DataFolder doesn't exist. Trying to make: " + (dataFolder.mkdirs() ? "Success!" : "Failure!"));
        }
        messages.init();
        keybinds.init();
    }

    public void exit() {
        messages.init();
        keybinds.exit();
    }

    private RootConfiguration() {
    }
}
