package me.pajic.simpledeathimprovements;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.pajic.simpledeathimprovements.compat.AccessoriesCompat;
import me.pajic.simpledeathimprovements.config.ModConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;

@Mod("simple_death_improvements")
public class Main {
    public static final String MOD_ID = "simple_death_improvements";
    public static final ResourceLocation CONFIG_RL = ResourceLocation.fromNamespaceAndPath(MOD_ID, "config");
    public static me.pajic.simpledeathimprovements.config.ModConfig CONFIG = ConfigApiJava.registerAndLoadConfig(ModConfig::new);
    public static final boolean ACCESSORIES_LOADED = ModList.get().isLoaded("accessories");

    public static ResourceLocation getItemRl(ItemStack item) {
        return BuiltInRegistries.ITEM.getKey(item.getItem());
    }

    public Main(ModContainer modContainer) {
        if (ACCESSORIES_LOADED) AccessoriesCompat.init();
    }
}
