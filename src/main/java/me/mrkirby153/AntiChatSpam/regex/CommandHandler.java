package me.mrkirby153.AntiChatSpam.regex;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;

import java.util.Map;

public class CommandHandler {

    private static int lastEndId;
    private final static int START_ID = 15000;
    private static int nextId = START_ID;

    public static void run(EntityPlayer player, String[] args) {
        if (args.length == 0) {
            printTemporaryChat(new ChatComponentText("Type !acs help to access the help").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.LIGHT_PURPLE)));
            // List every message
            printFilters();
            return;
        }
        if (args.length == 1) {
            // Show help
            if (args[0].equalsIgnoreCase("help")) {
                clearChat();
                showHelp();
            }
            if (args[0].equalsIgnoreCase("dismissHelp"))
                clearChat();
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

    private static void showHelp() {
        printTemporaryChat(new ChatComponentText("Click on [EDIT] or [DELETE] to edit or delete filters."));
        printTemporaryChat(generateHelpChat("!acs", "Shows all active filters"));
        printTemporaryChat(generateHelpChat("!acs add <regex>", "Adds a new filter with the corresponding regular expression."));
        printTemporaryChat(generateHelpChat("!acs delete <id>", "Deletes filter by its internal id."));
        printTemporaryChat(generateHelpChat("!acs edit <id> <new regex>", "Sets the regex identified by id to the new regex."));
        printTemporaryChat(IChatComponent.Serializer.func_150699_a(StatCollector.translateToLocal("acs.chat.dismissHelp")));
    }


    private static void printFilters() {
        clearChat();
        if (ChatHandler.filters.entrySet().size() < 1) {
            printTemporaryChat(new ChatComponentText("There are no filters to display!").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
        }
        for (Map.Entry<String, Integer> entry : ChatHandler.filters.entrySet()) {
            String rawJson = StatCollector.translateToLocal("acs.chat.acsFilter");
            String escapedRegex = entry.getKey().replace("\\", "\\\\\\");
            rawJson = rawJson.replaceAll("@filter_id@", Integer.toString(entry.getValue()));
            escapedRegex = escapedRegex.replace("\"", "\\\"");
            rawJson = rawJson.replace("@filter@", "\\\"" + escapedRegex + "\\\"");
            IChatComponent filter = IChatComponent.Serializer.func_150699_a(rawJson);
            printTemporaryChat(filter);
        }
    }


    public static void clearChat() {
        nextId = START_ID;
        int msgCountDelete = (lastEndId - nextId);
        for (int i = 0; i <= msgCountDelete; i++) {
            int msgNum = nextId++;
            Minecraft.getMinecraft().ingameGUI.getChatGUI().deleteChatLine(msgNum);
        }
        nextId = START_ID;
    }

    public static void printTemporaryChat(String message) {
        printTemporaryChat(new ChatComponentText(message));
    }

    public static void printTemporaryChat(IChatComponent component) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(component, nextId++);
        lastEndId = nextId;
    }

    private static IChatComponent generateHelpChat(String command, String helpText) {
        return new ChatComponentText(command).appendSibling(new ChatComponentText(" - " + helpText).
                setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA))).setChatStyle(new ChatStyle().
                setColor(EnumChatFormatting.GOLD));
    }
}
