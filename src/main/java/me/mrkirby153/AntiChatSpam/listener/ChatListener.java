package me.mrkirby153.AntiChatSpam.listener;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import me.mrkirby153.AntiChatSpam.regex.ChatHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.util.Random;

public class ChatListener {

    private static final Random random = new Random();

    @SubscribeEvent
    public void handleChat(ClientChatReceivedEvent event) {
        event.setCanceled(ChatHandler.hasHandledChat(event.message));
    }

    @SubscribeEvent
    public void chat(TickEvent.ClientTickEvent event) {
        if (event.side == Side.CLIENT) {
            if (event.phase == TickEvent.Phase.START) {
                if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().thePlayer != null)
                    Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("Test "+random.nextInt(Integer.MAX_VALUE)));
            }
        }
    }
}
