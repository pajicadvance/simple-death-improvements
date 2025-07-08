package me.pajic.simpledeathimprovements.compat;

//? if < 1.21.7 {
import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.events.OnDeathCallback;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import me.pajic.simpledeathimprovements.Main;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import java.util.UUID;
//?}

import net.minecraft.server.level.ServerPlayer;

public class AccessoriesCompat {
    //? if < 1.21.7
    private static final Object2ObjectOpenHashMap<UUID, NonNullList<ItemStack>> KEEP_CACHE = new Object2ObjectOpenHashMap<>();

    public static void init() {
        //? if < 1.21.7 {
        OnDeathCallback.EVENT.register((
                currentState,
                entity,
                capability,
                damageSource,
                droppedStacks
        ) -> {
            switch (Main.CONFIG.keepAccessories.get()) {
                case ALL -> {
                    if (entity instanceof Player player) {
                        NonNullList<ItemStack> keepList = NonNullList.create();
                        droppedStacks.forEach(stack -> {
                            if (!stack.is(Items.AIR)) keepList.add(stack.copy());
                        });
                        KEEP_CACHE.put(player.getUUID(), keepList);
                        return TriState.FALSE;
                    }
                    return currentState;
                }
                case LIST -> {
                    if (entity instanceof Player player) {
                        NonNullList<ItemStack> dropList = NonNullList.create();
                        NonNullList<ItemStack> keepList = NonNullList.create();
                        droppedStacks.forEach(stack -> {
                            if (!stack.is(Items.AIR)) {
                                if (Main.CONFIG.accessoryKeepList.get().contains(BuiltInRegistries.ITEM.getKey(stack.getItem()))) {
                                    keepList.add(stack.copy());
                                } else {
                                    dropList.add(stack);
                                }
                            }
                        });
                        for (int i = 0; i < dropList.size(); i++) {
                            ItemStack itemStack = dropList.get(i);
                            if (!itemStack.isEmpty()) {
                                player.drop(itemStack, !Main.CONFIG.noItemSplatterOnDeath.get(), false);
                                dropList.set(i, ItemStack.EMPTY);
                            }
                        }
                        KEEP_CACHE.put(player.getUUID(), keepList);
                        return TriState.FALSE;
                    }
                    return currentState;
                }
            }
            return currentState;
        });
        //?}
    }


    public static void restoreAccessoryInventory(ServerPlayer respawnedPlayer) {
        //? if < 1.21.7 {
        AccessoriesCapability.getOptionally(respawnedPlayer).ifPresent(ac -> {
            UUID uuid = respawnedPlayer.getUUID();
            if (KEEP_CACHE.containsKey(uuid)) {
                KEEP_CACHE.get(uuid).forEach(ac::attemptToEquipAccessory);
                KEEP_CACHE.remove(uuid);
            }
        });
        //?}
    }
}
