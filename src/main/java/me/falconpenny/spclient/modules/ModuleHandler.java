package me.falconpenny.spclient.modules;

import lombok.Getter;
import me.falconpenny.spclient.config.Config;
import me.falconpenny.spclient.modules.modules.ModuleAutoclicker;
import me.falconpenny.spclient.modules.modules.ModuleFullbright;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class ModuleHandler {
    @Getter
    private static final ModuleHandler handler = new ModuleHandler();
    @Getter
    private final Set<IModule> modules = new LinkedHashSet<>(Arrays.asList(
            new ModuleFullbright(),
            new ModuleAutoclicker()
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
        final KeyBinding[] binding = {null};
        final boolean[] effectivelyFinalReturn = {false};
        {
            modules.stream().map(module -> Config.getInstance().getKeybinds().get(module)).filter(Objects::nonNull).findFirst().ifPresent((KeyBinding it) -> {
                binding[0] = it;
                effectivelyFinalReturn[0] = true;
            });
            if (effectivelyFinalReturn[0]) {
                return;
            }
        }
        modules.stream().filter(module -> Keyboard.getEventKey() == Keyboard.getEventKey()).findFirst().ifPresent(module -> {
            module.toggle();
            module.stateMessage();
        });
    }
}
