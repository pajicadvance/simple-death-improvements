package me.pajic.simpledeathimprovements.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.simpledeathimprovements.Main;
import com.mojang.authlib.GameProfile;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {

    //? if >= 1.21.7
    /*@Shadow public abstract @NotNull ServerLevel level();*/

    //? if < 1.21.7 {
    public ServerPlayerMixin(Level level, BlockPos pos, float yRot, GameProfile gameProfile) {
        super(level, pos, yRot, gameProfile);
    }
    //?}
    //? if >= 1.21.7 {
    /*public ServerPlayerMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }
    *///?}

    @WrapMethod(method = "restoreFrom")
    private void restoreItems(ServerPlayer that, boolean keepEverything, Operation<Void> original) {
        if (
                !keepEverything && !that.isSpectator() &&
                !level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)
        ) {
            if (Main.CONFIG.keepArmorOnDeath.get() || Main.CONFIG.keepHotbarOnDeath.get()) {
                getInventory().replaceWith(that.getInventory());
            }
        }
        original.call(that, keepEverything);
    }
}
