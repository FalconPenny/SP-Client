package me.falconpenny.spclient.modules;

import lombok.NonNull;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nullable;
import java.util.Map;

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

    @NonNull
    Map<Class<? extends Event>, EventHandler<? extends Event>> eventhandlers();

    default <T extends Event> void addHandler(Class<T> type, EventHandler<T> implementation) {
        eventhandlers().put(type, implementation);
    }

    boolean state();

    void enable();

    void disable();

    void toggle();
}
