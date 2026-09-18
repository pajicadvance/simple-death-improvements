package me.pajic.simple_death_improvements;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.pajic.simple_death_improvements.compat.AccessoryUtil;
import me.pajic.simple_death_improvements.config.ModConfig;
import me.pajic.simple_death_improvements.platform.MultiLoaderUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SDI {

    public static final String MOD_ID = /*$ mod_id*/ "simple_death_improvements";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static ModConfig CONFIG = ConfigApiJava.registerAndLoadConfig(ModConfig::new);

    public static void onInitialize() {
        if (AccessoryUtil.INSTANCE != null) AccessoryUtil.INSTANCE.registerDropEvent();
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static Identifier getItemId(ItemStack stack) {
        return BuiltInRegistries.ITEM.getKey(stack.getItem());
    }

    public static void debugLog(String message, Object ... args) {
        if (MultiLoaderUtil.INSTANCE.isDevEnv()) LOGGER.info(message, args);
    }
}
