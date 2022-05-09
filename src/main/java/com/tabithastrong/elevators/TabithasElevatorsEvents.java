package com.tabithastrong.elevators;

import com.tabithastrong.elevators.TabithasElevators;
import com.tabithastrong.elevators.block.ElevatorBlock;
import com.tabithastrong.elevators.block.ElevatorImpl;
import com.tabithastrong.elevators.block.ElevatorSlabBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class TabithasElevatorsEvents {
    public static BlockPos findElevatorInDirection(World world, BlockPos pos, Direction dir, Block block) {
        BlockPos next = pos.offset(dir);

        for(int i = 0; i < 100; i++) {
            Block b = world.getBlockState(next).getBlock();

            if(b instanceof  ElevatorImpl) {
                if(((ElevatorImpl) b).getColour() == ((ElevatorImpl) block).getColour()) {
                    return next;
                }
            }


            next = next.offset(dir);
        }

        return null;
    }

    public static boolean isAreaValidForPlayer(World world, BlockPos pos) {
        return world.isAir(pos.offset(Direction.UP)) && world.isAir(pos.offset(Direction.UP, 2));
    }

    public static ActionResult onPlayerJump(PlayerEntity player) {
        World world = player.world;

        Block potential_slab = world.getBlockState(player.getBlockPos()).getBlock();
        Block block = potential_slab == Blocks.AIR ? world.getBlockState(player.getBlockPos().offset(Direction.DOWN)).getBlock() : potential_slab;

        if(block instanceof ElevatorImpl) {
            if(!world.isClient) {
                BlockPos next = findElevatorInDirection(world, player.getBlockPos(), Direction.UP, block);

                if(next != null && isAreaValidForPlayer(world, next)) {
                    world.playSound(null, player.getBlockPos(), SoundEvents.UI_TOAST_OUT, SoundCategory.BLOCKS, 1f, 1.5f);
                    player.teleport(player.getX(), next.getY()+1, player.getZ());
                    return ActionResult.FAIL;
                } else {
                    world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_REDSTONE_TORCH_BURNOUT, SoundCategory.BLOCKS, 0.2f, 1f);
                }
            }
        }

        return ActionResult.PASS;
    }

    public static ActionResult onPlayerSneak(PlayerEntity player) {
        World world = player.world;
        Block block = world.getBlockState(player.getBlockPos().offset(Direction.DOWN)).getBlock();

        if(block instanceof ElevatorBlock) {
            if(!world.isClient) {
                BlockPos next = findElevatorInDirection(world, player.getBlockPos().offset(Direction.DOWN), Direction.DOWN, block);

                if(next != null && isAreaValidForPlayer(world, next)) {
                    world.playSound(null, player.getBlockPos(), SoundEvents.UI_TOAST_IN, SoundCategory.BLOCKS, 1f, 1f);
                    player.teleport(player.getX(), next.getY()+1, player.getZ());
                    return ActionResult.FAIL;
                } else {
                    world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_REDSTONE_TORCH_BURNOUT, SoundCategory.BLOCKS, 0.2f, 1f);
                }
            }
        }

        return ActionResult.PASS;
    }
}
