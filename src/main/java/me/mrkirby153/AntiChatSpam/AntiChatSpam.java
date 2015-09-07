package me.mrkirby153.AntiChatSpam;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import me.mrkirby153.AntiChatSpam.network.PacketAntiSpam;

@Mod(modid = "AntiChatSpam", name = "Anti Chat Spam", version = "@VERSION@")
public class AntiChatSpam {

    public static SimpleNetworkWrapper network;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        network = NetworkRegistry.INSTANCE.newSimpleChannel("AntiChatSpam");
        network.registerMessage(PacketAntiSpam.Handler.class, PacketAntiSpam.class, 0, Side.CLIENT);
    }
}
