package me.mrkirby153.AntiChatSpam;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import me.mrkirby153.AntiChatSpam.network.PacketAntiSpam;
import me.mrkirby153.AntiChatSpam.test.ChatListener;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = "AntiChatSpam", name = "Anti Chat Spam", version = "@VERSION@")
public class AntiChatSpam {

    public static SimpleNetworkWrapper network;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        network = NetworkRegistry.INSTANCE.newSimpleChannel("AntiChatSpam");
        network.registerMessage(PacketAntiSpam.Handler.class, PacketAntiSpam.class, 0, Side.CLIENT);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        ChatListener cl = new ChatListener();
        FMLCommonHandler.instance().bus().register(cl);
        MinecraftForge.EVENT_BUS.register(cl);
    }

}
