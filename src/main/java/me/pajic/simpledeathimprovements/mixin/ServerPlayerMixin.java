package me.pajic.simpledeathimprovements.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
//? if > 1.21.1 {
/*import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.simpledeathimprovements.Config;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
*///?}

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {

    public ServerPlayerMixin(Level level, BlockPos pos, float yRot, GameProfile gameProfile) {
        super(level, pos, yRot, gameProfile);
    }

    //? if > 1.21.1 {
    /*@Unique ServerPlayer self = (ServerPlayer) (Object) this;
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
        if (Config.noDeathItemDespawn && self.isDeadOrDying()) {
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
        if (Config.noItemSplatterOnDeath && self.isDeadOrDying()) {
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
        if (!lastSafePos.equals(BlockPos.ZERO) && self.isDeadOrDying()) {
            if (Config.tryItemLavaSaveOnDeath && self.isInLava() || Config.tryItemVoidSaveOnDeath && self.getY() < (double) (level.getMinY() - 64)) {
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
        if (self.getBlockStateOn().entityCanStandOn(self.level(), self.getOnPos(), self) && !self.isInLava()) {
            lastSafePos = self.getOnPos();
        }
    }
    *///?}
}
