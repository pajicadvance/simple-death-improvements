package me.pajic.simpledeathimprovements.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.simpledeathimprovements.Main;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    //? if >= 1.21.7 {
    /*@Unique LivingEntity self = (LivingEntity) (Object) this;
    @Unique BlockPos lastSafePos = BlockPos.ZERO;

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
        if (self instanceof Player && Main.CONFIG.noDeathItemDespawn.get() && self.isDeadOrDying()) {
            itemEntity.setUnlimitedLifetime();
        }
    }

    @ModifyArgs(
            method = "createItemStackToDrop",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/item/ItemEntity;setDeltaMovement(DDD)V",
                    ordinal = 1
            )
    )
    private void preventItemThrowOnDeath(Args args) {
        if (self instanceof Player && Main.CONFIG.noItemSplatterOnDeath.get() && self.isDeadOrDying()) {
            args.setAll(0.0d, 0.0d, 0.0d);
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
        if (self instanceof Player && !lastSafePos.equals(BlockPos.ZERO) && self.isDeadOrDying()) {
            if (Main.CONFIG.tryItemLavaSaveOnDeath.get() && self.isInLava() || Main.CONFIG.tryItemVoidSaveOnDeath.get() && self.getY() < (double) (level.getMinY() - 64)) {
                return original.call(level, (double) lastSafePos.getX(), (double) lastSafePos.getY() + 1, (double) lastSafePos.getZ(), itemStack);
            }
        }
        return original.call(level, posX, posY, posZ, itemStack);
    }

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void trackLastSafeSpot(CallbackInfo ci) {
        if (self instanceof Player && self.getBlockStateOn().entityCanStandOn(self.level(), self.getOnPos(), self) && !self.isInLava()) {
            lastSafePos = self.getOnPos();
        }
    }
    *///?}
}
