package com.github.gam3n1ght.blocks.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.gam3n1ght.blocks.tileentities.LinearParticleAcceleratorTileEntity;
import static com.github.gam3n1ght.blocks.ModBlocks.LINEAR_PARTICLE_ACCELERATOR_CONTAINER;


public class LinearParticleAcceleratorContainer extends Container {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
    private LinearParticleAcceleratorTileEntity tileEntity;
//    private PlayerEntity playerEntity;
    private IItemHandler playerInventory;
    // These store cache values, used by the server to only update the client side tile entity when values have changed
 	private int[] cachedFields;	
    
 	// Define the size of each GUI Slot
 	private final static int SLOT_SPACE_SIZE = 18;
 	// Hotbar Slots and Location.
	// must assign a slot index to each of the slots used by the GUI.
	// For this container, we can see the furnace fuel, input, and output slots as well as the player inventory slots and the hotbar.
	// Each time we add a Slot to the container using addSlotToContainer(), it automatically increases the slotIndex, which means
	//  0 - 8   = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
	private final static int HOTBAR_SLOT_COUNT = 9;
	//  9 - 35  = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
	private final static int INVENTORY_SLOT_COUNT = 27;
	//  36 - 37 = fuel slots (tileEntity 0 - 1)
	//  38 - 39 = input slots (tileEntity 2 - 3)
	//  40      = output slot (tileEntity 4)
 	// Define where the first Hotbar Slot will render (x, y)
 	private final static int[] HOTBAR_SLOT_POS = new int[]{10,128};
 	// Define where the first Inventory Slot will render (x, y)
 	private final static int[] INVENTORY_SLOT_POS = new int[]{10,70};
 	// Define where each of the Fuel Slots will render (x,y),(x,y)
 	private final static int[][] FUEL_SLOT_POS = new int[][]{{10,47},{154,47}};
 	// Define where each of the Input Slots will render (x,y),(x,y)
 	private final static int[][] INPUT_SLOT_POS = new int[][]{{10,11},{154,11}};
 	// Define where each of the Output Slots will render (x,y)
 	private final static int[] OUTPUT_SLOT_POS = new int[]{82,11};
	
 	/**
 	 * Constructor
 	 * @param windowId
 	 * @param world
 	 * @param pos
 	 * @param playerInventory
 	 * @param player
 	 */
	public LinearParticleAcceleratorContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
		// Initialize Container
		super(LINEAR_PARTICLE_ACCELERATOR_CONTAINER, windowId);
		LOGGER.warn("!!!!! Container Construct");
		// Create ItemHandler from Player Inventory
		this.playerInventory = new InvWrapper(playerInventory);
		// Attach Tile Entity.
		this.tileEntity = (LinearParticleAcceleratorTileEntity) world.getTileEntity(pos);
		
		// Add Player Hotbar and Inventory to GUI
		initPlayerGui(player);
		
		// Create custom slots
		initMaterialSlots(world, pos);
	}
	
	/**
	 * Create Player Inventory and Hotbar Slots on the GUI
	 * @param player
	 * @return
	 */
	private void initPlayerGui(PlayerEntity player) {
		int slotIndex = 0;
		// Create Hotbar Slots
		slotIndex = this.addPlayerSlotRow(slotIndex, HOTBAR_SLOT_POS[0], HOTBAR_SLOT_POS[1], HOTBAR_SLOT_COUNT);
		// Create Inventory Slots
		int yPosition = INVENTORY_SLOT_POS[1];
		int rows = INVENTORY_SLOT_COUNT / HOTBAR_SLOT_COUNT;
		int cols = INVENTORY_SLOT_COUNT / rows;
		for (int row = 0; row < rows; row++) {
			LOGGER.warn("!!!!! initPlayerGui {} row {} of {}", slotIndex, row, rows);
			slotIndex = this.addPlayerSlotRow(slotIndex, INVENTORY_SLOT_POS[0], yPosition, cols);
			yPosition+=SLOT_SPACE_SIZE;
		}
	}
	
	/**
	 * Create Fuel, Input, and Output slots from our TileEntity
	 * @param world
	 * @param pos
	 */
	private void initMaterialSlots(World world, BlockPos pos) {
		this.tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
			// Our Slot Index should begin with the Fuel Slot.
			int slotIndex = 0;
			// Create the Fuel Slots
			for (int[] fuelpos : FUEL_SLOT_POS) {
				addSlot(new SlotFuel(this.tileEntity, slotIndex, fuelpos[0], fuelpos[1]));
				slotIndex++;
			}
			// Create Input Slots
			for (int[] inputpos : INPUT_SLOT_POS) {
				addSlot(new SlotInput(this.tileEntity, slotIndex, inputpos[0], inputpos[1]));
				slotIndex++;
			}
			// Create Output Slot
			addSlot(new SlotOutput(this.tileEntity, slotIndex, OUTPUT_SLOT_POS[0], OUTPUT_SLOT_POS[1]));
        });
	}
	
	/**
	 * Create {slotCount} number of slots along the X axis defined by {xPosition}
	 * @param slotIndex
	 * @param xPosition
	 * @param yPosition
	 * @param slotCount
	 * @return slotIndex
	 */
	private int addPlayerSlotRow(int slotIndex, int xPosition, int yPosition, int slotCount) {
		for (int x = 0; x < slotCount; x++) {
			addSlot(new SlotItemHandler(this.playerInventory, slotIndex, xPosition, yPosition));
			xPosition += SLOT_SPACE_SIZE;
			slotIndex++;
		}
		return slotIndex;
	}
	
	// Checks each tick to make sure the player is still able to access the inventory and if not closes the gui
	@Override
	public boolean canInteractWith(PlayerEntity player) {
		LOGGER.warn("--- Can Interact Player {}", player);
		LOGGER.warn("--- Can Interact Tile {}", this.tileEntity);
		return this.tileEntity.isUsableByPlayer(player);
	}
	
	// This is where you specify what happens when a player shift clicks a slot in the gui
	//  (when you shift click a slot in the TileEntity Inventory, it moves it to the first available position in the hotbar and/or
	//    player inventory.  When you you shift-click a hotbar or player inventory item, it moves it to the first available
	//    position in the TileEntity inventory - either input or fuel as appropriate for the item you clicked)
	// At the very least you must override this and return EMPTY_ITEM or the game will crash when the player shift clicks a slot
	// returns EMPTY_ITEM if the source slot is empty, or if none of the source slot items could be moved.
	//   otherwise, returns a copy of the source stack
	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int sourceSlotIndex) {
		Slot sourceSlot = (Slot)inventorySlots.get(sourceSlotIndex);
		if (sourceSlot == null || !sourceSlot.getHasStack()) return ItemStack.EMPTY;  //EMPTY_ITEM
		ItemStack sourceStack = sourceSlot.getStack();
		ItemStack copyOfSourceStack = sourceStack.copy();

//		// Check if the slot clicked is one of the vanilla container slots
//		if (sourceSlotIndex >= VANILLA_FIRST_SLOT_INDEX && sourceSlotIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
//			// This is a vanilla container slot so merge the stack into one of the furnace slots
//			// If the stack is smeltable try to merge merge the stack into the input slots
//			if (!TileInventoryFurnace.getSmeltingResultForItem(sourceStack).isEmpty()){  //isEmptyItem
//				if (!mergeItemStack(sourceStack, FIRST_INPUT_SLOT_INDEX, FIRST_INPUT_SLOT_INDEX + INPUT_SLOTS_COUNT, false)){
//					return ItemStack.EMPTY;  //EMPTY_ITEM;
//				}
//			}	else if (TileInventoryFurnace.getItemBurnTime(sourceStack) > 0) {
//				if (!mergeItemStack(sourceStack, FIRST_FUEL_SLOT_INDEX, FIRST_FUEL_SLOT_INDEX + FUEL_SLOTS_COUNT, true)) {
//					// Setting the boolean to true places the stack in the bottom slot first
//					return ItemStack.EMPTY;  //EMPTY_ITEM;
//				}
//			}	else {
//				return ItemStack.EMPTY;  //EMPTY_ITEM;
//			}
//		} else if (sourceSlotIndex >= FIRST_FUEL_SLOT_INDEX && sourceSlotIndex < FIRST_FUEL_SLOT_INDEX + FURNACE_SLOTS_COUNT) {
//			// This is a furnace slot so merge the stack into the players inventory: try the hotbar first and then the main inventory
//			//   because the main inventory slots are immediately after the hotbar slots, we can just merge with a single call
//			if (!mergeItemStack(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
//				return ItemStack.EMPTY;  //EMPTY_ITEM;
//			}
//		} else {
//			System.err.print("Invalid slotIndex:" + sourceSlotIndex);
//			return ItemStack.EMPTY;  //EMPTY_ITEM;
//		}
//
//		// If stack size == 0 (the entire stack was moved) set slot contents to null
//		if (sourceStack.getCount() == 0) {  //getStackSize()
//			sourceSlot.putStack(ItemStack.EMPTY);  // Empty Item
//		} else {
//			sourceSlot.onSlotChanged();
//		}

		sourceSlot.onTake(player, sourceStack);  // onPickupFromSlot()
		return copyOfSourceStack;
	}
	
	// This is where you check if any values have changed and if so send an update to any clients accessing this container
	// The container itemstacks are tested in Container.detectAndSendChanges, so we don't need to do that
	// We iterate through all of the TileEntity Fields to find any which have changed, and send them.
	// You don't have to use fields if you don't wish to; just manually match the ID in sendWindowProperty with the value in
	//   updateProgressBar()
	// The progress bar values are restricted to shorts.  If you have a larger value (eg int), it's not a good idea to try and split it
	//   up into two shorts because the progress bar values are sent independently, and unless you add synchronisation logic at the
	//   receiving side, your int value will be wrong until the second short arrives.  Use a custom packet instead.
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

//		boolean allFieldsHaveChanged = false;
//		boolean fieldHasChanged [] = new boolean[tileInventoryFurnace.getFieldCount()];
//		if (cachedFields == null) {
//			cachedFields = new int[tileInventoryFurnace.getFieldCount()];
//			allFieldsHaveChanged = true;
//		}
//		for (int i = 0; i < cachedFields.length; ++i) {
//			if (allFieldsHaveChanged || cachedFields[i] != tileInventoryFurnace.getField(i)) {
//				cachedFields[i] = tileInventoryFurnace.getField(i);
//				fieldHasChanged[i] = true;
//			}
//		}
//
//		// go through the list of listeners (players using this container) and update them if necessary
//		for (IContainerListener listener : this.listeners) {
//			for (int fieldID = 0; fieldID < tileInventoryFurnace.getFieldCount(); ++fieldID) {
//				if (fieldHasChanged[fieldID]) {
//					// Note that although sendWindowProperty takes 2 ints on a server these are truncated to shorts
//					listener.sendWindowProperty(this, fieldID, cachedFields[fieldID]);
//				}
//			}
//		}
	}
	
	// SlotFuel is a slot for fuel items
	public class SlotFuel extends Slot {
		public SlotFuel(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}

		// if this function returns false, the player won't be able to insert the given item into this slot
		@Override
		public boolean isItemValid(ItemStack stack) {
			return LinearParticleAcceleratorTileEntity.isValidFuelItem(stack);
		}
	}

	// SlotInput is a slot for input items
	public class SlotInput extends Slot {
		public SlotInput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}

		// if this function returns false, the player won't be able to insert the given item into this slot
		@Override
		public boolean isItemValid(ItemStack stack) {
			return LinearParticleAcceleratorTileEntity.isValidInputItem(stack);
		}
	}

	// SlotOutput is a slot that will not accept any items
	public class SlotOutput extends Slot {
		public SlotOutput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}

		// if this function returns false, the player won't be able to insert the given item into this slot
		@Override
		public boolean isItemValid(ItemStack stack) {
			return LinearParticleAcceleratorTileEntity.isValidOutputItem(stack);
		}
	}
}






























/* Deprecate Below */
/*
import com.github.gam3n1ght.tools.CustomEnergyStorage;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.AbstractFurnaceContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import static com.github.gam3n1ght.blocks.ModBlocks.LINEAR_PARTICLE_ACCELERATOR;
import static com.github.gam3n1ght.blocks.ModBlocks.LINEAR_PARTICLE_ACCELERATOR_CONTAINER;

public class LinearParticleAcceleratorContainer extends Container {
	
    private TileEntity tileEntity;
    private PlayerEntity playerEntity;
    private IItemHandler playerInventory;
	
 // -------------------------------
//    public FurnaceContainer(int p_i50082_1_, PlayerInventory p_i50082_2_) {
//        super(ContainerType.FURNACE, IRecipeType.SMELTING, p_i50082_1_, p_i50082_2_);
//     }
//
//     public FurnaceContainer(int p_i50083_1_, PlayerInventory p_i50083_2_, IInventory p_i50083_3_, IIntArray p_i50083_4_) {
//        super(ContainerType.FURNACE, IRecipeType.SMELTING, p_i50083_1_, p_i50083_2_, p_i50083_3_, p_i50083_4_);
//     }
     // -------------------------------
     
    public LinearParticleAcceleratorContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super(LINEAR_PARTICLE_ACCELERATOR_CONTAINER, windowId);

        // Render Item Slots
        tileEntity = world.getTileEntity(pos);
        tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            addSlot(new SlotItemHandler(h, 0, 64, 24));
        });
        
        // Render Player Inventory in GUI
        this.playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);
        layoutPlayerInventorySlots(10, 70);

        // @TODO: Figure out how to use "energy/power"
        //func_216958_a(new IntReferenceHolder() {
        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return getEnergy();
            }

            @Override
            public void set(int value) {
                tileEntity.getCapability(CapabilityEnergy.ENERGY).ifPresent(h -> ((CustomEnergyStorage)h).setEnergy(value));
            }
        });
    }
	
    public int getEnergy() {
        return tileEntity.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerEntity, LINEAR_PARTICLE_ACCELERATOR);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemstack = stack.copy();
            if (index == 0) {
                if (!this.mergeItemStack(stack, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(stack, itemstack);
            } else {
                if (stack.getItem() == Items.DIAMOND) {
                    if (!this.mergeItemStack(stack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 28) {
                    if (!this.mergeItemStack(stack, 28, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 37 && !this.mergeItemStack(stack, 1, 28, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, stack);
        }

        return itemstack;
    }

    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0 ; i < amount ; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0 ; j < verAmount ; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }
    
    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        // Player inventory (9x3 slots)
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Furnace inventory (9x1 slots)
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    private void layout
}
*/