package com.tabithastrong.elevators.mixin;

import com.tabithastrong.elevators.TabithasElevators;
import com.tabithastrong.elevators.event.PlayerJumpCallback;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    /***
     * @param info Function info, used to cancel the event
     * Mixin for PlayerEntity is used to determine when the player is jumping
     */
    @Inject(at = @At("HEAD"), method="Lnet/minecraft/entity/player/PlayerEntity;jump()V", cancellable = true)
    private void updateInput(CallbackInfo info) {
        // Get the player and call the event
        PlayerEntity player = (((PlayerEntity)(Object)this));
        ActionResult result = PlayerJumpCallback.EVENT.invoker().interact(player);

        // If cancelled then return in jump
        if(result == ActionResult.FAIL) {
            info.cancel();
        }
    }
}
