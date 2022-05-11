package com.tabithastrong.elevators.block;

import net.minecraft.block.Block;
import net.minecraft.util.DyeColor;

/*
    Elevator Block skeleton class, implements ElevatorImpl which returns the dye color
 */
public class ElevatorBlock extends Block implements ElevatorImpl {
    private DyeColor dyeColor;
    public ElevatorBlock(Settings settings, DyeColor color) {
        super(settings);
        this.dyeColor = color;
    }

    @Override
    public DyeColor getColour() {
        return dyeColor;
    }
}
