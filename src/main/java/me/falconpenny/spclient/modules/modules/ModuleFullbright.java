package me.falconpenny.spclient.modules.modules;

import me.falconpenny.spclient.modules.Module;
import me.falconpenny.spclient.modules.ModuleData;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModuleData(name = "Fullbright", aliases = {"fb"}, description = "Makes the entire world brighter.")
public class ModuleFullbright extends Module {
    private float gamma = 0F;

    @Override
    public void enable() {
        gamma = Minecraft.getMinecraft().gameSettings.gammaSetting;
        Minecraft.getMinecraft().gameSettings.gammaSetting = 1E6F;
    }

    @Override
    public void disable() {
        Minecraft.getMinecraft().gameSettings.gammaSetting = gamma;
    }

    @SubscribeEvent
    public void updateEvent(LivingEvent.LivingUpdateEvent event) {
        if (!state()) {
            gamma = Minecraft.getMinecraft().gameSettings.gammaSetting;
            return;
        }
        if (Minecraft.getMinecraft().gameSettings.gammaSetting != 1E6F) {
            setState(false);
            stateMessage();
        }
    }
}
