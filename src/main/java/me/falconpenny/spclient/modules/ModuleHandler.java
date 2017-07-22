package me.falconpenny.spclient.modules;

import lombok.Getter;
import me.falconpenny.spclient.config.Config;
import me.falconpenny.spclient.modules.modules.ModuleFullbright;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class ModuleHandler {
    @Getter
    private static final ModuleHandler handler = new ModuleHandler();
    @Getter
    private final Set<IModule> modules = new LinkedHashSet<>(Arrays.asList(
            new ModuleFullbright()
    ));

    private ModuleHandler() {
    }

    public void init() {
        FMLCommonHandler.instance().bus().register(this);
        modules.forEach(FMLCommonHandler.instance().bus()::register);
    }

    public void exit() {
        FMLCommonHandler.instance().bus().unregister(this);
        modules.forEach(FMLCommonHandler.instance().bus()::unregister);
    }

    @SubscribeEvent
    public void keyToggle(InputEvent.KeyInputEvent event) {
        if (!Keyboard.getEventKeyState() || Keyboard.getEventKey() == 0 || Keyboard.getEventKey() == Keyboard.KEY_BACK) {
            return;
        }
        modules.stream().filter(module -> Config.getInstance().getConfig().getCategory(Config.getCategory_keybinds()).get(module.name()).getInt(0) == Keyboard.getEventKey()).findFirst().ifPresent(module -> {
            module.toggle();
            module.stateMessage();
        });
    }
}
