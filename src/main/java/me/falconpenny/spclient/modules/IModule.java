package me.falconpenny.spclient.modules;

import lombok.NonNull;
import me.falconpenny.spclient.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import javax.annotation.Nullable;

public interface IModule {
    @Nullable
    String description();

    @Nullable
    String name();

    @NonNull
    default String localizedName() {
        String name = name();
        if (name == null) {
            return "Undefined Module Name";
        }
        String key = "spclient.modules.localization." + name.toLowerCase();
        return StatCollector.canTranslate(key) ? StatCollector.translateToLocal(key) : name;
    }

    @Nullable
    String[] aliases();

    boolean state();

    void setState(boolean state);

    void enable();

    void disable();

    void toggle();

    default void stateMessage() {
        if (Config.getInstance().isSwitchTogglenotify()) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(
                    Config.getInstance().getMessageTogglemessage()
                            .replace("$MODULE", localizedName())
                            .replace("$STATE", state() ? "enabled" : "disabled")
            ).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
        }
    }
}
