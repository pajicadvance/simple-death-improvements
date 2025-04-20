package me.pajic.simpledeathimprovements;

import me.pajic.simpledeathimprovements.compat.AccessoriesCompat;
import me.pajic.simpledeathimprovements.config.Config;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod("simple_death_improvements")
public class Main {
    public static final boolean ACCESSORIES_LOADED = ModList.get().isLoaded("accessories");

    public Main(ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.SERVER, Config.SPEC);
        if (ACCESSORIES_LOADED) AccessoriesCompat.init();
    }
}
