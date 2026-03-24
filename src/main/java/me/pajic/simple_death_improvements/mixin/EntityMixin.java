package me.pajic.simple_death_improvements.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.pajic.simple_death_improvements.SDI;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {

    @ModifyReturnValue(
            method = "ignoreExplosion",
            at = @At("RETURN")
    )
    private boolean preventExplosionsFromDestroyingItems(boolean original) {
		return (Entity) (Object) this instanceof ItemEntity ? SDI.CONFIG.explosionResistantItems.get() || original : original;
    }
}
