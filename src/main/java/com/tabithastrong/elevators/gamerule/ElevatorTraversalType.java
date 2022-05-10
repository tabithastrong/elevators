package com.tabithastrong.elevators.gamerule;

import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

/***
 * Enum to handle the Game Rule, contains the name and sound for the type
 */
public enum ElevatorTraversalType
{
    NONE("none", null),
    EXPERIENCE("experience", SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP),
    HUNGER("hunger", SoundEvents.ENTITY_PLAYER_BURP);

    // Name of the enum, used toString
    String name;

    // Sound event that is used when traversing
    public final SoundEvent sound;

    ElevatorTraversalType(String name, SoundEvent sound) {
        this.name = name;
        this.sound = sound;
    }

    @Override
    public String toString() {
        return name;
    }
}
