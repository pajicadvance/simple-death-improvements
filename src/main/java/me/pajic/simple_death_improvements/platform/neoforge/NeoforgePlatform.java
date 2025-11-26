package me.pajic.simple_death_improvements.platform.neoforge;

//? neoforge {

/*import me.pajic.simple_death_improvements.platform.Platform;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

public class NeoforgePlatform implements Platform {

	@Override
	public boolean isModLoaded(String modId) {
		return ModList.get().isLoaded(modId);
	}

	@Override
	public ModLoader loader() {
		return ModLoader.NEOFORGE;
	}

	@Override
	public String mcVersion() {
		return FMLLoader/^? if 1.21.1 {^//^.versionInfo()^//^?} else {^/.getCurrent().getVersionInfo()/^?}^/.mcVersion();
	}

	@Override
	public boolean isDebug() {
		return !FMLLoader/^? if > 1.21.1 {^/.getCurrent()/^?}^/.isProduction();
	}
}
*///?}
