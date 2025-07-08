package me.pajic.simpledeathimprovements.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.simpledeathimprovements.Main;
import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.EnumMap;

@Mixin(EntityEquipment.class)
public abstract class EntityEquipmentMixin {

    @Shadow @Final private EnumMap<EquipmentSlot, ItemStack> items;

    @WrapMethod(method = "dropAll")
    private void keepOffhandAndEquipment(LivingEntity entity, Operation<Void> original) {
        if (entity instanceof Player && Main.CONFIG.keepHotbarOnDeath.get() || Main.CONFIG.keepArmorOnDeath.get()) {
            items.replaceAll((slot, stack) -> {
                if (Main.CONFIG.keepHotbarOnDeath.get() && slot.equals(EquipmentSlot.OFFHAND)) return stack;
                if (Main.CONFIG.keepArmorOnDeath.get() && (
                        slot.equals(EquipmentSlot.HEAD) ||
                        slot.equals(EquipmentSlot.CHEST) ||
                        slot.equals(EquipmentSlot.LEGS) ||
                        slot.equals(EquipmentSlot.FEET)
                )) return stack;
                entity.drop(stack, true, false);
                return ItemStack.EMPTY;
            });
        }
        else original.call(entity);
    }
}
