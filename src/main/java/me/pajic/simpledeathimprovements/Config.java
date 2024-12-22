package me.pajic.simpledeathimprovements;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

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

    private static final ModConfigSpec.BooleanValue PLAYER_DROP_MORE_XP_ON_DEATH = BUILDER
            .translation("text.config.simple_death_improvements.option.playerDropMoreXpOnDeath")
            .define("playerDropMoreXpOnDeath", true);

    private static final ModConfigSpec.IntValue DROPPED_EXPERIENCE_PERCENT = BUILDER
            .translation("text.config.simple_death_improvements.option.droppedExperiencePercent")
            .defineInRange("droppedExperiencePercent", 80, 1, 100);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean noItemSplatterOnDeath;
    public static boolean noDeathItemDespawn;
    public static boolean tryItemLavaSaveOnDeath;
    public static boolean tryItemVoidSaveOnDeath;
    public static boolean playerDropMoreXpOnDeath;
    public static int droppedExperiencePercent;

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
        playerDropMoreXpOnDeath = PLAYER_DROP_MORE_XP_ON_DEATH.get();
        droppedExperiencePercent = DROPPED_EXPERIENCE_PERCENT.get();
    }
}
