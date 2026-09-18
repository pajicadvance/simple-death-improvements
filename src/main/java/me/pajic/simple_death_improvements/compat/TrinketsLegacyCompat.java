package me.pajic.simple_death_improvements.compat;

//? fabric && 1.21.1 {

/*import dev.emi.trinkets.api.TrinketEnums;
import dev.emi.trinkets.api.event.TrinketDropCallback;
import me.pajic.simple_death_improvements.SDI;

public class TrinketsLegacyCompat implements AccessoryUtil {

    @Override
    public void registerDropEvent() {
        TrinketDropCallback.EVENT.register((rule, stack, ref, entity) ->
                SDI.CONFIG.keepAccessoriesOnDeath.get() && !SDI.CONFIG.accessoryDropList.contains(SDI.getItemId(stack)) ?
                        TrinketEnums.DropRule.KEEP : TrinketEnums.DropRule.DEFAULT);
    }
}
*///?}
