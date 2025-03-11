package me.pajic.simpledeathimprovements.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.simpledeathimprovements.Main;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Inventory.class)
public class InventoryMixin {

    @Shadow @Final public Player player;
    @Shadow @Final public NonNullList<ItemStack> items;
    @Shadow @Final public NonNullList<ItemStack> armor;
    @Shadow @Final public NonNullList<ItemStack> offhand;

    @ModifyArg(
            method = "dropAll",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;"
            ),
            index = 1
    )
    private boolean preventItemSplatterOnDeath(boolean dropAround) {
        if (Main.CONFIG.noItemSplatterOnDeath()) {
            return false;
        }
        return dropAround;
    }

    @WrapMethod(method = "dropAll")
    private void dropItems(Operation<Void> original) {
        if (Main.CONFIG.keepArmorOnDeath() || Main.CONFIG.keepHotbarOnDeath()) {
            dropList(items, Main.CONFIG.keepHotbarOnDeath() ? 9 : 0);
            if (!Main.CONFIG.keepArmorOnDeath()) dropList(armor, 0);
            if (!Main.CONFIG.keepHotbarOnDeath()) dropList(offhand, 0);
        }
        else original.call();
    }

    @Unique
    private void dropList(NonNullList<ItemStack> list, int startIndex) {
        for (int i = startIndex; i < list.size(); i++) {
            ItemStack itemStack = list.get(i);
            if (!itemStack.isEmpty()) {
                player.drop(itemStack, !Main.CONFIG.noItemSplatterOnDeath(), false);
                list.set(i, ItemStack.EMPTY);
            }
        }
    }
}