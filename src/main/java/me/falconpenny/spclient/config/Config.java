package me.falconpenny.spclient.config;

import lombok.Getter;
import me.falconpenny.spclient.Client;
import me.falconpenny.spclient.modules.IModule;
import me.falconpenny.spclient.modules.ModuleHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Config {
    @Getter
    private static final Config instance = new Config();
    @Getter
    private Configuration config;
    @Getter
    private static final String
            category_switches = "Switches",
            category_keybinds = "Keybinds",
            category_messages = "Messages",
            category_options = "Options";

    @Getter
    private boolean switchTogglenotify;
    @Getter
    private String messageTogglemessage;

    @Getter
    private final Map<IModule, KeyBinding> keybinds = new LinkedHashMap<>();

    @Getter
    private int minCps;
    @Getter
    private int maxCps;

    public void init(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        sync();

        FMLCommonHandler.instance().bus().register(this);
    }

    private void sync() {
        config.addCustomCategoryComment(category_switches, Client.translate("spclient.config.category.comment.switches"));
        config.addCustomCategoryComment(category_messages, Client.translate("spclient.config.category.comment.messages"));
        config.addCustomCategoryComment(category_keybinds, Client.translate("spclient.config.category.comment.keybinds"));
        config.addCustomCategoryComment(category_options, Client.translate("spclient.config.category.comment.options"));

        Property p;
        {
            p = config.get(category_switches, "toggle-notify", true, Client.translate("spclient.config.switches.comment.toggle-notify")).setLanguageKey("spclient.config.switches.toggle-notify");
            switchTogglenotify = p.getBoolean();
        }
        {
            p = config.get(category_messages, "toggle-message", Client.translate("spclient.config.messages.default.toggle-message"), Client.translate("spclient.config.messages.comment.toggle-message")).setLanguageKey("spclient.config.messages.toggle-message");
            messageTogglemessage = p.getString();
        }
        {
            p = config.get(category_options, "minCps", 7, Client.translate("spclient.config.options.comment.mincps")).setLanguageKey("spclient.config.options.mincps");
            minCps = p.getInt();

            p = config.get(category_options, "maxCps", 10, Client.translate("spclient.config.options.comment.maxcps")).setLanguageKey("spclient.config.options.maxcps");
            maxCps = p.getInt();
        }
        {
            for (IModule module : ModuleHandler.getHandler().getModules()) {
                p = config.get(category_keybinds, module.name(), Keyboard.KEY_NONE, Client.translate("spclient.config.keybinds.comment").replace("$MODULE", module.localizedName()));
                String name = module.name();
                if (name == null) {
                    continue;
                }
                KeyBinding key;
                keybinds.put(module, key = new KeyBinding(StatCollector.canTranslate("spclient.keybinds.module." + name.toLowerCase()) ? "spclient.keybinds.module." + name.toLowerCase() : "MODULE: " + name, p.getInt(0), "spclient.keybinds.category"));
                ClientRegistry.registerKeyBinding(key);
            }
        }
    }

    @SubscribeEvent
    public void configChange(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equals(Client.MODID)) {
            removeBinds();
            sync();
        }
    }

    public void writeBinds() {
        for (Map.Entry<IModule, KeyBinding> entry : keybinds.entrySet()) {
            config.get(category_keybinds, entry.getKey().name(), 0).set(entry.getValue().getKeyCode());
        }
    }

    public void removeBinds() {
        List<KeyBinding> binds = Arrays.stream(Minecraft.getMinecraft().gameSettings.keyBindings).filter(bind -> !keybinds.containsValue(bind)).collect(Collectors.toList());
        Minecraft.getMinecraft().gameSettings.keyBindings = binds.toArray(new KeyBinding[binds.size()]);
    }

    private Config() {
    }
}
