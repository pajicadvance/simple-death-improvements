package me.pajic.simpledeathimprovements.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.simpledeathimprovements.Main;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;

@Mixin(Inventory.class)
public class InventoryMixin {

    //? if < 1.21.7 {
    @Shadow @Final public NonNullList<ItemStack> items;
    @Shadow @Final public NonNullList<ItemStack> armor;
    @Shadow @Final public NonNullList<ItemStack> offhand;
    //?}

    @ModifyArg(
            method = "dropAll",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;"
            ),
            index = 1
    )
    private boolean preventItemSplatterOnDeath(boolean dropAround) {
        if (Main.CONFIG.noItemSplatterOnDeath.get()) {
            return false;
        }
        return dropAround;
    }

    @ModifyExpressionValue(
            method = "dropAll",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"
            )
    )
    //? if < 1.21.7 {
    private boolean keepItems(boolean original, @Local List<ItemStack> list, @Local ItemStack item, @Local int i) {
        if (Main.CONFIG.keepArmorOnDeath.get() && list.equals(armor) && !Main.CONFIG.armorDropList.contains(Main.getItemRl(item))) return true;
        if (Main.CONFIG.keepHotbarOnDeath.get() && !Main.CONFIG.hotbarDropList.contains(Main.getItemRl(item))) {
            if (list.equals(offhand)) return true;
            if (list.equals(items) && i < 9) return true;
        }
        return original;
    }
    //?}
    //? if >= 1.21.7 {
    /*private boolean keepHotbarItems(boolean original, @Local ItemStack item, @Local int i) {
        if (Main.CONFIG.keepHotbarOnDeath.get() && i < 9 && !Main.CONFIG.hotbarDropList.contains(Main.getItemRl(item))) return true;
        return original;
    }
    *///?}
}