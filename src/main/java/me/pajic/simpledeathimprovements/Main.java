package me.pajic.simpledeathimprovements;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.pajic.simpledeathimprovements.compat.AccessoriesCompat;
import me.pajic.simpledeathimprovements.config.ModConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {
    public static final String MOD_ID = "simple_death_improvements";
    public static final ResourceLocation CONFIG_RL = ResourceLocation.fromNamespaceAndPath(MOD_ID, "config");
    public static ModConfig CONFIG = ConfigApiJava.registerAndLoadConfig(ModConfig::new);
    private static final Logger LOGGER = LoggerFactory.getLogger("Simple Death Improvements");
    private static final boolean DEBUG = FabricLoader.getInstance().isDevelopmentEnvironment();
    public static final boolean ACCESSORIES_LOADED = FabricLoader.getInstance().isModLoaded("accessories");

    public static ResourceLocation getItemRl(ItemStack item) {
        return BuiltInRegistries.ITEM.getKey(item.getItem());
    }

    @Override
    public void onInitialize() {
        if (ACCESSORIES_LOADED) AccessoriesCompat.init();
    }

    public static void debugLog(String message, Object ... args) {
        if (DEBUG) LOGGER.info(message, args);
    }
}
