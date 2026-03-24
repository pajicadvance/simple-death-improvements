package me.pajic.simple_death_improvements.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.simple_death_improvements.SDI;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Inventory.class)
public class InventoryMixin {

    @ModifyArg(
            method = "dropAll",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;"
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
    private boolean keepHotbarItems(boolean original, @Local(name = "itemStack") ItemStack itemStack, @Local(name = "i") int i) {
		return SDI.CONFIG.keepHotbarOnDeath.get() && i < 9 && !SDI.CONFIG.hotbarDropList.contains(SDI.getItemId(itemStack)) || original;
    }
}
