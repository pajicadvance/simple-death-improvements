package me.pajic.simpledeathimprovements.compat;

//? if <= 1.21.1
/*import io.wispforest.accessories.api.DropRule;*/
//? if >= 1.21.8
import io.wispforest.accessories.api.events.DropRule;
import io.wispforest.accessories.api.events.OnDropCallback;
import me.pajic.simpledeathimprovements.Main;
import net.minecraft.core.registries.BuiltInRegistries;

public class AccessoriesCompat {
    public static void init() {
        OnDropCallback.EVENT.register((dropRule, stack, reference, damageSource) -> {
            switch (Main.CONFIG.keepAccessories.get()) {
                case ALL -> {
                    return DropRule.KEEP;
                }
                case LIST -> {
                    return Main.CONFIG.accessoryKeepList.get().contains(BuiltInRegistries.ITEM.getKey(stack.getItem())) ? DropRule.KEEP : dropRule;
                }
                case NONE -> {
                    return dropRule;
                }
            }
            return dropRule;
        });
    }
}
