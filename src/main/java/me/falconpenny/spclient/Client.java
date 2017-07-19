package me.falconpenny.spclient;

import lombok.Getter;
import me.falconpenny.spclient.configuration.Configuration;
import me.falconpenny.spclient.modules.ModuleHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLModDisabledEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Client.MODID, name = "SP-Client", acceptedMinecraftVersions = "[1.8,1.8.8,1.8.9,)", canBeDeactivated = true,
        guiFactory = "me.falconpenny.spclient.ConfigGui$GuiFactory")
public class Client {
    public static final String MODID = "spclient";

    @Getter
    private static Client instance;

    {
        instance = this;
    }

    @Mod.EventHandler
    public void preInitialization(FMLPreInitializationEvent event) {
        ModMetadata meta = event.getModMetadata();
        Configuration.getInstance().init(event);
        meta.description = "A \"ghost\" client for Forge.";
    }

    @Mod.EventHandler
    public void initialization(FMLInitializationEvent event) {
        ModuleHandler.getHandler().init();
        Configuration.getInstance().sync();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ModuleHandler.getHandler().exit();
            if (Configuration.getInstance().getConfig().hasChanged())
                Configuration.getInstance().getConfig().save();
        }));
    }

    @Mod.EventHandler
    public void disable(FMLModDisabledEvent event) {
        // TODO: Unload somehow.
    }
}
