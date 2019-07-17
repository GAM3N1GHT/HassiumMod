package com.github.gam3n1ght.blocks.tileentities;

import com.github.gam3n1ght.Config;
import com.github.gam3n1ght.blocks.containers.LinearParticleAcceleratorContainer;
import com.github.gam3n1ght.tools.CustomEnergyStorage;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.github.gam3n1ght.blocks.ModBlocks.LINEAR_PARTICLE_ACCELERATOR_TILE;


public class LinearParticleAcceleratorTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider, IInventory {

	public final static int FUEL_SLOT_COUNT = 2;
	public final static int INPUT_SLOT_COUNT = 2;
	public final static int OUTPUT_SLOT_COUNT = 1;
	private ItemStack[] itemStacks = new ItemStack[FUEL_SLOT_COUNT + INPUT_SLOT_COUNT + OUTPUT_SLOT_COUNT];
    private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);
    private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
    private int counter;
 	
    public LinearParticleAcceleratorTileEntity() {
    	super(LINEAR_PARTICLE_ACCELERATOR_TILE);
    	clear();
    }
    
    /**
     * Get Fuel Slot Item
     * @param index Allowed Values: 0, 1
     * @return
     */
    public ItemStack getFuelSlotItem(int index) {
    	if (index > FUEL_SLOT_COUNT-1 || index < 0) {
    		return null; // out of bounds
    	}
    	return index < this.itemStacks.length ? this.itemStacks[index] : null;
    }
    
    /**
     * Get Input Slot Item
     * @param index Allowed Values: 0, 1
     * @return
     */
    public ItemStack getInputSlotItem(int index) {
    	if (index > INPUT_SLOT_COUNT-1 || index < 0) {
    		return null; // out of bounds
    	}
    	int actualIndex = FUEL_SLOT_COUNT + index;
    	return actualIndex < this.itemStacks.length ? this.itemStacks[actualIndex] : null;
    }
    
    /**
     * Get Output Slot Item
     * @return
     */
    public ItemStack getOutputSlotItem() {
    	int index = INPUT_SLOT_COUNT + FUEL_SLOT_COUNT;
    	return index < this.itemStacks.length ? this.itemStacks[index] : null;
    }
    
    /** @TODO: See https://github.com/TheGreyGhost/MinecraftByExample/blob/master/src/main/java/minecraftbyexample/mbe31_inventory_furnace/TileInventoryFurnace.java **/
	// Return true if the given player is able to use this block. In this case it checks that
	// 1) the world tileentity hasn't been replaced in the meantime, and
	// 2) the player isn't too far away from the centre of the block
	public boolean isUsableByPlayer(PlayerEntity player) {
		if (this.world.getTileEntity(this.pos) != this) return false;
		final double X_CENTRE_OFFSET = 0.5;
		final double Y_CENTRE_OFFSET = 0.5;
		final double Z_CENTRE_OFFSET = 0.5;
		final double MAXIMUM_DISTANCE_SQ = 8.0 * 8.0;
		return player.getDistanceSq(pos.getX() + X_CENTRE_OFFSET, pos.getY() + Y_CENTRE_OFFSET, pos.getZ() + Z_CENTRE_OFFSET) < MAXIMUM_DISTANCE_SQ;
	}
	
	/**
	 * Is {stack} a valid Fuel Item?
	 * @param stack
	 * @return
	 */
	public static boolean isValidFuelItem(ItemStack stack)
	{
        return FurnaceTileEntity.isFuel(stack) || stack.getItem() == Items.BUCKET;

	}
	
	/**
	 * Is {stack} a valid Input/Recipe Item?
	 * @param stack
	 * @return
	 */
	public static boolean isValidInputItem(ItemStack stack)
	{
		return true;
	}
	
	/**
	 * Don't allow anything to be put in the Output slot
	 * @param stack
	 * @return
	 */
	public static boolean isValidOutputItem(ItemStack stack)
	{
		return false;
	}
	
    @Override
    public void tick() {
    	return;
//        if (world.isRemote) {
//            return;
//        }
//
//        if (counter > 0) {
//            counter--;
//            if (counter <= 0) {
//                energy.ifPresent(e -> ((CustomEnergyStorage) e).addEnergy(Config.LINEAR_PARTICLE_ACCELERATOR_GENERATE.get()));
//            }
//            markDirty();
//        }
//
//        if (counter <= 0) {
//            handler.ifPresent(h -> {
//                ItemStack stack = h.getStackInSlot(0);
//                if (stack.getItem() == Items.DIAMOND) {
//                    h.extractItem(0, 1, false);
//                    counter = Config.LINEAR_PARTICLE_ACCELERATOR_TICKS.get();
//                    markDirty();
//                }
//            });
//        }
//
//        BlockState blockState = world.getBlockState(pos);
//        if (blockState.get(BlockStateProperties.POWERED) != counter > 0) {
//            world.setBlockState(pos, blockState.with(BlockStateProperties.POWERED, counter > 0), 3);
//        }
//
//        sendOutPower();
    }

    private void sendOutPower() {
        energy.ifPresent(energy -> {
            AtomicInteger capacity = new AtomicInteger(energy.getEnergyStored());
            if (capacity.get() > 0) {
                for (Direction direction : Direction.values()) {
                    TileEntity te = world.getTileEntity(pos.offset(direction));
                    if (te != null) {
                        boolean doContinue = te.getCapability(CapabilityEnergy.ENERGY, direction).map(handler -> {
                                    if (handler.canReceive()) {
                                        int received = handler.receiveEnergy(Math.min(capacity.get(), Config.LINEAR_PARTICLE_ACCELERATOR_SEND.get()), false);
                                        capacity.addAndGet(-received);
                                        ((CustomEnergyStorage) energy).consumeEnergy(received);
                                        markDirty();
                                        return capacity.get() > 0;
                                    } else {
                                        return true;
                                    }
                                }
                        ).orElse(true);
                        if (!doContinue) {
                            return;
                        }
                    }
                }
            }
        });
    }

    @Override
    public void read(CompoundNBT tag) {
        CompoundNBT invTag = tag.getCompound("inv");
        handler.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
        CompoundNBT energyTag = tag.getCompound("energy");
        energy.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(energyTag));

        counter = tag.getInt("counter");
        super.read(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        handler.ifPresent(h -> {
            CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            tag.put("inv", compound);
        });
        energy.ifPresent(h -> {
            CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            tag.put("energy", compound);
        });

        tag.putInt("counter", counter);
        return super.write(tag);
    }

    private IItemHandler createHandler() {
        return new ItemStackHandler(1) {

            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return stack.getItem() == Items.DIAMOND;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (stack.getItem() != Items.DIAMOND) {
                    return stack;
                }
                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    private IEnergyStorage createEnergy() {
        return new CustomEnergyStorage(Config.LINEAR_PARTICLE_ACCELERATOR_MAXPOWER.get(), 0);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        if (cap == CapabilityEnergy.ENERGY) {
            return energy.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(getType().getRegistryName().getPath());
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
//    	return new LinearParticleAcceleratorContainer(i, playerInventory, playerEntity);
        return new LinearParticleAcceleratorContainer(i, world, pos, playerInventory, playerEntity);
    }

	@Override
	public void clear() {
		Arrays.fill(itemStacks, ItemStack.EMPTY);
	}

	@Override
	public int getSizeInventory() {
		return itemStacks.length;
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : itemStacks) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return itemStacks[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack itemStackInSlot = getStackInSlot(index);
		if (itemStackInSlot.isEmpty()) return ItemStack.EMPTY;

		ItemStack itemStackRemoved;
		if (itemStackInSlot.getCount() <= count) {
			itemStackRemoved = itemStackInSlot;
			setInventorySlotContents(index, ItemStack.EMPTY);
		} else {
			itemStackRemoved = itemStackInSlot.split(count);
			if (itemStackInSlot.getCount() == 0) { //getStackSize
				setInventorySlotContents(index, ItemStack.EMPTY);
			}
		}
		markDirty();
		return itemStackRemoved;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack itemStack = getStackInSlot(index);
		if (!itemStack.isEmpty()) setInventorySlotContents(index, ItemStack.EMPTY);
		return itemStack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		itemStacks[index] = stack;
		if (!stack.isEmpty() && stack.getCount() > getInventoryStackLimit()) {
			stack.setCount(getInventoryStackLimit());
		}
		markDirty();
	}
    
}
