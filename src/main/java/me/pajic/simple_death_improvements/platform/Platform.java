package me.pajic.simple_death_improvements.platform;

public interface Platform {
	boolean isModLoaded(String modId);

	boolean isDebug();

	ModLoader loader();

	String mcVersion();

	enum ModLoader {
		FABRIC, NEOFORGE, FORGE
	}
}
