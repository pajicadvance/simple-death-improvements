package me.pajic.simple_death_improvements.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.simple_death_improvements.SDI;
import me.pajic.simple_death_improvements.access.PlayerAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//~ if <26.1 'gamerules.GameRules' -> 'GameRules'
import net.minecraft.world.level.gamerules.GameRules;

//? >=26.1 {
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
//?} else {
/*import me.pajic.simple_death_improvements.access.ItemEntityAccess;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.simple_death_improvements.util.ModUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
*///?}

@Mixin(Player.class)
public abstract class PlayerMixin implements PlayerAccess {

    @Shadow public abstract Component getDisplayName();

    @Unique Player sdi$self = (Player) (Object) this;
    @Unique BlockPos sdi$lastSafePos = BlockPos.ZERO;
    @Unique boolean sdi$startedTrackingSafePos = false;
    @Unique int sdi$delayBeforeTracking = 100;

    //? <26.1 {
    /*@Inject(
            method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/item/ItemEntity;setPickUpDelay(I)V"
            )
    )
    private void preventItemDespawnOnDeath(
            ItemStack droppedItem,
            boolean dropAround,
            boolean includeThrowerName,
            CallbackInfoReturnable<ItemEntity> cir,
            @Local ItemEntity itemEntity
    ) {
        if (sdi$self.isDeadOrDying()) {
            if (SDI.CONFIG.noDeathItemDespawn.get()) {
                itemEntity.setUnlimitedLifetime();
                SDI.debugLog("Set infinite lifetime to items dropped by player {}", sdi$self.getDisplayName().getString());
            } else ((ItemEntityAccess) itemEntity).sdi$setLifetime(SDI.CONFIG.itemDespawnTimer.get() * 20);
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
        if (SDI.CONFIG.noItemSplatterOnDeath.get() && sdi$self.isDeadOrDying()) {
            args.setAll(0.0d, 0.0d, 0.0d);
            SDI.debugLog("Prevented item splatter for player {}", sdi$self.getDisplayName().getString());
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
		return ModUtil.trySaveItemsOnDeath(sdi$self, sdi$lastSafePos, level, posX, posY, posZ, itemStack, original);
    }
    *///?}

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void trackLastSafeSpot(CallbackInfo ci) {
        BlockPos pos = sdi$self.getOnPos();
        if (sdi$delayBeforeTracking > 0) sdi$delayBeforeTracking--;
        else if (pos != BlockPos.ZERO && sdi$self.getBlockStateOn().entityCanStandOn(sdi$self.level(), pos, sdi$self) && !sdi$self.isInLava()) {
            if (!sdi$startedTrackingSafePos) {
                sdi$startedTrackingSafePos = true;
				SDI.debugLog("Started tracking last safe position for player {}", sdi$self.getDisplayName().getString());
            }
            sdi$lastSafePos = pos;
        }
    }

    @WrapMethod(method = "getBaseExperienceReward")
    private int modifyDroppedXpOnDeath(/*? >=26.1 {*/ServerLevel level, /*?}*/Operation<Integer> original) {
        //~ if <26.1 'get(GameRules.KEEP_INVENTORY)' -> 'getBoolean(GameRules.RULE_KEEPINVENTORY)'
        //~ if <26.1 'level' -> 'sdi$self.level()'
        if (SDI.CONFIG.playerDropMoreXpOnDeath.get() && !level.getGameRules().get(GameRules.KEEP_INVENTORY)) {
            int xp = 0;
            int xpLevel = sdi$self.experienceLevel;
            for (int i = 0; i < xpLevel; i++) {
                sdi$self.experienceLevel = i;
                xp += sdi$self.getXpNeededForNextLevel();
            }
            sdi$self.experienceLevel = xpLevel;
            xp += (int) (sdi$self.experienceProgress * sdi$self.getXpNeededForNextLevel());
            return (int) (xp * (float) SDI.CONFIG.droppedExperiencePercent.get() / 100);
        }
        return original.call(/*? >=26.1 {*/level/*?}*/);
    }

    //? if 1.21.1 {
    /*@Inject(
            method = "addAdditionalSaveData",
            at = @At("TAIL")
    )
    private void saveLastSafeSpot(CompoundTag compound, CallbackInfo ci) {
        compound.putInt("LastSafePosX", sdi$lastSafePos.getX());
        compound.putInt("LastSafePosY", sdi$lastSafePos.getY());
        compound.putInt("LastSafePosZ", sdi$lastSafePos.getZ());
        SDI.debugLog("Saved safe position {} {} {} for player {}", sdi$lastSafePos.getX(), sdi$lastSafePos.getY(), sdi$lastSafePos.getZ(), getDisplayName().getString());
    }

    @Inject(
            method = "readAdditionalSaveData",
            at = @At("TAIL")
    )
    private void loadLastSafeSpot(CompoundTag compound, CallbackInfo ci) {
        int x = compound.getInt("LastSafePosX");
        int y = compound.getInt("LastSafePosY");
        int z = compound.getInt("LastSafePosZ");
        sdi$lastSafePos = new BlockPos(x, y, z);
        SDI.debugLog("Loaded safe position {} {} {} for player {}", x, y, z, getDisplayName().getString());
    }
    *///?} else {
    @Inject(
            method = "addAdditionalSaveData",
            at = @At("TAIL")
    )
    private void saveLastSafeSpot(ValueOutput output, CallbackInfo ci) {
        output.putInt("LastSafePosX", sdi$lastSafePos.getX());
        output.putInt("LastSafePosY", sdi$lastSafePos.getY());
        output.putInt("LastSafePosZ", sdi$lastSafePos.getZ());
        SDI.debugLog("Saved safe position {} {} {} for player {}", sdi$lastSafePos.getX(), sdi$lastSafePos.getY(), sdi$lastSafePos.getZ(), getDisplayName().getString());
    }

    @Inject(
            method = "readAdditionalSaveData",
            at = @At("TAIL")
    )
    private void loadLastSafeSpot(ValueInput input, CallbackInfo ci) {
        int x = input.getIntOr("LastSafePosX", 0);
        int y = input.getIntOr("LastSafePosY", 0);
        int z = input.getIntOr("LastSafePosZ", 0);
        sdi$lastSafePos = new BlockPos(x, y, z);
        SDI.debugLog("Loaded safe position {} {} {} for player {}", x, y, z, getDisplayName().getString());
    }
    //?}

    @Override
    public BlockPos sdi$getLastSafeBlockPosition() {
        return sdi$lastSafePos;
    }
}
