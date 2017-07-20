package me.falconpenny.spclient;

import lombok.Getter;
import me.falconpenny.spclient.config.Config;
import me.falconpenny.spclient.modules.ModuleHandler;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLModDisabledEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Client.MODID, name = "SP-Client", acceptedMinecraftVersions = "[1.8,1.8.8,1.8.9,)", canBeDeactivated = true,
        guiFactory = "me.falconpenny.spclient.config.ConfigGuiFactory", version = Client.VERSION)
public class Client {
    public static final String MODID = "spclient";
    public static final String VERSION = "0.1";

    @Getter
    @Mod.Instance(Client.MODID)
    private static Client instance;

    @Mod.EventHandler
    public void preInitialization(FMLPreInitializationEvent event) {
        ModMetadata meta = event.getModMetadata();
        Config.getInstance().init(event);
        meta.description = "A \"ghost\" client for Forge.";
    }

    @Mod.EventHandler
    public void initialization(FMLInitializationEvent event) {
        ModuleHandler.getHandler().init();
    }

    @Mod.EventHandler
    public void disable(FMLModDisabledEvent event) {
        Config.getInstance().writeBinds();
        Config.getInstance().removeBinds();
        ModuleHandler.getHandler().exit();
        if (Config.getInstance().getConfig().hasChanged())
            Config.getInstance().getConfig().save();
        Config.getInstance().getKeybinds().clear();

        // TODO: Unload somehow.
    }

    public static String translate(String key) {
        String translated = StatCollector.canTranslate(key) ? StatCollector.translateToLocal(key) : StatCollector.translateToFallback(key);
        int length = translated.length();
        if (length == 0) {
            length = (translated = StatCollector.translateToFallback(key)).length();
        }
        int f, l;
        if ((f = (translated = translated.replace("\\n", "\n")).indexOf('{')) != -1 && (l = translated.indexOf('}')) != -1 && f < l) {
            int index = 0;
            StringBuilder iterKey = new StringBuilder();
            boolean started = false;
            while (index++ - 1 < length) {
                length = translated.length();
                char iteration = translated.charAt(index);
                if (started) {
                    if (iteration == '}') {
                        String repl;
                        translated = translated.replace('{' + iterKey.toString() + '}', repl = translate(iterKey.toString()));
                        iterKey = new StringBuilder();
                        started = false;
                        if ((f = translated.indexOf('{')) == -1 || (l = translated.indexOf('}')) == -1 || f > l) {
                            break;
                        }
                        int toSet = translated.indexOf(repl);
                        if (toSet != -1) index = --toSet;
                        continue;
                    }
                    iterKey.append(iteration);
                    continue;
                }
                if (iteration == '{') {
                    started = true;
                }
            }
        }
        return translated;
    }
}
