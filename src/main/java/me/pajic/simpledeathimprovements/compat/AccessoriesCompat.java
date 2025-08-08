package me.pajic.simpledeathimprovements.compat;

//? if < 1.21.7 {
import io.wispforest.accessories.api.DropRule;
import io.wispforest.accessories.api.events.OnDropCallback;
import me.pajic.simpledeathimprovements.Main;
import net.minecraft.core.registries.BuiltInRegistries;
//?}


public class AccessoriesCompat {
    public static void init() {
        //? if < 1.21.7 {
        OnDropCallback.EVENT.register((dropRule, stack, reference, damageSource) -> {
            switch (Main.CONFIG.keepAccessories.get()) {
                case ALL -> {
                    return DropRule.KEEP;
                }
                case LIST -> {
                    return Main.CONFIG.accessoryKeepList.get().contains(BuiltInRegistries.ITEM.getKey(stack.getItem())) ? DropRule.KEEP : DropRule.DEFAULT;
                }
                case NONE -> {
                    return DropRule.DEFAULT;
                }
            }
            return dropRule;
        });
        //?}
    }
}
