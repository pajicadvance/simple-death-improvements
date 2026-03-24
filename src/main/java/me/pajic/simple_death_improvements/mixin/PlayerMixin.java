package me.pajic.simple_death_improvements.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.simple_death_improvements.SDI;
import me.pajic.simple_death_improvements.access.PlayerAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin implements PlayerAccess {

    @Shadow public abstract Component getDisplayName();

    @Unique Player sdi$self = (Player) (Object) this;
    @Unique BlockPos sdi$lastSafePos = BlockPos.ZERO;
    @Unique boolean sdi$startedTrackingSafePos = false;
    @Unique int sdi$delayBeforeTracking = 100;

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
    private int modifyDroppedXpOnDeath(ServerLevel level, Operation<Integer> original) {
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
        return original.call(level);
    }

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

    @Override
    public BlockPos sdi$getLastSafeBlockPosition() {
        return sdi$lastSafePos;
    }
}
