package me.pajic.simple_death_improvements.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.simple_death_improvements.SDI;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

//? <26.1 {
/*import net.minecraft.core.NonNullList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import java.util.List;
*///?}

@Mixin(Inventory.class)
public class InventoryMixin {

    //? <26.1 {
    /*@Shadow @Final public NonNullList<ItemStack> items;
    @Shadow @Final public NonNullList<ItemStack> armor;
    @Shadow @Final public NonNullList<ItemStack> offhand;
    *///?}

    @ModifyArg(
            method = "dropAll",
            at = @At(
                    value = "INVOKE",
                    //~ if >26.2 'drop' -> 'createItemStackToDrop'
                    target = "Lnet/minecraft/world/entity/player/Player;createItemStackToDrop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;"
            ),
            index = 1
    )
    private boolean preventItemSplatterOnDeath(boolean dropAround) {
		return !SDI.CONFIG.noItemSplatterOnDeath.get() && dropAround;
    }

    @ModifyExpressionValue(
            method = "dropAll",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"
            )
    )
    //? <26.1 {
    /*private boolean keepItems(boolean original, @Local List<ItemStack> list, @Local ItemStack item, @Local int i) {
        if (SDI.CONFIG.keepArmorOnDeath.get() && list.equals(armor) && !SDI.CONFIG.armorDropList.contains(SDI.getItemId(item))) return true;
        if (SDI.CONFIG.keepHotbarOnDeath.get() && list.equals(items) && i < 9 && !SDI.CONFIG.hotbarDropList.contains(SDI.getItemId(item))) return true;
		if (SDI.CONFIG.keepOffhandOnDeath.get() && list.equals(offhand) && !SDI.CONFIG.offhandDropList.contains(SDI.getItemId(item))) return true;
        return original;
    }
    *///?} else {
    private boolean keepHotbarItems(boolean original, @Local ItemStack itemStack, @Local int i) {
        return SDI.CONFIG.keepHotbarOnDeath.get() && i < 9 && !SDI.CONFIG.hotbarDropList.contains(SDI.getItemId(itemStack)) || original;
    }
    //?}
}
