package me.falconpenny.spclient.modules;

import lombok.Getter;
import me.falconpenny.spclient.configurations.RootConfiguration;
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
        if (!Keyboard.getEventKeyState() || Keyboard.getEventKey() == 0) {
            return;
        }
        modules.stream().filter(module -> module.key() == Keyboard.getEventKey()).findFirst().ifPresent(module -> {
            module.toggle();
            if (RootConfiguration.getInstance().getMessages().config().getBoolean(
                    "toggle-notify",
                    "switches",
                    true,
                    "Whether or not to automatically send toggle messages to the user.",
                    "Message upon module toggle"
            )) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(
                        RootConfiguration.getInstance().getMessages().config().getString(
                                "toggle-message",
                                "messages",
                                " \u2666 The module $MODULE has been toggled to the state of $STATE.",
                                "What the toggle message should be.",
                                "Message upon toggle"
                        )
                                .replace("$MODULE", Conditionals.withDefault(module.name(), "Undefined name."))
                                .replace("$STATE", module.state() ? "enabled" : "disabled")
                ).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
            }
        });
    }
}
