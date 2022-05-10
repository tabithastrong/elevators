package com.tabithastrong.elevators.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

/***
 * Player Sneak Event, called from a mixin and can be registered to know when the player begins to sneak
 */
public interface PlayerSneakCallback {
    /***
     * The event, loops the listeners and calls the interact function.
     */
    Event<PlayerSneakCallback> EVENT = EventFactory.createArrayBacked(PlayerSneakCallback.class,
        (listeners) -> (player) -> {
            for(PlayerSneakCallback listener : listeners) {
                ActionResult result = listener.interact(player);

                if(result != ActionResult.PASS) {
                    return result;
                }
            }
            return ActionResult.PASS;
        }
    );

    ActionResult interact(PlayerEntity player);
}
