package me.pajic.simpledeathimprovements.config;

import me.fzzyhmstrs.fzzy_config.annotations.Version;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIdentifier;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.pajic.simpledeathimprovements.Main;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

@Version(version = 1)
public class ModConfig extends Config {
    public ModConfig() {
        super(Main.CONFIG_RL);
    }

    public ValidatedBoolean noItemSplatterOnDeath = new ValidatedBoolean(true);
    public ValidatedBoolean noDeathItemDespawn = new ValidatedBoolean(true);
    public ValidatedBoolean tryItemLavaSaveOnDeath = new ValidatedBoolean(true);
    public ValidatedBoolean tryItemVoidSaveOnDeath = new ValidatedBoolean(true);
    public ValidatedBoolean explosionResistantItems = new ValidatedBoolean(true);
    public ValidatedBoolean playerDropMoreXpOnDeath = new ValidatedBoolean(true);
    public ValidatedInt droppedExperiencePercent = new ValidatedInt(80, 100, 1);
    public ValidatedBoolean keepArmorOnDeath = new ValidatedBoolean(false);
    public ValidatedBoolean keepHotbarOnDeath = new ValidatedBoolean(false);
    public ValidatedEnum<AccessoryKeepMode> keepAccessories = new ValidatedEnum<>(AccessoryKeepMode.NONE);
    public ValidatedList<ResourceLocation> accessoryKeepList = ValidatedIdentifier.ofRegistry(ResourceLocation.withDefaultNamespace("diamond"), BuiltInRegistries.ITEM).toList();
}
