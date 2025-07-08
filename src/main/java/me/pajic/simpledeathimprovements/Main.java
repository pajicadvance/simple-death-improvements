package me.pajic.simpledeathimprovements;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.pajic.simpledeathimprovements.compat.AccessoriesCompat;
import me.pajic.simpledeathimprovements.config.ModConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;

public class Main implements ModInitializer {
    public static final String MOD_ID = "simple_death_improvements";
    public static final ResourceLocation CONFIG_RL = ResourceLocation.fromNamespaceAndPath(MOD_ID, "config");
    public static ModConfig CONFIG = ConfigApiJava.registerAndLoadConfig(ModConfig::new);
    public static final boolean ACCESSORIES_LOADED = FabricLoader.getInstance().isModLoaded("accessories");

    @Override
    public void onInitialize() {
        if (ACCESSORIES_LOADED) AccessoriesCompat.init();
    }
}
