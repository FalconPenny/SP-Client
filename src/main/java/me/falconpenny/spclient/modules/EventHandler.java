package me.falconpenny.spclient.modules;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@FunctionalInterface
public interface EventHandler<T extends Event> {
    @SubscribeEvent
    void handle(T event);
}
