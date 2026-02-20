package me.pajic.simple_death_improvements.mixin;

import me.pajic.simple_death_improvements.access.ExperienceOrbAccess;
import net.minecraft.world.entity.ExperienceOrb;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ExperienceOrb.class)
public abstract class ExperienceOrbMixin implements ExperienceOrbAccess {

	//? if 1.21.1
	//@Shadow private int value;
	//? if > 1.21.1
	@Shadow protected abstract void setValue(int value);

	@Override
	public void sdi$setValue(int value) {
		//? if 1.21.1
		//this.value = value;
		//? if > 1.21.1
		setValue(value);
	}
}
