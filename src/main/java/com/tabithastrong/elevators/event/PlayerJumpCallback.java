package com.tabithastrong.elevators.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

/***
 * Player Jump Event, called from a mixin and can be registered to know when the player jumps
 */
public interface PlayerJumpCallback {
    /***
     * The event, loops the listeners and calls the interact function.
     */
    Event<PlayerJumpCallback> EVENT = EventFactory.createArrayBacked(PlayerJumpCallback.class,
        (listeners) -> (player) -> {
            for(PlayerJumpCallback listener : listeners) {
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
