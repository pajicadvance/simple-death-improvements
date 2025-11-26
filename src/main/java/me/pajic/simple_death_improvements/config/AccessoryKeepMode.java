package me.pajic.simple_death_improvements.config;

import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;
import org.jetbrains.annotations.NotNull;

public enum AccessoryKeepMode implements EnumTranslatable {
    ALL, LIST, NONE;

    @Override
    @NotNull public String prefix() {
        return "simple_death_improvements.accessoryKeepMode";
    }
}
