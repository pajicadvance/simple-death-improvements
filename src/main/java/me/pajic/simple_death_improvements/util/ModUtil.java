package me.pajic.simple_death_improvements.util;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.simple_death_improvements.SDI;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ModUtil {

	public static ItemEntity trySaveItemsOnDeath(
			Player player,
			BlockPos lastSafePos,
			Level level,
			double posX,
			double posY,
			double posZ,
			ItemStack itemStack,
			Operation<ItemEntity> original
	) {
		if (player.isDeadOrDying() && !lastSafePos.equals(BlockPos.ZERO)) {
			if (player.getBlockStateOn().is(BlockTags.AIR)) {
				SDI.debugLog("Player {} died while airborne", player.getName().getString());
				boolean isSafeBelow = false;
				BlockPos onPos = player.getOnPos();
				int x = onPos.getX();
				int z = onPos.getZ();
				for (int y = onPos.getY() - 1; y > level.getMinY(); y--) {
					BlockPos pos = new BlockPos(x, y, z);
					BlockState state = level.getBlockState(pos);
					if (SDI.CONFIG.tryItemLavaSaveOnDeath.get() && state.is(Blocks.LAVA) && !player.isInLava()) {
						SDI.debugLog(
								"Block below player at {} {} {} is lava",
								pos.getX(), pos.getY(), pos.getZ()
						);
						return saveItems(level, lastSafePos, itemStack, player, original, ItemSaveReason.LAVA_AIR);
					}
					if (!isSafeBelow && state.isFaceSturdy(level, pos, Direction.UP)) {
						SDI.debugLog(
								"Block {} below player at {} {} {} is safe",
								state.getBlock().getName().getString(),
								pos.getX(), pos.getY(), pos.getZ()
						);
						isSafeBelow = true;
					}
				}
				if (SDI.CONFIG.tryItemVoidSaveOnDeath.get() && !isSafeBelow) {
					SDI.debugLog("Didn't find safe block below player");
					return saveItems(level, lastSafePos, itemStack, player, original, ItemSaveReason.VOID_AIR);
				}
			}
			if (SDI.CONFIG.tryItemLavaSaveOnDeath.get() && player.isInLava()) {
				return saveItems(level, lastSafePos, itemStack, player, original, ItemSaveReason.LAVA);
			}
			if (SDI.CONFIG.tryItemVoidSaveOnDeath.get() && player.getY() < (double) (level.getMinY() - 64)) {
				return saveItems(level, lastSafePos, itemStack, player, original, ItemSaveReason.VOID);
			}
		}
		return original.call(level, posX, posY, posZ, itemStack);
	}

	private static ItemEntity saveItems(
			Level level,
			BlockPos lastSafePos,
			ItemStack itemStack,
			Player player,
			Operation<ItemEntity> original,
			ItemSaveReason reason
	) {
		SDI.debugLog(
				"Dropped items for player {} at {} {} {}. Reason: {}",
				player.getDisplayName().getString(),
				lastSafePos.getX(), lastSafePos.getY(), lastSafePos.getZ(),
				reason.toString()
		);
		return original.call(
				level,
				(double) lastSafePos.getX() + 0.5,
				(double) lastSafePos.getY() + 1,
				(double) lastSafePos.getZ() + 0.5,
				itemStack
		);
	}
}
