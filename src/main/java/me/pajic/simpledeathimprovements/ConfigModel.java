package me.pajic.simpledeathimprovements;

import io.wispforest.owo.config.Option;
import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.RangeConstraint;
import io.wispforest.owo.config.annotation.Sync;

@Modmenu(modId = "simple_death_improvements")
@Config(name = "simple_death_improvements", wrapperName = "ModConfig")
@Sync(Option.SyncMode.OVERRIDE_CLIENT)
@SuppressWarnings("unused")
public class ConfigModel {
    public boolean noItemSplatterOnDeath = true;
    public boolean noDeathItemDespawn = true;
    public boolean tryItemLavaSaveOnDeath = true;
    public boolean tryItemVoidSaveOnDeath = true;
    public boolean playerDropMoreXpOnDeath = true;
    @RangeConstraint(min = 1, max = 100) public int droppedExperiencePercent = 80;
}
