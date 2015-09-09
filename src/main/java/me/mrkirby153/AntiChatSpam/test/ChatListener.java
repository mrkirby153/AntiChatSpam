package me.mrkirby153.AntiChatSpam.test;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import me.mrkirby153.AntiChatSpam.regex.ChatHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class ChatListener {

    private Minecraft mc = null;
    @SubscribeEvent
    public void handleChat(ClientChatReceivedEvent event) {

    }

    public void addChatComponentMessage(IChatComponent p_146105_1_) {
        if (!ChatHandler.hasHandledChat(p_146105_1_))
            this.mc.ingameGUI.getChatGUI().printChatMessage(p_146105_1_);
    }
}
