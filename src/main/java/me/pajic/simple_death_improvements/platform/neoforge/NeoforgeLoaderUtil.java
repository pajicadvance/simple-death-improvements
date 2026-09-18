package me.pajic.simple_death_improvements.platform.neoforge;

//? neoforge {

/*import me.pajic.simple_death_improvements.platform.MultiLoaderUtil;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

public class NeoforgeLoaderUtil implements MultiLoaderUtil {

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevEnv() {
        return !FMLLoader/^? >=1.21.9 {^/.getCurrent()/^?}^/.isProduction();
    }
}
*///?}
