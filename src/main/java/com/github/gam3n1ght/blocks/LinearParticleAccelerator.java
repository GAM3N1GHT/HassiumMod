package com.github.gam3n1ght.blocks;

import com.github.gam3n1ght.blocks.tiles.LinearParticleAcceleratorTile;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;

public class LinearParticleAccelerator extends Block {

	public static String REGISTRY_NAME = "linear_particle_accelerator";
	
	public LinearParticleAccelerator() {
		super(Properties.create(Material.IRON)
				.sound(SoundType.METAL)
				.hardnessAndResistance(2.0f)
//				.lightValue(64)
				);
		setRegistryName(REGISTRY_NAME);
	}
	
	public static BlockItem getBlockItem() {
		BlockItem i = new BlockItem(
				ModBlocks.LINEAR_PARTICLE_ACCELERATOR,
				new Item.Properties().group(ItemGroup.DECORATIONS).maxStackSize(1));
		i.setRegistryName(REGISTRY_NAME);
		return i;
	}
	
	public static TileEntityType<?> getTile() {
		return TileEntityType.Builder.create(LinearParticleAcceleratorTile::new, ModBlocks.LINEAR_PARTICLE_ACCELERATOR)
				.build(null)
				.setRegistryName(REGISTRY_NAME);

	}
}
