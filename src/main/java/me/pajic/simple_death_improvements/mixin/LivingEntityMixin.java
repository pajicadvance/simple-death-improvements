package me.pajic.simple_death_improvements.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import me.pajic.simple_death_improvements.SDI;
import me.pajic.simple_death_improvements.access.ExperienceOrbAccess;
import me.pajic.simple_death_improvements.access.PlayerAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
//? if > 1.21.1 {
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.simple_death_improvements.util.ModUtil;
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

    @Unique LivingEntity self = (LivingEntity) (Object) this;

	//? if > 1.21.1 {
    @Inject(
            method = "createItemStackToDrop",
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
		if (self instanceof Player player) {
			return ModUtil.trySaveItemsOnDeath(player, ((PlayerAccess) player).sdi$getLastSafeBlockPosition(), level, posX, posY, posZ, itemStack, original);
		}
		return original.call(level, posX, posY, posZ, itemStack);
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
		if (SDI.CONFIG.noXpSplatterOnDeath.get() && self instanceof Player player) {
			BlockPos safePos = ((PlayerAccess) player).sdi$getLastSafeBlockPosition();
			ExperienceOrb orb = new ExperienceOrb(EntityType.EXPERIENCE_ORB, level);
			orb.setPos(safePos.getX() + 0.5, safePos.getY() + 1, safePos.getZ() + 0.5);
			((ExperienceOrbAccess) orb).sdi$setValue(amount);
			level.addFreshEntity(orb);
			SDI.debugLog("Prevented XP splatter for player {}", self.getDisplayName().getString());
			return false;
		}
		return true;
	}
}
