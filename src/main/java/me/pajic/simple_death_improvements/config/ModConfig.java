package me.pajic.simple_death_improvements.config;

import me.fzzyhmstrs.fzzy_config.annotations.Version;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIdentifier;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.pajic.simple_death_improvements.SDI;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

@Version(version = 1)
public class ModConfig extends Config {
    public ModConfig() {
        super(SDI.CONFIG_RL);
    }

	public ValidatedBoolean noItemSplatterOnDeath = new ValidatedBoolean(true);
	public ValidatedBoolean noDeathItemDespawn = new ValidatedBoolean(true);
	public ValidatedBoolean tryItemLavaSaveOnDeath = new ValidatedBoolean(true);
	public ValidatedBoolean tryItemVoidSaveOnDeath = new ValidatedBoolean(true);
	public ValidatedBoolean explosionResistantItems = new ValidatedBoolean(true);
	public ValidatedBoolean playerDropMoreXpOnDeath = new ValidatedBoolean(true);
	public ValidatedInt droppedExperiencePercent = new ValidatedInt(80, 100, 1);
	public ValidatedBoolean keepArmorOnDeath = new ValidatedBoolean(false);
	public ValidatedList<ResourceLocation> armorDropList = ValidatedIdentifier.ofRegistry(ResourceLocation.withDefaultNamespace("diamond_chestplate"), BuiltInRegistries.ITEM).toList();
	public ValidatedBoolean keepHotbarOnDeath = new ValidatedBoolean(false);
	public ValidatedList<ResourceLocation> hotbarDropList = ValidatedIdentifier.ofRegistry(ResourceLocation.withDefaultNamespace("diamond_pickaxe"), BuiltInRegistries.ITEM).toList();
	public ValidatedEnum<AccessoryKeepMode> keepAccessories = new ValidatedEnum<>(AccessoryKeepMode.NONE);
	public ValidatedList<ResourceLocation> accessoryKeepList = ValidatedIdentifier.ofRegistry(ResourceLocation.withDefaultNamespace("diamond"), BuiltInRegistries.ITEM).toList();
}
