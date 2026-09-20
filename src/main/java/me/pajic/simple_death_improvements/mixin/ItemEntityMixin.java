package me.pajic.simple_death_improvements.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.pajic.simple_death_improvements.access.ItemEntityAccess;
import net.minecraft.world.entity.item.ItemEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemEntity.class)
public class ItemEntityMixin implements ItemEntityAccess {

    @Unique private int sdi$lifetime = 6000;

    @Override
    public void sdi$setLifetime(int lifetime) {
        sdi$lifetime = lifetime;
    }

    @ModifyExpressionValue(
            method = "tick",
            //? fabric {
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=6000"
            )
            //?} else {
            /*at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/entity/item/ItemEntity;lifespan:I",
                    opcode = Opcodes.GETFIELD
            )
            *///?}
    )
    private int modifyLifetime(int original) {
        return sdi$lifetime != 6000 ? sdi$lifetime : original;
    }
}
