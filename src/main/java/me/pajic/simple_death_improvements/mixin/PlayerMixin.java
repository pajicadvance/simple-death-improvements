package me.pajic.simple_death_improvements.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.simple_death_improvements.SDI;
import me.pajic.simple_death_improvements.access.PlayerAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
//? if 1.21.1 {
/*import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import net.minecraft.nbt.CompoundTag;
*///?} else {
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
//?}
import net.minecraft.world.level./*? if > 1.21.10 {*/gamerules./*?}*/GameRules;

@Mixin(Player.class)
public abstract class PlayerMixin implements PlayerAccess {

    @Shadow public abstract Component getDisplayName();

    @Unique Player self = (Player) (Object) this;
    @Unique BlockPos lastSafePos = BlockPos.ZERO;
    @Unique boolean startedTrackingSafePos = false;
    @Unique int delayBeforeTracking = 100;

    //? if 1.21.1 {
    /*@Inject(
            method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/item/ItemEntity;setPickUpDelay(I)V"
            )
    )
    private void preventItemDespawnOnDeath(ItemStack droppedItem, boolean dropAround, boolean includeThrowerName, CallbackInfoReturnable<ItemEntity> cir,
                              @Local ItemEntity itemEntity
    ) {
        if (SDI.CONFIG.noDeathItemDespawn.get() && self.isDeadOrDying()) {
            itemEntity.setUnlimitedLifetime();
            SDI.debugLog("Set infinite lifetime to items dropped by player {}", self.getDisplayName().getString());
        }
    }

    @ModifyArgs(
            method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/item/ItemEntity;setDeltaMovement(DDD)V"
            )
    )
    private void preventItemThrowOnDeath(Args args) {
        if (SDI.CONFIG.noItemSplatterOnDeath.get() && self.isDeadOrDying()) {
            args.setAll(0.0d, 0.0d, 0.0d);
            SDI.debugLog("Prevented item splatter for player {}", self.getDisplayName().getString());
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
            if (SDI.CONFIG.tryItemLavaSaveOnDeath.get() && self.isInLava() || SDI.CONFIG.tryItemVoidSaveOnDeath.get() && self.getY() < (double) (level.getMinBuildHeight() - 64)) {
                SDI.debugLog("Dropped items for player {} at {} {} {}", self.getDisplayName().getString(), lastSafePos.getX(), lastSafePos.getY(), lastSafePos.getZ());
                return original.call(level, (double) lastSafePos.getX() + 0.5, (double) lastSafePos.getY() + 1, (double) lastSafePos.getZ() + 0.5, itemStack);
            }
        }
        return original.call(level, posX, posY, posZ, itemStack);
    }
    *///?}

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void trackLastSafeSpot(CallbackInfo ci) {
        BlockPos pos = self.getOnPos();
        if (delayBeforeTracking > 0) delayBeforeTracking--;
        else if (pos != BlockPos.ZERO && self.getBlockStateOn().entityCanStandOn(self.level(), pos, self) && !self.isInLava()) {
            if (!startedTrackingSafePos) {
                startedTrackingSafePos = true;
				SDI.debugLog("Started tracking last safe position for player {}", self.getDisplayName().getString());
            }
            lastSafePos = pos;
        }
    }

    @WrapMethod(method = "getBaseExperienceReward")
    //? if 1.21.1
    //private int modifyDroppedXpOnDeath(Operation<Integer> original) {
    //? if > 1.21.1
    private int modifyDroppedXpOnDeath(ServerLevel level, Operation<Integer> original) {
        //? if 1.21.1
        //if (SDI.CONFIG.playerDropMoreXpOnDeath.get() && !self.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
        //? if > 1.21.1
        if (SDI.CONFIG.playerDropMoreXpOnDeath.get() && !level.getGameRules()./*? if > 1.21.10 {*/get/*?} else {*//*getBoolean*//*?}*/(GameRules./*? if > 1.21.10 {*/KEEP_INVENTORY/*?} else {*//*RULE_KEEPINVENTORY*//*?}*/)) {
            int xp = 0;
            int xpLevel = self.experienceLevel;
            for (int i = 0; i < xpLevel; i++) {
                self.experienceLevel = i;
                xp += self.getXpNeededForNextLevel();
            }
            self.experienceLevel = xpLevel;
            xp += (int) (self.experienceProgress * self.getXpNeededForNextLevel());
            return (int) (xp * (float) SDI.CONFIG.droppedExperiencePercent.get() / 100);
        }
        //? if 1.21.1
        //return original.call();
        //? if > 1.21.1
        return original.call(level);
    }

    //? if 1.21.1 {
    /*@Inject(
            method = "addAdditionalSaveData",
            at = @At("TAIL")
    )
    private void saveLastSafeSpot(CompoundTag compound, CallbackInfo ci) {
        compound.putInt("LastSafePosX", lastSafePos.getX());
        compound.putInt("LastSafePosY", lastSafePos.getY());
        compound.putInt("LastSafePosZ", lastSafePos.getZ());
        SDI.debugLog("Saved safe position {} {} {} for player {}", lastSafePos.getX(), lastSafePos.getY(), lastSafePos.getZ(), getDisplayName().getString());
    }

    @Inject(
            method = "readAdditionalSaveData",
            at = @At("TAIL")
    )
    private void loadLastSafeSpot(CompoundTag compound, CallbackInfo ci) {
        int x = compound.getInt("LastSafePosX");
        int y = compound.getInt("LastSafePosY");
        int z = compound.getInt("LastSafePosZ");
        lastSafePos = new BlockPos(x, y, z);
        SDI.debugLog("Loaded safe position {} {} {} for player {}", x, y, z, getDisplayName().getString());
    }
    *///?} else {
    @Inject(
            method = "addAdditionalSaveData",
            at = @At("TAIL")
    )
    private void saveLastSafeSpot(ValueOutput output, CallbackInfo ci) {
        output.putInt("LastSafePosX", lastSafePos.getX());
        output.putInt("LastSafePosY", lastSafePos.getY());
        output.putInt("LastSafePosZ", lastSafePos.getZ());
		SDI.debugLog("Saved safe position {} {} {} for player {}", lastSafePos.getX(), lastSafePos.getY(), lastSafePos.getZ(), getDisplayName().getString());
    }

    @Inject(
            method = "readAdditionalSaveData",
            at = @At("TAIL")
    )
    private void loadLastSafeSpot(ValueInput input, CallbackInfo ci) {
        int x = input.getIntOr("LastSafePosX", 0);
        int y = input.getIntOr("LastSafePosY", 0);
        int z = input.getIntOr("LastSafePosZ", 0);
        lastSafePos = new BlockPos(x, y, z);
		SDI.debugLog("Loaded safe position {} {} {} for player {}", x, y, z, getDisplayName().getString());
    }
    //?}

    @Override
    public BlockPos sdi$getLastSafeBlockPosition() {
        return lastSafePos;
    }
}
