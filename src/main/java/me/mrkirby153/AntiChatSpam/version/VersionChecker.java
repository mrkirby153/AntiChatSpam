package me.mrkirby153.AntiChatSpam.version;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import me.mrkirby153.AntiChatSpam.reference.Strings;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;

public class VersionChecker {

    public static String remoteVersion = "INVALID_VERSION";
    public static boolean wrongMCVersion = false;
    protected static boolean doneChecking = false;
    private static boolean nagged = false;

    public static VersionStatus status = VersionStatus.OUTDATED;

    public void init() {
        ThreadVersionChecker.checkVersion();
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (doneChecking && event.phase == TickEvent.Phase.END && Minecraft.getMinecraft().thePlayer != null && !nagged) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            if (status == VersionStatus.ERRORED) {
                player.addChatComponentMessage(IChatComponent.Serializer.func_150699_a(StatCollector.translateToLocal("acs.chat.versionCheckError")));
                nagged = true;
                return;
            }
            if (wrongMCVersion) {
                ChatStyle style = new ChatStyle();
                style.setBold(true);
                style.setColor(EnumChatFormatting.RED);
                player.addChatComponentMessage(new ChatComponentTranslation("acs.chat.wrongMcVersion").setChatStyle(style));
                status = VersionStatus.WRONG_MC_VERSION;
            }
            if (!Strings.VERSION.equals(remoteVersion)) {
                IChatComponent component_1 = IChatComponent.Serializer.func_150699_a(StatCollector.translateToLocal("acs.chat.outdated.1"));
                IChatComponent component_2 = IChatComponent.Serializer.func_150699_a(StatCollector.translateToLocal("acs.chat.outdated.2").replace("@VERSION@", Strings.VERSION).replace("@MC_VERSION@", Strings.MC_VERSION).replace("@NEW_VERSION@", remoteVersion));
                player.addChatComponentMessage(component_1);
                player.addChatComponentMessage(component_2);
                nagged = true;
                status = VersionStatus.OUTDATED;
                return;
            } else {
                status = VersionStatus.CURRENT;
            }
            nagged = true;
        }
    }

    public enum VersionStatus {
        WRONG_MC_VERSION,
        ERRORED,
        OUTDATED,
        CURRENT
    }
}
