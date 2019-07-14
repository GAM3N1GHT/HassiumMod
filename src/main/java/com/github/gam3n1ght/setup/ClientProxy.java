package com.github.gam3n1ght.setup;

import com.github.gam3n1ght.blocks.screens.LinearParticleAcceleratorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ClientProxy implements IProxy {
	
    @Override
    public void init() {
        ScreenManager.registerFactory(com.github.gam3n1ght.blocks.ModBlocks.LINEAR_PARTICLE_ACCELERATOR_CONTAINER, LinearParticleAcceleratorScreen::new);
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getInstance().world;
    }

    @Override
    public PlayerEntity getClientPlayer() {
        return Minecraft.getInstance().player;
    }
    
}
