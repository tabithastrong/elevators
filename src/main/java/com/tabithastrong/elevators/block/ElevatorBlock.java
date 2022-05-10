package com.tabithastrong.elevators.block;

import com.tabithastrong.elevators.TabithasElevators;
import com.tabithastrong.elevators.gamerule.ElevatorTraversalType;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
