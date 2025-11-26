package me.pajic.simple_death_improvements;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.pajic.simple_death_improvements.compat.AccessoriesCompat;
import me.pajic.simple_death_improvements.compat.CompatFlags;
import me.pajic.simple_death_improvements.config.ModConfig;
import me.pajic.simple_death_improvements.platform.Platform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//? fabric {
import me.pajic.simple_death_improvements.platform.fabric.FabricPlatform;
//?} neoforge {
/*import me.pajic.simple_death_improvements.platform.neoforge.NeoforgePlatform;
*///?}

@SuppressWarnings("LoggingSimilarMessage")
public class SDI {

	public static final String MOD_ID = /*$ mod_id*/ "simple_death_improvements";
	public static final String MOD_VERSION = /*$ mod_version*/ "1.4.2";
	public static final String MOD_FRIENDLY_NAME = /*$ mod_name*/ "Simple Death Improvements";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final ResourceLocation CONFIG_RL = id("config");
	public static ModConfig CONFIG = ConfigApiJava.registerAndLoadConfig(ModConfig::new);
	private static final Platform PLATFORM = createPlatformInstance();

	public static void onInitialize() {
		if (CompatFlags.ACCESSORIES_LOADED) AccessoriesCompat.init();
	}

	public static ResourceLocation getItemId(ItemStack stack) {
		return BuiltInRegistries.ITEM.getKey(stack.getItem());
	}

	public static Platform xplat() {
		return PLATFORM;
	}

	private static Platform createPlatformInstance() {
		//? fabric {
		return new FabricPlatform();
		//?} neoforge {
		/*return new NeoforgePlatform();
		*///?}
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	public static void debugLog(String message, Object ... args) {
		if (PLATFORM.isDebug()) LOGGER.info(message, args);
	}
}
