package me.pajic.simple_death_improvements.mixin.compat;

//? if neoforge {

/*import com.llamalad7.mixinextras.sugar.Local;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import io.wispforest.accessories.neoforge.AccessoriesForge;
import me.pajic.simple_death_improvements.SDI;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.stream.Stream;

@IfModLoaded("accessories")
@Mixin(AccessoriesForge.class)
public class AccessoriesForgeMixin {

    @ModifyArgs(
            method = "createDroppedEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/item/ItemEntity;setDeltaMovement(DDD)V",
                    ordinal = 0
            )
    )
    private static void noAccessorySplatter(Args args) {
        if (SDI.CONFIG.noItemSplatterOnDeath.get()) {
			SDI.debugLog("Prevented accessory splatter");
            args.setAll(0.0d, 0.0d, 0.0d);
        }
    }

    @Inject(
            method = "createDroppedEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/item/ItemEntity;setPickUpDelay(I)V"
            )
    )
    private static void noAccessoryDespawn(Entity entity, ItemStack stack, CallbackInfoReturnable<Stream<ItemEntity>> cir, @Local ItemEntity itemEntity) {
        if (SDI.CONFIG.noDeathItemDespawn.get()) {
            itemEntity.setUnlimitedLifetime();
			SDI.debugLog("Set infinite lifetime to dropped accessories");
        }
    }
}
*///?}
