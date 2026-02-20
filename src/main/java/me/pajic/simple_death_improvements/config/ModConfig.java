package me.pajic.simple_death_improvements.config;

import me.fzzyhmstrs.fzzy_config.annotations.Version;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIdentifier;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.pajic.simple_death_improvements.SDI;
import me.pajic.simple_death_improvements.util.AccessoryKeepMode;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

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
	public ValidatedBoolean noXpSplatterOnDeath = new ValidatedBoolean(true);
	public ValidatedBoolean playerDropMoreXpOnDeath = new ValidatedBoolean(true);
	public ValidatedInt droppedExperiencePercent = new ValidatedInt(80, 100, 1);
	public ValidatedBoolean keepArmorOnDeath = new ValidatedBoolean(false);
	public ValidatedList<Identifier> armorDropList = ValidatedIdentifier.ofRegistry(Identifier.withDefaultNamespace("diamond_chestplate"), BuiltInRegistries.ITEM).toList();
	public ValidatedBoolean keepHotbarOnDeath = new ValidatedBoolean(false);
	public ValidatedList<Identifier> hotbarDropList = ValidatedIdentifier.ofRegistry(Identifier.withDefaultNamespace("diamond_pickaxe"), BuiltInRegistries.ITEM).toList();
	public ValidatedBoolean keepOffhandOnDeath = new ValidatedBoolean(false);
	public ValidatedList<Identifier> offhandDropList = ValidatedIdentifier.ofRegistry(Identifier.withDefaultNamespace("diamond_pickaxe"), BuiltInRegistries.ITEM).toList();
	public ValidatedEnum<AccessoryKeepMode> keepAccessories = new ValidatedEnum<>(AccessoryKeepMode.NONE);
	public ValidatedList<Identifier> accessoryKeepList = ValidatedIdentifier.ofRegistry(Identifier.withDefaultNamespace("diamond"), BuiltInRegistries.ITEM).toList();
}
