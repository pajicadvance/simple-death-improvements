package me.pajic.simple_death_improvements.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.authlib.GameProfile;
import me.pajic.simple_death_improvements.SDI;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {

    @Shadow public abstract @NotNull ServerLevel level();

    public ServerPlayerMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @SuppressWarnings("resource")
	@WrapMethod(method = "restoreFrom")
    private void restoreItems(ServerPlayer that, boolean keepEverything, Operation<Void> original) {
        if (
                !keepEverything && !that.isSpectator() &&
                !level().getGameRules().get(GameRules.KEEP_INVENTORY)
        ) {
            if (SDI.CONFIG.keepArmorOnDeath.get() || SDI.CONFIG.keepHotbarOnDeath.get() || SDI.CONFIG.keepOffhandOnDeath.get()) {
                getInventory().replaceWith(that.getInventory());
            }
        }
        original.call(that, keepEverything);
    }
}
