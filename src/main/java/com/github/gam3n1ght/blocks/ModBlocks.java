package com.github.gam3n1ght.blocks;

import com.github.gam3n1ght.blocks.containers.LinearParticleAcceleratorContainer;
import com.github.gam3n1ght.blocks.tiles.LinearParticleAcceleratorTile;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class ModBlocks {

	@ObjectHolder("hassiummod:lead_ore")
	public static LeadOre LEAD_ORE;
	
	@ObjectHolder("hassiummod:copper_ore")
	public static CopperOre COPPER_ORE;
	
	@ObjectHolder("hassiummod:linear_particle_accelerator")
	public static LinearParticleAccelerator LINEAR_PARTICLE_ACCELERATOR;
	
    @ObjectHolder("hassiummod:linear_particle_accelerator")
    public static TileEntityType<LinearParticleAcceleratorTile> LINEAR_PARTICLE_ACCELERATOR_TILE;
    
	@ObjectHolder("hassiummod:linear_particle_accelerator")
	public static ContainerType<LinearParticleAcceleratorContainer> LINEAR_PARTICLE_ACCELERATOR_CONTAINER;
	
}
