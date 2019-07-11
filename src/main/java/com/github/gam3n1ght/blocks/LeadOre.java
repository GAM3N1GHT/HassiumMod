package com.github.gam3n1ght.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class LeadOre extends Block {

	public int VEIN_SIZE = 12;
	private static String REGISTRY_NAME = "lead_ore";
	
	public LeadOre() {
		super(Properties.create(Material.IRON)
				.sound(SoundType.METAL)
				.hardnessAndResistance(2.0f)
//				.lightValue(64)
				);
		setRegistryName(REGISTRY_NAME);
	}
	
	public static BlockItem getBlockItem() {
		BlockItem i = new BlockItem(ModBlocks.LEAD_ORE,
				new Item.Properties()
				.group(ItemGroup.BUILDING_BLOCKS)
				.maxStackSize(64));
		i.setRegistryName(REGISTRY_NAME);
		return i;
	}
}
