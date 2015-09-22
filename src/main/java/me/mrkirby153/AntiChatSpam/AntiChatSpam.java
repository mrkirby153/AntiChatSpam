package me.mrkirby153.AntiChatSpam;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import me.mrkirby153.AntiChatSpam.listener.ChatListener;
import me.mrkirby153.AntiChatSpam.reference.Strings;
import me.mrkirby153.AntiChatSpam.regex.ChatHandler;
import me.mrkirby153.AntiChatSpam.version.VersionChecker;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Strings.MODID, name = Strings.MODNAME, version = Strings.VERSION)
public class AntiChatSpam {

    public static SimpleNetworkWrapper network;

    @Mod.Instance("AntiChatSpam")
    public static AntiChatSpam instance;

    public static Logger logger;
    private VersionChecker checker;
    public static boolean isClient = true;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        if(event.getSide() == Side.SERVER) {
            logger.info("Wrong side SERVER, AntiChatSpam not loading");
            isClient = false;
        }
        logger = LogManager.getLogger("AntiChatSpam");
        network = NetworkRegistry.INSTANCE.newSimpleChannel("AntiChatSpam");
        checker = new VersionChecker();
        if(!MinecraftForge.MC_VERSION.equals(Strings.MC_VERSION))
            VersionChecker.wrongMCVersion = true;
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        if(!isClient)
            return;
        ChatListener cl = new ChatListener();
        FMLCommonHandler.instance().bus().register(cl);
        MinecraftForge.EVENT_BUS.register(cl);
        ChatHandler.load();
        checker.init();
    }
}
