package me.pajic.simple_death_improvements.compat;

//? neoforge {

/*import me.pajic.simple_death_improvements.SDI;
import net.neoforged.neoforge.common.NeoForge;
import top.theillusivec4.curios.api.event.DropRulesEvent;

//~ if <26.1 'common.DropRule' -> 'type.capability.ICurio'
import top.theillusivec4.curios.api.common.DropRule;

public class CuriosCompat implements AccessoryUtil {

    @Override
    public void registerDropEvent() {
        NeoForge.EVENT_BUS.addListener(DropRulesEvent.class, event ->
                event.addOverride(
                        stack ->
                                SDI.CONFIG.keepAccessoriesOnDeath.get() &&
                                !SDI.CONFIG.accessoryDropList.contains(SDI.getItemId(stack)),
                        /^? <26.1 {^//^ICurio.^//^?}^/DropRule.ALWAYS_KEEP
                )
        );
    }
}
*///?}
