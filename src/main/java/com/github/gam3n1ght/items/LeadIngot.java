package com.github.gam3n1ght.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;


public class LeadIngot extends Item {

	private static String REGISTRY_NAME = "lead_ingot";
	
	public LeadIngot() {
		super(new Item.Properties()
				.group(ItemGroup.MATERIALS)
				.maxStackSize(64)
				);
		setRegistryName(REGISTRY_NAME);
	}

}
