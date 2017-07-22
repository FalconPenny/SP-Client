package me.falconpenny.spclient.modules.modules;

import lombok.NonNull;
import me.falconpenny.spclient.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.living.LivingEvent;

public class ModuleFullbright extends Module {
    private float gamma = 0F;

    @NonNull
    @Override
    public String name() {
        return "Fullbright";
    }

    @NonNull
    @Override
    public String[] aliases() {
        return new String[]{"fb"};
    }

    @Override
    public void enable() {
        gamma = Minecraft.getMinecraft().gameSettings.gammaSetting;
        Minecraft.getMinecraft().gameSettings.gammaSetting = 1E6F;
    }

    @Override
    public void disable() {
        Minecraft.getMinecraft().gameSettings.gammaSetting = gamma;
    }

    {
        addHandler(LivingEvent.LivingUpdateEvent.class, event -> {
            if (!state()) {
                gamma = Minecraft.getMinecraft().gameSettings.gammaSetting;
                return;
            }
            if (Minecraft.getMinecraft().gameSettings.gammaSetting != 1E6F) {
                setState(false);
                stateMessage();
            }
        });
    }
}
