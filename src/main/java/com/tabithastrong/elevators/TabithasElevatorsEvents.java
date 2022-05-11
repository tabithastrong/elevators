package com.tabithastrong.elevators;

import com.tabithastrong.elevators.block.ElevatorImpl;
import com.tabithastrong.elevators.block.ElevatorSlabBlock;
import com.tabithastrong.elevators.gamerule.ElevatorTraversalType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class TabithasElevatorsEvents {

    /***
     * Used to find the nearest elevator block in a direction
     * @param world World used to find block
     * @param pos Current block position
     * @param dir Direction to look in
     * @param block Block type to look for
     * @return Position of closest block or null if not found
     */
    public static BlockPos findElevatorInDirection(World world, BlockPos pos, Direction dir, Block block) {
        BlockPos next = pos.offset(dir).offset(dir);

        // Looks for 100 blocks in that direction
        for(int i = 0; i < 100; i++) {
            Block b = world.getBlockState(next).getBlock();

            // If find an elevator block with matching colour then return it
            if(b instanceof  ElevatorImpl) {
                if(((ElevatorImpl) b).getColour() == ((ElevatorImpl) block).getColour()) {
                    return next;
                }
            }

            // If didn't find block then offset and loop again
            next = next.offset(dir);
        }

        return null;
    }

    /***
     * Checks if a player can fit in this area
     * @param world World player is in
     * @param pos Position to check
     * @return True if can fit, false if can't
     */
    public static boolean isAreaValidForPlayer(World world, BlockPos pos) {
        return world.isAir(pos.offset(Direction.UP)) && world.isAir(pos.offset(Direction.UP, 2));
    }

    /***
     * Can the player currently travel, used to check again traversal type eg: does have enough xp or food
     * @param player Player to check
     * @param traversalType Traversal type of world
     * @return True if can travel, false if can't
     */
    public static boolean canPlayerTravel(PlayerEntity player, ElevatorTraversalType traversalType) {
        // If player is in creative we can travel
        if(player.isCreative()) {
            return true;
        } else {
            // Check that the player has requirements
            if(traversalType == ElevatorTraversalType.EXPERIENCE && player.totalExperience > 0) {
                return true;
            } else if(traversalType == ElevatorTraversalType.HUNGER && player.getHungerManager().getFoodLevel() > 0) {
                return true;
            } else if(traversalType == ElevatorTraversalType.NONE){
                return true;
            }
        }

        return false;
    }

    /***
     * Commits the effects of travel on the player
     * @param player Player to affect
     * @param traversalType Traversal type of world
     */
    public static void commitTravelOnPlayer(PlayerEntity player, ElevatorTraversalType traversalType) {
        // If is creative then we don't do anything
        if(traversalType == ElevatorTraversalType.NONE || player.isCreative()) {
            return;
        }

        // Affect the XP or hunger of the player
        if(traversalType == ElevatorTraversalType.EXPERIENCE) {
            player.addExperience(-1);
        } else if(traversalType == ElevatorTraversalType.HUNGER) {
            player.getHungerManager().setFoodLevel(player.getHungerManager().getFoodLevel() - 1);
        }
    }

    /***
     * Transport the player towards another elevator
     * @param player Player to transport
     * @param direction Direction to go in
     * @return ActionResult representing what happened
     */
    public static ActionResult transportPlayerInElevator(PlayerEntity player, Direction direction) {
        World world = player.world;

        Block potential_slab = world.getBlockState(player.getBlockPos()).getBlock();

        // Get block from either slab or block, slab is block beneath if we are on a full block so we just check
        Block block = potential_slab == Blocks.AIR ? world.getBlockState(player.getBlockPos().offset(Direction.DOWN)).getBlock() : potential_slab;

        // If block is elevator
        if(block instanceof ElevatorImpl) {
            if(!world.isClient) {
                // Find elevator in the direction we want to go, check the traversal type and that the player can travel
                BlockPos next = findElevatorInDirection(world, player.getBlockPos(), direction, block);
                ElevatorTraversalType traversalType = world.getGameRules().get(TabithasElevators.AFFECT_PLAYER_ON_TRAVERSAL).get();
                boolean canTravel = canPlayerTravel(player, traversalType);

                // If we found a block and the player can fit
                if(next != null && isAreaValidForPlayer(world, next) && canTravel) {
                    BlockState nextState = world.getBlockState(next);

                    // Check if the block is a bottom slab so we can change the height
                    boolean nextIsBottomSlab = nextState.getBlock() instanceof ElevatorSlabBlock &&
                            world.getBlockState(next).get(ElevatorSlabBlock.TYPE).equals(SlabType.BOTTOM);

                    // Play the sound of transport
                    world.playSound(null, player.getBlockPos(), direction == Direction.UP ? SoundEvents.UI_TOAST_IN : SoundEvents.UI_TOAST_OUT, SoundCategory.BLOCKS, 1f, 1.5f);

                    // If the traversal type has a sound then play it
                    if(traversalType.sound != null && !player.isCreative()) {
                        world.playSound(null, player.getBlockPos(), traversalType.sound, SoundCategory.BLOCKS, 0.5f, 1f);
                    }

                    // teleport the player and commit the effects
                    player.teleport(player.getX(), next.getY() + (nextIsBottomSlab ? 0.5f : 1f), player.getZ());
                    commitTravelOnPlayer(player, traversalType);

                    // FAIL so that the player doesn't jump
                    return ActionResult.FAIL;
                } else {

                    // If the player can't travel, check the types and figure out why we can't travel to say the correct message
                    if(!canTravel) {
                        if(traversalType == ElevatorTraversalType.EXPERIENCE) {
                            player.sendMessage(MutableText.of(new TranslatableTextContent("message.tabithas_elevators.notEnoughExperience")), true);
                            world.playSound(null, player.getBlockPos(), traversalType.sound, SoundCategory.BLOCKS, 0.2f, 0.7f);
                        } else if(traversalType == ElevatorTraversalType.HUNGER) {
                            player.sendMessage(MutableText.of(new TranslatableTextContent("message.tabithas_elevators.notFullEnough")), true);
                            world.playSound(null, player.getBlockPos(), traversalType.sound, SoundCategory.BLOCKS, 0.2f, 0.9f);
                        }
                    } else if(next == null) {
                        // If we can't find next elevator then tell the player
                        player.sendMessage(MutableText.of(new TranslatableTextContent("message.tabithas_elevators.cantFindElevator")), true);
                        world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_PLAYER_SMALL_FALL, SoundCategory.BLOCKS, 0.2f, 1f);
                    } else {
                        // If we can't fit then tell the player
                        player.sendMessage(MutableText.of(new TranslatableTextContent("message.tabithas_elevators.notEnoughSpace")), true);
                        world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_WOOL_STEP, SoundCategory.BLOCKS, 0.2f, 1f);
                    }
                }
            }
        }

        return ActionResult.PASS;
    }

    /***
     * Event for player jumping
     * @param player Player that jumped
     * @return Action result based on whether we can teleport
     */
    public static ActionResult onPlayerJump(PlayerEntity player) {
        return transportPlayerInElevator(player, Direction.UP);
    }

    /***
     * Event for player sneaking
     * @param player Player that sneaked
     * @return Action result based on whether we can teleport
     */
    public static ActionResult onPlayerSneak(PlayerEntity player) {
        return transportPlayerInElevator(player, Direction.DOWN);
    }
}
