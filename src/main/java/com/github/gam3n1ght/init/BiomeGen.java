package com.github.gam3n1ght.init;


import com.github.gam3n1ght.HassiumMod;
import com.github.gam3n1ght.blocks.ModBlocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.ForgeRegistries;


@ObjectHolder(HassiumMod.MODID)
public class BiomeGen {
	
	public static void addFeaturesToBiomes() {
		for (final Biome biome : ForgeRegistries.BIOMES) {
			// Lead Ore
			biome.addFeature(
				GenerationStage.Decoration.UNDERGROUND_ORES,
				Biome.createDecoratedFeature(
					Feature.ORE,
					new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ModBlocks.LEAD_ORE.getDefaultState(), ModBlocks.LEAD_ORE.VEIN_SIZE),
					Placement.COUNT_RANGE,
					new CountRangeConfig(ModBlocks.LEAD_ORE.VEIN_SIZE, 10, 0, 100))); // Vein/Chunk Count, MinHeight, MaxHeightBase, MaxHeight
			// Copper Ore
			biome.addFeature(
					GenerationStage.Decoration.UNDERGROUND_ORES,
					Biome.createDecoratedFeature(
						Feature.ORE,
						new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ModBlocks.COPPER_ORE.getDefaultState(), ModBlocks.COPPER_ORE.VEIN_SIZE),
						Placement.COUNT_RANGE,
						new CountRangeConfig(ModBlocks.COPPER_ORE.VEIN_SIZE, 10, 0, 100))); // Vein/Chunk Count, MinHeight, MaxHeightBase, MaxHeight
		}
	}
	
}
