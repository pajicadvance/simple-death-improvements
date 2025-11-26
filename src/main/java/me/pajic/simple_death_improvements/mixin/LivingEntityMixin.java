package me.pajic.simple_death_improvements.mixin;

//? if > 1.21.1 {

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.simple_death_improvements.SDI;
import me.pajic.simple_death_improvements.access.PlayerAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {


    @Unique LivingEntity self = (LivingEntity) (Object) this;

    @Inject(
            method = "createItemStackToDrop",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/item/ItemEntity;setPickUpDelay(I)V"
            )
    )
    private void preventItemDespawnOnDeath(ItemStack droppedItem, boolean dropAround, boolean includeThrowerName, CallbackInfoReturnable<ItemEntity> cir,
                                           @Local ItemEntity itemEntity
    ) {
        if (self instanceof Player && SDI.CONFIG.noDeathItemDespawn.get() && self.isDeadOrDying()) {
            itemEntity.setUnlimitedLifetime();
			SDI.debugLog("Set infinite lifetime to items dropped by player {}", self.getDisplayName().getString());
        }
    }

    @ModifyArgs(
            method = "createItemStackToDrop",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/item/ItemEntity;setDeltaMovement(DDD)V"
            )
    )
    private void preventItemThrowOnDeath(Args args) {
        if (self instanceof Player && SDI.CONFIG.noItemSplatterOnDeath.get() && self.isDeadOrDying()) {
            args.setAll(0.0d, 0.0d, 0.0d);
			SDI.debugLog("Prevented item splatter for player {}", self.getDisplayName().getString());
        }
    }

    @WrapOperation(
            method = "createItemStackToDrop",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/item/ItemEntity;"
            )
    )
    private ItemEntity trySaveItemsOnDeath(Level level, double posX, double posY, double posZ, ItemStack itemStack, Operation<ItemEntity> original) {
        if (self instanceof Player player && self.isDeadOrDying()) {
            BlockPos lastSafePos = ((PlayerAccess) player).sdi$getLastSafeBlockPosition();
            if (!lastSafePos.equals(BlockPos.ZERO) && SDI.CONFIG.tryItemLavaSaveOnDeath.get() && self.isInLava() || SDI.CONFIG.tryItemVoidSaveOnDeath.get() && self.getY() < (double) (level.getMinY() - 64)) {
				SDI.debugLog("Dropped items for player {} at {} {} {}", self.getDisplayName().getString(), lastSafePos.getX(), lastSafePos.getY(), lastSafePos.getZ());
                return original.call(level, (double) lastSafePos.getX() + 0.5, (double) lastSafePos.getY() + 1, (double) lastSafePos.getZ() + 0.5, itemStack);
            }
        }
        return original.call(level, posX, posY, posZ, itemStack);
    }
}
//?}
