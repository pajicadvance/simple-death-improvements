package me.pajic.simple_death_improvements.mixin;

//? if > 1.21.1 {

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.simple_death_improvements.SDI;
import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.EnumMap;

@Mixin(EntityEquipment.class)
public abstract class EntityEquipmentMixin {

    @Shadow @Final private EnumMap<EquipmentSlot, ItemStack> items;

    @WrapMethod(method = "dropAll")
    private void keepOffhandAndEquipment(LivingEntity entity, Operation<Void> original) {
        if (entity instanceof Player && (SDI.CONFIG.keepHotbarOnDeath.get() || SDI.CONFIG.keepArmorOnDeath.get() || SDI.CONFIG.keepOffhandOnDeath.get())) {
            items.replaceAll((slot, stack) -> {
                if (SDI.CONFIG.keepOffhandOnDeath.get() && slot.equals(EquipmentSlot.OFFHAND) &&
                        !SDI.CONFIG.offhandDropList.contains(SDI.getItemId(stack))) return stack;
                if (SDI.CONFIG.keepArmorOnDeath.get() && !SDI.CONFIG.armorDropList.contains(SDI.getItemId(stack)) && (
                        slot.equals(EquipmentSlot.HEAD) ||
                        slot.equals(EquipmentSlot.CHEST) ||
                        slot.equals(EquipmentSlot.LEGS) ||
                        slot.equals(EquipmentSlot.FEET)
                )) return stack;
                entity.drop(stack, !SDI.CONFIG.noItemSplatterOnDeath.get(), false);
                return ItemStack.EMPTY;
            });
        }
        else original.call(entity);
    }

    @ModifyArg(
            method = "dropAll",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;"
            ),
            index = 1
    )
    private boolean preventItemSplatterOnDeath(boolean dropAround) {
		return !SDI.CONFIG.noItemSplatterOnDeath.get() && dropAround;
    }
}
//?}
