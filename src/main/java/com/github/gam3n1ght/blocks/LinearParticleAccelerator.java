package com.github.gam3n1ght.blocks;

import javax.annotation.Nullable;

import com.github.gam3n1ght.blocks.tileentities.LinearParticleAcceleratorTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class LinearParticleAccelerator extends Block {

	public static String REGISTRY_NAME = "linear_particle_accelerator";
	
	public LinearParticleAccelerator() {
		super(Properties.create(Material.IRON)
				.sound(SoundType.METAL)
				.hardnessAndResistance(3.5f)
				.lightValue(24)
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
		return TileEntityType.Builder.create(LinearParticleAcceleratorTileEntity::new, ModBlocks.LINEAR_PARTICLE_ACCELERATOR)
				.build(null)
				.setRegistryName(REGISTRY_NAME);

	}

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public int getLightValue(BlockState state) {
        return state.get(BlockStateProperties.POWERED) ? super.getLightValue(state) : 0;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new LinearParticleAcceleratorTileEntity();
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        if (entity != null) {
            world.setBlockState(pos, state.with(BlockStateProperties.FACING, getFacingFromEntity(pos, entity)), 2);
        }
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
    	LOGGER.warn("!!!!! LinearParticleAccelerator onBlockActivated {}", state, pos.getX(), pos.getY(), pos.getZ());
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof INamedContainerProvider) {
                NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, tileEntity.getPos());
            } else {
                throw new IllegalStateException("Our named container provider is missing!");
            }
            LOGGER.warn("!!!!! LinearParticleAccelerator onBlockActivated RETURN TRUE");
            return true;
        }
        LOGGER.warn("!!!!! LinearParticleAccelerator onBlockActivated RETURN SUPER");
        return super.onBlockActivated(state, world, pos, player, hand, result);
    }

    public static Direction getFacingFromEntity(BlockPos clickedBlock, LivingEntity entity) {
        return Direction.getFacingFromVector((float) (entity.posX - clickedBlock.getX()), (float) (entity.posY - clickedBlock.getY()), (float) (entity.posZ - clickedBlock.getZ()));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING, BlockStateProperties.POWERED);
    }
}
