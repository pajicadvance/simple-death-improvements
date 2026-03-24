package me.pajic.simple_death_improvements.platform.fabric;

//? fabric {

import me.pajic.simple_death_improvements.SDI;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ModInitializer;

@Entrypoint("main")
public class FabricEntrypoint implements ModInitializer {

	@Override
	public void onInitialize() {
		SDI.onInitialize();
	}
}
//?}
