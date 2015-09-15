package me.mrkirby153.AntiChatSpam;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import me.mrkirby153.AntiChatSpam.listener.ChatListener;
import me.mrkirby153.AntiChatSpam.regex.ChatHandler;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = "AntiChatSpam", name = "Anti Chat Spam", version = "@VERSION@")
public class AntiChatSpam {

    public static SimpleNetworkWrapper network;

    @Mod.Instance("AntiChatSpam")
    public static AntiChatSpam instance;

    public Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger = LogManager.getLogger("AntiChatSpam");
        network = NetworkRegistry.INSTANCE.newSimpleChannel("AntiChatSpam");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        ChatListener cl = new ChatListener();
        FMLCommonHandler.instance().bus().register(cl);
        MinecraftForge.EVENT_BUS.register(cl);
        ChatHandler.load();
    }
}
