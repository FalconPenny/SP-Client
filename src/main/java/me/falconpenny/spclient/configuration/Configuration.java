package me.falconpenny.spclient.configuration;

import lombok.Getter;
import lombok.NonNull;
import me.falconpenny.spclient.Client;
import me.falconpenny.spclient.modules.ModuleHandler;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.io.File;

public class Configuration {
    private Configuration() {
    }

    @Getter private final static Configuration instance = new Configuration();
    @Getter private File configFile;
    @Getter private net.minecraftforge.common.config.Configuration config;
    @Getter private ConfigCategory keybinds;
    @Getter private ConfigCategory messages;
    @Getter private ConfigCategory switches;

    public void init(@NonNull FMLPreInitializationEvent event) {
        configFile = event.getSuggestedConfigurationFile();
        config = new net.minecraftforge.common.config.Configuration(configFile, "0.1", false);
        if (configFile.exists()) { // If not, it'll just apply defaults anyways, but it's nice to actually have custom loaded.
            config.load();
        }
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void update(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equals(Client.MODID)) {
            sync();
        }
    }

    public void sync() {
        {
            keybinds = config.getCategory("Keybinds");
            ModuleHandler.getHandler().getModules().forEach(module -> {
                Property property = keybinds.get(module.name());
                property.comment = "Key to toggle " + module.name() + ". " + Keyboard.KEY_BACK + " & 0 = disabled.";
                property.setDefaultValue(0);
            });
        }
        Property prop;
        {
            messages = config.getCategory("Messages");

            prop = messages.get("toggle-message");
            prop.comment = "What the toggle message should be.";
            prop.setDefaultValue(" \u2666 The module $MODULE has been toggled to the state of $STATE.");
        }
        {
            switches = config.getCategory("Switches");

            prop = switches.get("toggle-notify");
            prop.comment = "Whether or not to automatically send toggle messages to the user.";
            prop.setDefaultValue(false);
        }
        if (config.hasChanged()) {
            config.save();
        }
    }
}
