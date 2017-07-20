package me.falconpenny.spclient.config;

import me.falconpenny.spclient.Client;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.List;
import java.util.stream.Collectors;

public class ConfigGui extends GuiConfig {
    public ConfigGui(GuiScreen parent) {
        super(parent, ConfigGui.getElements(), Client.MODID, false, false, StatCollector.translateToLocal("spclient.config.title"));
    }

    private static List<IConfigElement> getElements() {
        return Config.getInstance().getConfig().getCategoryNames().stream()
                .map(name -> new ConfigElement(Config.getInstance().getConfig().getCategory(name).setLanguageKey("spclient.config.category." + name)))
                .collect(Collectors.toList());
    }
}
