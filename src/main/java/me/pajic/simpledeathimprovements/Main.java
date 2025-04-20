package me.pajic.simpledeathimprovements;

import me.pajic.simpledeathimprovements.compat.AccessoriesCompat;
import me.pajic.simpledeathimprovements.config.ModConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class Main implements ModInitializer {

    public static final ModConfig CONFIG = ModConfig.createAndLoad();
    public static final boolean ACCESSORIES_LOADED = FabricLoader.getInstance().isModLoaded("accessories");

    @Override
    public void onInitialize() {
        if (ACCESSORIES_LOADED) AccessoriesCompat.init();
    }
}
