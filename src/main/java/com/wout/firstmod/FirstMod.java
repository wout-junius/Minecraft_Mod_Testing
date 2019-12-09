package com.wout.firstmod;

import com.wout.firstmod.blocks.BeefBlock;
import com.wout.firstmod.blocks.CookedBeefBlock;
import com.wout.firstmod.blocks.ModBlocks;
import com.wout.firstmod.setup.ClientProxy;
import com.wout.firstmod.setup.IProxy;
import com.wout.firstmod.setup.ServerProxy;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("firstmod")
public class FirstMod {
    private static final Logger LOGGER = LogManager.getLogger();

    public FirstMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

    }

    private void setup(final FMLCommonSetupEvent event) {

    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
            event.getRegistry().register(new BeefBlock());
            event.getRegistry().register(new CookedBeefBlock());
        }

        @SubscribeEvent
        public static void onItemRegistry(final RegistryEvent.Register<Item> event) {
            event.getRegistry().register(new BlockItem(ModBlocks.BEEFBLOCK, new Item.Properties()).setRegistryName("beefblock"));
            event.getRegistry().register(new BlockItem(ModBlocks.COOKEDBEEFBLOCK, new Item.Properties()).setRegistryName("cookedbeefblock"));
        }
    }
}
