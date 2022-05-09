package com.tabithastrong.elevators.block;

import net.minecraft.block.Block;
import net.minecraft.util.DyeColor;

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
