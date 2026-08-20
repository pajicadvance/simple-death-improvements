package me.pajic.simple_death_improvements.compat;

import me.pajic.simple_death_improvements.SDI;
import me.pajic.simple_death_improvements.access.PlayerAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;

//? if neoforge {
/*import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
*///?}

public class CuriosApiCompat {
	//? if neoforge {
	/*public static void init() {
		NeoForge.EVENT_BUS.register(CuriosApiCompat.class);
		SDI.debugLog("'Curios API' was found and compat class registered.");
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onLivingDrops(LivingDropsEvent event) {
		if (!(event.getEntity() instanceof Player player)) return;
		if (event.getDrops().isEmpty()) return;
		processDrops(player, event.getDrops());
	}
	*///?}

	private static void processDrops(Player player, Collection<ItemEntity> drops) {
		Level level = player.level();
		BlockPos lastSafePos = ((PlayerAccess) player).sdi$getLastSafeBlockPosition();

		//? if 1.21.1
		//int minLevelY = level.getMinBuildHeight();
		//? if > 1.21.1
		int minLevelY = level.getMinY();

		boolean relocate = !lastSafePos.equals(BlockPos.ZERO) && (
				SDI.CONFIG.tryItemLavaSaveOnDeath.get() && player.isInLava()
						|| SDI.CONFIG.tryItemVoidSaveOnDeath.get() && player.getY() < minLevelY - 64
		);

		for (ItemEntity item : drops) {
			if (SDI.CONFIG.noItemSplatterOnDeath.get()) {
				item.setDeltaMovement(Vec3.ZERO);
			}
			if (SDI.CONFIG.noDeathItemDespawn.get()) {
				item.setUnlimitedLifetime();
			}
			if (relocate) {
				item.setPos(lastSafePos.getX() + 0.5, lastSafePos.getY() + 1, lastSafePos.getZ() + 0.5);
			}
		}
	}
}
