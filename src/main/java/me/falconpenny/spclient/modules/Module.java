package me.falconpenny.spclient.modules;

import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Module implements IModule {
    private final Map<Class<? extends Event>, EventHandler<? extends Event>> handlers = new LinkedHashMap<>();
    private boolean state = false;

    @Nullable
    @Override
    public String description() {
        return null;
    }

    @Nullable
    @Override
    public String[] aliases() {
        return new String[0];
    }

    @Override
    public int key() {
        return 0;
    }

    @Override
    public Map<Class<? extends Event>, EventHandler<? extends Event>> eventhandlers() {
        return handlers;
    }

    @Override
    public void enable() {
    }

    @Override
    public void disable() {
    }

    @Override
    public void toggle() {
        state = !state;
        if (state) enable();
        else disable();
    }

    @Override
    public boolean state() {
        //noinspection UnnecessaryUnboxing -- Done so that you have to use #toggle to change it.
        return Boolean.valueOf(state).booleanValue();
    }
}
