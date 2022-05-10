package com.tabithastrong.elevators.mixin;

import com.tabithastrong.elevators.TabithasElevators;
import com.tabithastrong.elevators.event.PlayerJumpCallback;
import com.tabithastrong.elevators.event.PlayerSneakCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {

    /***
     * @param isSneaking - called from Minecraft and is used to determine if the player is sneaking
     * @param info - can be used to cancel the function
     *
     * Mixin used to call the events we have created
     */
    @Inject(at = @At("HEAD"), method="Lnet/minecraft/entity/Entity;setSneaking(Z)V")
    private void updateInput(boolean isSneaking, CallbackInfo info) {
        // Get entity
        Entity entity = (((Entity)(Object)this));

        // Check if player and event is for sneaking
        if(entity instanceof PlayerEntity && isSneaking) {
            // Get player
            PlayerEntity player = (PlayerEntity) entity;

            // Check that the player isn't already sneaking
            if(!player.isSneaking()) {
                // Call event
                ActionResult result = PlayerSneakCallback.EVENT.invoker().interact(player);
            }
        }
    }
}
