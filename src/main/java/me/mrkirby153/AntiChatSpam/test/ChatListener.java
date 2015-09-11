package me.mrkirby153.AntiChatSpam.test;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import me.mrkirby153.AntiChatSpam.regex.ChatHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class ChatListener {

    private Minecraft mc = null;

    @SubscribeEvent
    public void handleChat(ClientChatReceivedEvent event) {

    }

    @SubscribeEvent
    public void chat(TickEvent.ClientTickEvent event) {
        if (event.side == Side.CLIENT) {
            if (event.phase == TickEvent.Phase.START) {
                if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().thePlayer != null)
                    Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("THIS IS A TEST!"));
            }
        }
    }

    public void addChatComponentMessage(IChatComponent p_146105_1_) {
        if (ChatHandler.hasHandledChat(p_146105_1_)) {
            System.out.println("THIS IS A TEST OF THINGS!");
            return;
        }
    }
}
