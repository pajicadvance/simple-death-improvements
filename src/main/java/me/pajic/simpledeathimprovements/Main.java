package me.pajic.simpledeathimprovements;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.pajic.simpledeathimprovements.compat.AccessoriesCompat;
import me.pajic.simpledeathimprovements.config.ModConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod("simple_death_improvements")
public class Main {
    public static final String MOD_ID = "simple_death_improvements";
    public static final ResourceLocation CONFIG_RL = ResourceLocation.fromNamespaceAndPath(MOD_ID, "config");
    public static me.pajic.simpledeathimprovements.config.ModConfig CONFIG = ConfigApiJava.registerAndLoadConfig(ModConfig::new);
    private static final Logger LOGGER = LoggerFactory.getLogger("Simple Death Improvements");
    private static final boolean DEBUG = !FMLLoader.isProduction();
    public static final boolean ACCESSORIES_LOADED = ModList.get().isLoaded("accessories");

    public static ResourceLocation getItemRl(ItemStack item) {
        return BuiltInRegistries.ITEM.getKey(item.getItem());
    }

    public Main() {
        if (ACCESSORIES_LOADED) AccessoriesCompat.init();
    }

    public static void debugLog(String message, Object ... args) {
        if (DEBUG) LOGGER.info(message, args);
    }
}
