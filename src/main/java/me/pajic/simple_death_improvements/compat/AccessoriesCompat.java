package me.pajic.simple_death_improvements.compat;

//? if 1.21.1
//import io.wispforest.accessories.api.DropRule;
//? if > 1.21.1
import io.wispforest.accessories.api.events.DropRule;
import io.wispforest.accessories.api.events.OnDropCallback;
import me.pajic.simple_death_improvements.SDI;
import net.minecraft.core.registries.BuiltInRegistries;

public class AccessoriesCompat {
    public static void init() {
        OnDropCallback.EVENT.register((dropRule, stack, reference, damageSource) -> {
            switch (SDI.CONFIG.keepAccessories.get()) {
                case ALL -> {
                    return DropRule.KEEP;
                }
                case LIST -> {
                    return SDI.CONFIG.accessoryKeepList.get().contains(BuiltInRegistries.ITEM.getKey(stack.getItem())) ? DropRule.KEEP : dropRule;
                }
                case NONE -> {
                    return dropRule;
                }
            }
            return dropRule;
        });
    }
}
