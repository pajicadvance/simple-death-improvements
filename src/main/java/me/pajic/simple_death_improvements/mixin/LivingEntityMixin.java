package me.pajic.simple_death_improvements.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import me.pajic.simple_death_improvements.SDI;
import me.pajic.simple_death_improvements.access.ExperienceOrbAccess;
import me.pajic.simple_death_improvements.access.ItemEntityAccess;
import me.pajic.simple_death_improvements.access.PlayerAccess;
import me.pajic.simple_death_improvements.util.ModUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

//? >=26.1 {
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
//?}

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Unique LivingEntity sdi$self = (LivingEntity) (Object) this;

    //? >=26.1 {
    @Inject(
            method = "createItemStackToDrop",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/item/ItemEntity;setPickUpDelay(I)V"
            )
    )
    private void preventItemDespawnOnDeath(
			ItemStack itemStack,
			boolean randomly,
			boolean thrownFromHand,
			CallbackInfoReturnable<ItemEntity> cir,
			@Local ItemEntity entity
	) {
        if (sdi$self instanceof Player && sdi$self.isDeadOrDying()) {
            if (SDI.CONFIG.noDeathItemDespawn.get()) {
                entity.setUnlimitedLifetime();
                SDI.debugLog("Set infinite lifetime to items dropped by player {}", sdi$self.getDisplayName().getString());
            } else ((ItemEntityAccess) entity).sdi$setLifetime(SDI.CONFIG.itemDespawnTimer.get() * 20);
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
        if (sdi$self instanceof Player && SDI.CONFIG.noItemSplatterOnDeath.get() && sdi$self.isDeadOrDying()) {
            args.setAll(0.0d, 0.0d, 0.0d);
			SDI.debugLog("Prevented item splatter for player {}", sdi$self.getDisplayName().getString());
        }
    }

    @WrapOperation(
            method = "createItemStackToDrop",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/item/ItemEntity;"
            )
    )
    private ItemEntity trySaveItemsOnDeath(Level level, double x, double y, double z, ItemStack itemStack, Operation<ItemEntity> original) {
		if (sdi$self instanceof Player player) {
			return ModUtil.trySaveItemsOnDeath(player, ((PlayerAccess) player).sdi$getLastSafeBlockPosition(), level, x, y, z, itemStack, original);
		}
		return original.call(level, x, y, z, itemStack);
    }
    //?}

	@WrapWithCondition(
			method = "dropExperience",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/ExperienceOrb;award(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;I)V"
			)
	)
	private boolean preventXpSplatter(ServerLevel level, Vec3 pos, int amount) {
		if (SDI.CONFIG.noXpSplatterOnDeath.get() && sdi$self instanceof Player player) {
			BlockPos safePos = ((PlayerAccess) player).sdi$getLastSafeBlockPosition();
			ExperienceOrb orb = new ExperienceOrb(ModUtil.XP_ORB, level);
			orb.setPos(safePos.getX() + 0.5, safePos.getY() + 1, safePos.getZ() + 0.5);
			((ExperienceOrbAccess) orb).sdi$setValue(amount);
			level.addFreshEntity(orb);
			SDI.debugLog("Prevented XP splatter for player {}", sdi$self.getDisplayName().getString());
			return false;
		}
		return true;
	}
}
