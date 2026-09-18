package me.pajic.simple_death_improvements.compat;

import me.pajic.simple_death_improvements.platform.MultiLoaderUtil;
import org.jetbrains.annotations.Nullable;

public interface AccessoryUtil {

    boolean TRINKETS_LOADED = MultiLoaderUtil.INSTANCE.isModLoaded("trinkets_updated") || MultiLoaderUtil.INSTANCE.isModLoaded("trinkets");
    boolean CURIOS_LOADED = MultiLoaderUtil.INSTANCE.isModLoaded("curios");

    @Nullable AccessoryUtil INSTANCE = makeInstance();

    @Nullable static AccessoryUtil makeInstance() {
        //? !(neoforge && 1.21.1) {
        //~ if 1.21.1 'TrinketsCompat' -> 'TrinketsLegacyCompat'
        if (TRINKETS_LOADED) return new TrinketsCompat();
        //?}
        //? neoforge
        //if (CURIOS_LOADED) return new CuriosCompat();
        return null;
    }

    void registerDropEvent();
}
