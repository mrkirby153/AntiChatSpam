package me.mrkirby153.AntiChatSpam.regex;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

import java.util.Arrays;
import java.util.Map;

public class CommandHandler {

    private static int lastEndId;

    public static void run(EntityPlayer player, String[] args) {
        System.out.println(Arrays.toString(args));
        if (args.length == 0) {
            // List every message
            printFilters();
            return;
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("delete")) {
                int id = Integer.parseInt(args[1]);
                ChatHandler.remove(id);
                printFilters();
            }
        }
        if (args.length >= 2) {
            if (args[0].equalsIgnoreCase("add")) {
                String regexToAdd = "";
                for (int i = 1; i <= args.length - 1; i++) {
                    regexToAdd += args[i] + " ";
                }
                regexToAdd = regexToAdd.trim();
                ChatHandler.add(regexToAdd);
                printFilters();
            }
        }
        if (args.length >= 3) {
            if (args[0].equalsIgnoreCase("edit")) {
                int messageId = Integer.parseInt(args[1]);
                String newMessage = "";
                for (int i = 2; i <= args.length - 1; i++) {
                    newMessage += args[i] + " ";
                }
                newMessage = newMessage.trim();
                ChatHandler.edit(messageId, newMessage);
                printFilters();
            }
        }
    }


    private static void printFilters() {
        int startId = 15000;
        for (int i = 0; i < (lastEndId - startId) + 1; i++) {
            int msgNum = startId++;
            Minecraft.getMinecraft().ingameGUI.getChatGUI().deleteChatLine(msgNum);
        }
        startId = 15000;
        if (ChatHandler.filters.entrySet().size() < 1) {
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatComponentText(EnumChatFormatting.RED + "There are no active filters"), startId++);
        }
        for (Map.Entry<String, Integer> entry : ChatHandler.filters.entrySet()) {
            String rawJson = StatCollector.translateToLocal("acs.chat.acsFilter");
            String escapedRegex = entry.getKey().replace("\\", "\\\\\\");
            rawJson = rawJson.replaceAll("@filter_id@", Integer.toString(entry.getValue()));
            escapedRegex = escapedRegex.replace("\"", "\\\"");
            rawJson = rawJson.replace("@filter@", "\\\"" + escapedRegex + "\\\"");
            IChatComponent filter = IChatComponent.Serializer.func_150699_a(rawJson);
            int newId = startId++;
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(filter, newId);
        }
        lastEndId = startId;
    }
}
