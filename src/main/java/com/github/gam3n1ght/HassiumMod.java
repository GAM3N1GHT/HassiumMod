package com.github.gam3n1ght;

import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.github.gam3n1ght.blocks.CopperOre;
import com.github.gam3n1ght.blocks.LeadOre;
import com.github.gam3n1ght.blocks.LinearParticleAccelerator;
import com.github.gam3n1ght.blocks.containers.LinearParticleAcceleratorContainer;
import com.github.gam3n1ght.init.BiomeGen;
import com.github.gam3n1ght.items.CopperIngot;
import com.github.gam3n1ght.items.LeadIngot;
import com.github.gam3n1ght.setup.ClientProxy;
import com.github.gam3n1ght.setup.IProxy;
//import com.github.gam3n1ght.setup.ModSetup;
import com.github.gam3n1ght.setup.ServerProxy;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(HassiumMod.MODID)
public class HassiumMod
{
	public static final String MODID = "hassiummod";
	public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());
//	public static ModSetup setup = new ModSetup();
    private static final Logger LOGGER = LogManager.getLogger();

    public HassiumMod() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        LOGGER.info("Loading Configs from {}", FMLPaths.CONFIGDIR.get());
        Config.loadConfig(Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve("hassiummod-client.toml"));
        Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("hassiummod-common.toml"));
        
//        // Register the enqueueIMC method for modloading
//        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
//        // Register the processIMC method for modloading
//        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
//        // Register the doClientStuff method for modloading
//        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
    	LOGGER.info("Initialize Worlds and Biomes");
    	// Setup Worlds and Biomes.
        BiomeGen.addFeaturesToBiomes();
//    	setup.init();
    	LOGGER.info("Initialize Proxy");
        proxy.init();
    }

//    private void doClientStuff(final FMLClientSetupEvent event) {
//        // do something that can only be done on the client
//        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
//    }
//
//    private void enqueueIMC(final InterModEnqueueEvent event)
//    {
//        // some example code to dispatch IMC to another mod
//        InterModComms.sendTo(MODID, "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
//    }
//
//    private void processIMC(final InterModProcessEvent event)
//    {
//        // some example code to receive and process InterModComms from other mods
//        LOGGER.info("Got IMC {}", event.getIMCStream().
//                map(m->m.getMessageSupplier().get()).
//                collect(Collectors.toList()));
//    }


    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
    	
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
            // Be sure to register your Blocks here to make them available in the game.
            event.getRegistry().register(new LeadOre());
            event.getRegistry().register(new CopperOre());
            event.getRegistry().register(new LinearParticleAccelerator());
        }
        
        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
        	// Be sure to register your Items here to make them available in the game.
        	// Block Items
        	event.getRegistry().register(LeadOre.getBlockItem());
        	event.getRegistry().register(CopperOre.getBlockItem());
        	event.getRegistry().register(LinearParticleAccelerator.getBlockItem());
        	// Items
        	event.getRegistry().register(new LeadIngot());
        	event.getRegistry().register(new CopperIngot());
        }

        @SubscribeEvent
        public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
            event.getRegistry().register(
//            		IForgeContainerType.create((IContainerFactory<LinearParticleAcceleratorContainer>) (windowId, inv, data) -> {
        			IForgeContainerType.create((windowId, inv, data) -> {
            			BlockPos pos = data.readBlockPos();
            			return new LinearParticleAcceleratorContainer(
            				windowId,
            				HassiumMod.proxy.getClientWorld(),
            				pos,
            				inv,
            				HassiumMod.proxy.getClientPlayer());
            		}).setRegistryName(LinearParticleAccelerator.REGISTRY_NAME));
        }
        
        @SubscribeEvent
        public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
        	event.getRegistry().register(LinearParticleAccelerator.getTile());
        }
    }
}
