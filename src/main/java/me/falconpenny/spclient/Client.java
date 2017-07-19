package me.falconpenny.spclient;

import lombok.Getter;
import me.falconpenny.spclient.configurations.RootConfiguration;
import me.falconpenny.spclient.modules.ModuleHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "spclient", name = "SP-Client", acceptedMinecraftVersions = "[1.8,1.8.8,1.8.9,)", canBeDeactivated = true)
public class Client {
    @Getter
    private static Client instance;

    {
        instance = this;
    }

    @Mod.EventHandler
    public void preInitialization(FMLPreInitializationEvent event) {
        ModMetadata meta = event.getModMetadata();
        meta.description = "A \"ghost\" client for Forge.";
    }

    @Mod.EventHandler
    public void initialization(FMLInitializationEvent event) {
        RootConfiguration.getInstance().init();
        ModuleHandler.getHandler().init();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ModuleHandler.getHandler().exit();
            RootConfiguration.getInstance().exit();
        }));
    }
}
