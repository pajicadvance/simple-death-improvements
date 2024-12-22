package me.pajic.simpledeathimprovements.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.simpledeathimprovements.Main;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
//? if <= 1.21.1 {
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//?}
//? if > 1.21.1
/*import net.minecraft.server.level.ServerLevel;*/

@Mixin(Player.class)
public abstract class PlayerMixin {

    @Unique Player self = (Player) (Object) this;

    //? if <= 1.21.1 {
    @Unique BlockPos lastSafePos = BlockPos.ZERO;

    @Inject(
            method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/item/ItemEntity;setPickUpDelay(I)V"
            )
    )
    private void preventItemDespawnOnDeath(ItemStack droppedItem, boolean dropAround, boolean includeThrowerName, CallbackInfoReturnable<ItemEntity> cir,
                              @Local ItemEntity itemEntity
    ) {
        if (Main.CONFIG.noDeathItemDespawn() && self.isDeadOrDying()) {
            itemEntity.setUnlimitedLifetime();
        }
    }

    @ModifyArgs(
            method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/item/ItemEntity;setDeltaMovement(DDD)V",
                    ordinal = 1
            )
    )
    private void preventItemThrowOnDeath(Args args) {
        if (Main.CONFIG.noItemSplatterOnDeath() && self.isDeadOrDying()) {
            args.setAll(0.0d, 0.0d, 0.0d);
        }
    }

    @WrapOperation(
            method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/item/ItemEntity;"
            )
    )
    private ItemEntity trySaveItemsOnDeath(Level level, double posX, double posY, double posZ, ItemStack itemStack, Operation<ItemEntity> original) {
        if (!lastSafePos.equals(BlockPos.ZERO) && self.isDeadOrDying()) {
            if (Main.CONFIG.tryItemLavaSaveOnDeath() && self.isInLava() || Main.CONFIG.tryItemVoidSaveOnDeath() && self.getY() < (double) (level.getMinBuildHeight() - 64)) {
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
    //?}

    @WrapMethod(method = "getBaseExperienceReward")
    //? if <= 1.21.1
    private int modifyDroppedXpOnDeath(Operation<Integer> original) {
    //? if > 1.21.1
    /*private int modifyDroppedXpOnDeath(ServerLevel level, Operation<Integer> original) {*/
        //? if <= 1.21.1
        if (Main.CONFIG.playerDropMoreXpOnDeath() && !self.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
        //? if > 1.21.1
        /*if (Main.CONFIG.playerDropMoreXpOnDeath() && !level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {*/
            int xp = 0;
            int xpLevel = self.experienceLevel;
            for (int i = 0; i < xpLevel; i++) {
                self.experienceLevel = i;
                xp += self.getXpNeededForNextLevel();
            }
            self.experienceLevel = xpLevel;
            xp += (int) (self.experienceProgress * self.getXpNeededForNextLevel());
            return (int) (xp * (float) Main.CONFIG.droppedExperiencePercent() / 100);
        }
        //? if <= 1.21.1
        return original.call();
        //? if > 1.21.1
        /*return original.call(level);*/
    }
}