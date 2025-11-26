package me.pajic.simple_death_improvements.platform.fabric;

//? fabric {

import me.pajic.simple_death_improvements.SDI;
import net.fabricmc.api.ModInitializer;

@SuppressWarnings("unused")
public class FabricEntrypoint implements ModInitializer {

	@Override
	public void onInitialize() {
		SDI.onInitialize();
	}
}
//?}
