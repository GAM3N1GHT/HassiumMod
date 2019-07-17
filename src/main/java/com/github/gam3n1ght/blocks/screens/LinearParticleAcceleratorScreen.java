package com.github.gam3n1ght.blocks.screens;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.gam3n1ght.HassiumMod;
import com.github.gam3n1ght.blocks.containers.LinearParticleAcceleratorContainer;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class LinearParticleAcceleratorScreen extends ContainerScreen<LinearParticleAcceleratorContainer> {

	private static final Logger LOGGER = LogManager.getLogger();

	private ResourceLocation GUI = new ResourceLocation(HassiumMod.MODID, "textures/gui/linear_particle_accelerator_gui.png");
	
	public LinearParticleAcceleratorScreen(LinearParticleAcceleratorContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
    	LOGGER.warn("!!!!! LinearParticleAcceleratorScreen render {} {} {}", mouseX, mouseY, partialTicks);
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    	LOGGER.warn("!!!!! LinearParticleAcceleratorScreen drawGuiContainerForegroundLayer {} {}", mouseX, mouseY);
//        drawString(Minecraft.getInstance().fontRenderer, "Energy: " + container.getEnergy(), 10, 10, 0xffffff);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    	LOGGER.warn("!!!!! LinearParticleAcceleratorScreen drawGuiContainerBackgroundLayer {} {} {}", mouseX, mouseY, partialTicks);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(relX, relY, 0, 0, this.xSize, this.ySize);
    }

}
