package me.pajic.simpledeathimprovements.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.pajic.simpledeathimprovements.Config;
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
        Entity instance = (Entity) (Object) this;
        if (instance instanceof ItemEntity) return Config.explosionResistantItems || original;
        return original;
    }
}
