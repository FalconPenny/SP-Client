package me.falconpenny.spclient.modules;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;

public abstract class Module implements IModule {
    @Nullable
    @Getter
    private final ModuleData moduleDataAnnotation = getClass().getAnnotation(ModuleData.class);
    @Setter
    private boolean state = false;

    @Nullable
    @Override
    public String name() {
        if (moduleDataAnnotation != null) {
            return moduleDataAnnotation.name();
        }
        return null;
    }

    @Nullable
    @Override
    public String description() {
        if (moduleDataAnnotation != null) {
            return moduleDataAnnotation.description();
        }
        return null;
    }

    @Nullable
    @Override
    public String[] aliases() {
        if (moduleDataAnnotation != null) {
            return moduleDataAnnotation.aliases();
        }
        return new String[0];
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
