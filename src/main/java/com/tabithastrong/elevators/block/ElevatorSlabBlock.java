package com.tabithastrong.elevators.block;

import net.minecraft.block.SlabBlock;
import net.minecraft.util.DyeColor;

public class ElevatorSlabBlock extends SlabBlock implements ElevatorImpl {
    private DyeColor dyeColor;
    public ElevatorSlabBlock(Settings settings, DyeColor color) {
        super(settings);
        this.dyeColor = color;
    }

    @Override
    public DyeColor getColour() {
        return dyeColor;
    }
}
