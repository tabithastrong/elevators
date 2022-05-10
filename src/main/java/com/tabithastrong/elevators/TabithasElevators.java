package com.tabithastrong.elevators;

import com.tabithastrong.elevators.block.ElevatorBlock;
import com.tabithastrong.elevators.block.ElevatorSlabBlock;
import com.tabithastrong.elevators.event.PlayerJumpCallback;
import com.tabithastrong.elevators.event.PlayerSneakCallback;
import com.tabithastrong.elevators.gamerule.ElevatorTraversalType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.EnumRule;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/***
 * Base class for the mod
 */
public class TabithasElevators implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("tabithas_elevators");

    // List of blocks and identifiers
    public static final Block[] ELEVATORS = new Block[16];
    public static final Block[] ELEVATOR_SLABS = new Block[16];
    public static final Identifier[] ELEVATOR_IDENTIFIERS = new Identifier[16];
    public static final Identifier[] ELEVATOR_SLAB_IDENTIFIERS = new Identifier[16];

    // Game rule used to determine how the player interacts with the elevators
    public static final GameRules.Key<EnumRule<ElevatorTraversalType>> AFFECT_PLAYER_ON_TRAVERSAL = GameRuleRegistry.register(
        "tabithas_elevators.elevatorTraversalType", GameRules.Category.PLAYER, GameRuleFactory.createEnumRule(ElevatorTraversalType.EXPERIENCE)
    );

    // Item group for all blocks in the mod
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier("tabithas_elevators", "item_group"), () -> {
        return new ItemStack(ELEVATORS[0], 1);
    });

    /***
     * Initialization function
     */
    @Override
    public void onInitialize() {

        /***
         * Loops through all the colours in the game, generates the blocks and items for each color
         */
        for(int i = 0; i < 16; i++) {
            DyeColor color = DyeColor.values()[i];

            ELEVATORS[i] = new ElevatorBlock(FabricBlockSettings.of(Material.WOOL, color.getMapColor()).sounds(BlockSoundGroup.WOOL).hardness(1f).resistance(1f), color);
            ELEVATOR_SLABS[i] = new ElevatorSlabBlock(FabricBlockSettings.of(Material.WOOL, color.getMapColor()).sounds(BlockSoundGroup.WOOL).hardness(1f).resistance(1f).allowsSpawning((state, world, pos, type) -> false), color);

            ELEVATOR_IDENTIFIERS[i] = new Identifier("tabithas_elevators", "elevator_" + color.asString());
            ELEVATOR_SLAB_IDENTIFIERS[i] = new Identifier("tabithas_elevators", "elevator_" + color.asString() + "_slab");

            Registry.register(Registry.BLOCK, ELEVATOR_IDENTIFIERS[i], ELEVATORS[i]);
            Registry.register(Registry.BLOCK, ELEVATOR_SLAB_IDENTIFIERS[i], ELEVATOR_SLABS[i]);
            Registry.register(Registry.ITEM, ELEVATOR_IDENTIFIERS[i], new BlockItem(ELEVATORS[i], new FabricItemSettings().group(ITEM_GROUP)));
            Registry.register(Registry.ITEM, ELEVATOR_SLAB_IDENTIFIERS[i], new BlockItem(ELEVATOR_SLABS[i], new FabricItemSettings().group(ITEM_GROUP)));
        }

        // Registers the events for the mixins
        PlayerJumpCallback.EVENT.register(TabithasElevatorsEvents::onPlayerJump);
        PlayerSneakCallback.EVENT.register(TabithasElevatorsEvents::onPlayerSneak);
    }
}
