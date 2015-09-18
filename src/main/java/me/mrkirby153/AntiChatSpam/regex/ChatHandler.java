package me.mrkirby153.AntiChatSpam.regex;

import me.mrkirby153.AntiChatSpam.AntiChatSpam;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatHandler {

    private static File chatFilterFile = new File("AntiChatSpam_filters.txt");

    public static HashMap<String, Integer> filters = new HashMap<>();

    private static int startingTextId = 7706171;

    public static void load() {
        filters = new HashMap<>();
        // Load from file
        try {
            if (!chatFilterFile.exists())
                chatFilterFile.createNewFile();
            BufferedReader reader = new BufferedReader(new FileReader(chatFilterFile));
            String line;
            while ((line = reader.readLine()) != null) {
                add(line, false);
            }
            reader.close();
        } catch (IOException e) {
            AntiChatSpam.logger.catching(Level.FATAL, e);
        }
    }

    public static void save() {
        // Save to file
        try {
            String toWrite = "";
            for (String s : filters.keySet()) {
                toWrite += s + "\n";
            }
            FileWriter writer = new FileWriter(chatFilterFile);
            writer.write(toWrite);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            AntiChatSpam.logger.catching(Level.FATAL, e);
        }
    }

    public static void add(String pattern, boolean save) {
        AntiChatSpam.logger.info(String.format("Adding filter \"%s\"", pattern));
        filters.put(pattern, startingTextId++);
        if (save)
            save();
    }

    public static void add(String pattern) {
        add(pattern, true);
    }

    public static void remove(int id){
        Iterator<Map.Entry<String, Integer>> iterator = filters.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, Integer> entry = iterator.next();
            if(entry.getValue() == id)
                iterator.remove();
        }
        save();
    }


    public static boolean hasHandledChat(IChatComponent component) {
        String unformattedMessage = component.getUnformattedText();
        for (Map.Entry<String, Integer> p : filters.entrySet()) {
            String pattern = p.getKey();
            int chatId = p.getValue();
            Pattern pat = Pattern.compile(pattern);
            Matcher matcher = pat.matcher(unformattedMessage);
            if (matcher.find()) {
                Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(component, chatId);
                return true;
            }
        }
        return false;
    }

    public static void doThings() {
        System.out.println("It works");
    }

    public static String[] getAllRules() {
       ArrayList<String> list = new ArrayList<>();
        for(String rule : filters.keySet()){
            list.add(rule);
        }
        return list.toArray(new String[filters.keySet().size()]);
    }

    public static void edit(int messageId, String newMessage) {
        remove(messageId);
        filters.put(newMessage, messageId);
        save();
    }
}
