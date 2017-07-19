package me.falconpenny.spclient;

import me.falconpenny.spclient.configuration.Configuration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigGui {
    public static class GuiFactory implements IModGuiFactory {
        @Override
        public void initialize(Minecraft minecraft) {

        }

        @Override
        public Class<? extends GuiScreen> mainConfigGuiClass() {
            return Gui.class;
        }

        @Override
        public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
            return null;
        }

        @Override
        public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement runtimeOptionCategoryElement) {
            return null;
        }
    }

    public static class Gui extends GuiConfig {
        public Gui(GuiScreen parent) {
            super(parent, Stream.of(Configuration.getInstance().getKeybinds(), Configuration.getInstance().getMessages(), Configuration.getInstance().getSwitches()).map(ConfigElement::new).collect(Collectors.toList()), "sp-client", false, false, "SP Client Configuration");
        }
    }

    private ConfigGui() {}
}
