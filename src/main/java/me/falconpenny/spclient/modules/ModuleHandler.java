package me.falconpenny.spclient.modules;

import lombok.Getter;
import me.falconpenny.spclient.config.Config;
import me.falconpenny.spclient.utils.Conditionals;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ModuleHandler {
    @Getter
    private static final ModuleHandler handler = new ModuleHandler();
    @Getter
    private final Set<Module> modules = new LinkedHashSet<>();

    private ModuleHandler() {
    }

    public void init() {
        FMLCommonHandler.instance().bus().register(this);
        modules.stream().map(IModule::eventhandlers).forEach(handlers -> handlers.entrySet().stream().map(Map.Entry::getValue).forEach(FMLCommonHandler.instance().bus()::register));
    }

    public void exit() {
        FMLCommonHandler.instance().bus().unregister(this);
        modules.stream().map(IModule::eventhandlers).forEach(handlers -> handlers.entrySet().stream().map(Map.Entry::getValue).forEach(FMLCommonHandler.instance().bus()::unregister));
    }

    @SubscribeEvent
    public void keyToggle(InputEvent.KeyInputEvent event) {
        if (!Keyboard.getEventKeyState() || Keyboard.getEventKey() == 0 || Keyboard.getEventKey() == Keyboard.KEY_BACK) {
            return;
        }
        modules.stream().filter(module -> Config.getInstance().getConfig().getCategory(Config.getCategory_keybinds()).get(module.name()).getInt(0) == Keyboard.getEventKey()).findFirst().ifPresent(module -> {
            module.toggle();
            if (Config.getInstance().isSwitchTogglenotify()) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(
                        Config.getInstance().getMessageTogglemessage()
                                .replace("$MODULE", Conditionals.withDefault(module.name(), "Undefined name."))
                                .replace("$STATE", module.state() ? "enabled" : "disabled")
                ).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
            }
        });
    }
}
