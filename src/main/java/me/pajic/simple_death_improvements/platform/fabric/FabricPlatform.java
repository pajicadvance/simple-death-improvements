package me.pajic.simple_death_improvements.platform.fabric;

//? fabric {

import me.pajic.simple_death_improvements.platform.Platform;
import net.fabricmc.loader.api.FabricLoader;

public class FabricPlatform implements Platform {

	@Override
	public boolean isModLoaded(String modId) {
		return FabricLoader.getInstance().isModLoaded(modId);
	}

	@Override
	public ModLoader loader() {
		return ModLoader.FABRIC;
	}

	@Override
	public String mcVersion() {
		return FabricLoader.getInstance().getRawGameVersion();
	}

	@Override
	public boolean isDebug() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}
}
//?}
