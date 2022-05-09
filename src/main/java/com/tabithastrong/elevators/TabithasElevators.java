package com.tabithastrong.elevators;

import com.tabithastrong.elevators.block.ElevatorBlock;
import com.tabithastrong.elevators.block.ElevatorSlabBlock;
import com.tabithastrong.elevators.event.PlayerJumpCallback;
import com.tabithastrong.elevators.event.PlayerSneakCallback;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class TabithasElevators implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("tabithas_elevators");

    public static final Block[] ELEVATORS = new Block[16];
    public static final Block[] ELEVATOR_SLABS = new Block[16];
    public static final Identifier[] ELEVATOR_IDENTIFIERS = new Identifier[16];
    public static final Identifier[] ELEVATOR_SLAB_IDENTIFIERS = new Identifier[16];

    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier("tabithas_elevators", "item_group"), () -> {
        return new ItemStack(ELEVATORS[0], 1);
    });

    @Override
    public void onInitialize() {
        for(int i = 0; i < 16; i++) {
            DyeColor color = DyeColor.values()[i];
            ELEVATORS[i] = new ElevatorBlock(FabricBlockSettings.of(Material.WOOL, color.getMapColor()).sounds(BlockSoundGroup.WOOL).hardness(1f).resistance(1f), color);
            ELEVATOR_SLABS[i] = new ElevatorSlabBlock(FabricBlockSettings.of(Material.WOOL, color.getMapColor()).sounds(BlockSoundGroup.WOOL).hardness(1f).resistance(1f), color);

            ELEVATOR_IDENTIFIERS[i] = new Identifier("tabithas_elevators", "elevator_" + color.asString());
            ELEVATOR_SLAB_IDENTIFIERS[i] = new Identifier("tabithas_elevators", "elevator_" + color.asString() + "_slab");

            Registry.register(Registry.BLOCK, ELEVATOR_IDENTIFIERS[i], ELEVATORS[i]);
            Registry.register(Registry.BLOCK, ELEVATOR_SLAB_IDENTIFIERS[i], ELEVATOR_SLABS[i]);
            Registry.register(Registry.ITEM, ELEVATOR_IDENTIFIERS[i], new BlockItem(ELEVATORS[i], new FabricItemSettings().group(ITEM_GROUP)));
            Registry.register(Registry.ITEM, ELEVATOR_SLAB_IDENTIFIERS[i], new BlockItem(ELEVATOR_SLABS[i], new FabricItemSettings().group(ITEM_GROUP)));
        }

        PlayerJumpCallback.EVENT.register(TabithasElevatorsEvents::onPlayerJump);
        PlayerSneakCallback.EVENT.register(TabithasElevatorsEvents::onPlayerSneak);
    }
}
