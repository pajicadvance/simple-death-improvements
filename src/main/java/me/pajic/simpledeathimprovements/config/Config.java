package me.pajic.simpledeathimprovements.config;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = "simple_death_improvements", bus = EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue NO_ITEM_SPLATTER_ON_DEATH = BUILDER
            .translation("text.config.simple_death_improvements.option.noItemSplatterOnDeath")
            .define("noItemSplatterOnDeath", true);

    private static final ModConfigSpec.BooleanValue NO_DEATH_ITEM_DESPAWN = BUILDER
            .translation("text.config.simple_death_improvements.option.noDeathItemDespawn")
            .define("noDeathItemDespawn", true);

    private static final ModConfigSpec.BooleanValue TRY_ITEM_LAVA_SAVE_ON_DEATH = BUILDER
            .translation("text.config.simple_death_improvements.option.tryItemLavaSaveOnDeath")
            .define("tryItemLavaSaveOnDeath", true);

    private static final ModConfigSpec.BooleanValue TRY_ITEM_VOID_SAVE_ON_DEATH = BUILDER
            .translation("text.config.simple_death_improvements.option.tryItemVoidSaveOnDeath")
            .define("tryItemVoidSaveOnDeath", true);

    private static final ModConfigSpec.BooleanValue EXPLOSION_RESISTANT_ITEMS = BUILDER
            .translation("text.config.simple_death_improvements.option.explosionResistantItems")
            .define("explosionResistantItems", true);

    private static final ModConfigSpec.BooleanValue PLAYER_DROP_MORE_XP_ON_DEATH = BUILDER
            .translation("text.config.simple_death_improvements.option.playerDropMoreXpOnDeath")
            .define("playerDropMoreXpOnDeath", true);

    private static final ModConfigSpec.IntValue DROPPED_EXPERIENCE_PERCENT = BUILDER
            .translation("text.config.simple_death_improvements.option.droppedExperiencePercent")
            .defineInRange("droppedExperiencePercent", 80, 1, 100);

    private static final ModConfigSpec.BooleanValue KEEP_ARMOR_ON_DEATH = BUILDER
            .translation("text.config.simple_death_improvements.option.keepArmorOnDeath")
            .define("keepArmorOnDeath", false);

    private static final ModConfigSpec.BooleanValue KEEP_HOTBAR_ON_DEATH = BUILDER
            .translation("text.config.simple_death_improvements.option.keepHotbarOnDeath")
            .define("keepHotbarOnDeath", false);

    private static final ModConfigSpec.EnumValue<AccessoryKeepMode> KEEP_ACCESSORIES = BUILDER
            .translation("text.config.simple_death_improvements.option.keepAccessories")
            .defineEnum("keepAccessories", AccessoryKeepMode.NONE);

    private static final ModConfigSpec.ConfigValue<List<? extends String>> ACCESSORY_KEEP_LIST = BUILDER
            .translation("text.config.simple_death_improvements.option.accessoryKeepList")
            .defineListAllowEmpty("accessoryKeepList", List.of(), () -> "", o -> true);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean noItemSplatterOnDeath;
    public static boolean noDeathItemDespawn;
    public static boolean tryItemLavaSaveOnDeath;
    public static boolean tryItemVoidSaveOnDeath;
    public static boolean explosionResistantItems;
    public static boolean playerDropMoreXpOnDeath;
    public static int droppedExperiencePercent;
    public static boolean keepArmorOnDeath;
    public static boolean keepHotbarOnDeath;
    public static AccessoryKeepMode keepAccessories;
    public static List<String> accessoryKeepList;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent.Loading event) {
        updateConfig();
    }

    @SubscribeEvent
    static void onChange(final ModConfigEvent.Reloading event) {
        updateConfig();
    }

    private static void updateConfig() {
        noItemSplatterOnDeath = NO_ITEM_SPLATTER_ON_DEATH.get();
        noDeathItemDespawn = NO_DEATH_ITEM_DESPAWN.get();
        tryItemLavaSaveOnDeath = TRY_ITEM_LAVA_SAVE_ON_DEATH.get();
        tryItemVoidSaveOnDeath = TRY_ITEM_VOID_SAVE_ON_DEATH.get();
        explosionResistantItems = EXPLOSION_RESISTANT_ITEMS.get();
        playerDropMoreXpOnDeath = PLAYER_DROP_MORE_XP_ON_DEATH.get();
        droppedExperiencePercent = DROPPED_EXPERIENCE_PERCENT.get();
        keepArmorOnDeath = KEEP_ARMOR_ON_DEATH.get();
        keepHotbarOnDeath = KEEP_HOTBAR_ON_DEATH.get();
        keepAccessories = KEEP_ACCESSORIES.get();
        accessoryKeepList = new ArrayList<>(ACCESSORY_KEEP_LIST.get());
    }
}
