package me.falconpenny.spclient.modules;

import lombok.Data;
import lombok.NonNull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.eventhandler.Event;

@Data
public class ModuleContext<T extends Event> {
    @NonNull
    private final Minecraft client;
    @NonNull
    private final EntityPlayerSP player;
    @NonNull
    private final T event;

    public void sendMessage(@NonNull ChatComponentText... message) {
        if (message.length == 0) return;
        for (ChatComponentText m : message) {
            player.addChatMessage(m);
        }
    }
}
