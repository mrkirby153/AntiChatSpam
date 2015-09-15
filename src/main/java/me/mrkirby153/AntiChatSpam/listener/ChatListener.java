package me.mrkirby153.AntiChatSpam.listener;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import me.mrkirby153.AntiChatSpam.regex.ChatHandler;
import me.mrkirby153.AntiChatSpam.regex.CommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.ServerChatEvent;

import java.util.Random;

public class ChatListener {

    private static final Random random = new Random();

    @SubscribeEvent
    public void handleChat(ClientChatReceivedEvent event) {
        event.setCanceled(ChatHandler.hasHandledChat(event.message));
    }

    @SubscribeEvent
    public void commandEvent(ServerChatEvent event){
        if(event.message.startsWith("!acs")){
            event.setCanceled(true);
            String message = event.message.substring(4, event.message.length());
            String[] args = message.split(" ");
            String[] trimmedArgs = new String[args.length - 1];
            System.arraycopy(args, 1, trimmedArgs, 0, args.length - 1);
            CommandHandler.run(event.player, trimmedArgs);
        }
    }
}
