package me.pajic.simple_death_improvements.mixin;

import me.pajic.simple_death_improvements.access.ExperienceOrbAccess;
import net.minecraft.world.entity.ExperienceOrb;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ExperienceOrb.class)
public abstract class ExperienceOrbMixin implements ExperienceOrbAccess {

	@Shadow /*? fabric {*/protected/*?} else {*//*public*//*?}*/ abstract void setValue(int value);

	@Override
	public void sdi$setValue(int value) {
		setValue(value);
	}
}
