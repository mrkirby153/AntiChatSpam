package me.mrkirby153.AntiChatSpam.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;

public class PacketAntiSpam implements IMessage {

    public static void sendMessage(EntityPlayer entityPlayer, String message) {

    }

    public IChatComponent message;

    public PacketAntiSpam() {

    }

    public PacketAntiSpam(IChatComponent message) {
        this.message = message;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        message = readChatComponent(buf);
    }

    public IChatComponent readChatComponent(ByteBuf data){
        return IChatComponent.Serializer.func_150699_a(ByteBufUtils.readUTF8String(data));
    }

    public void writeChatComponent(ByteBuf data, IChatComponent component){
        ByteBufUtils.writeUTF8String(data, IChatComponent.Serializer.func_150696_a(component));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        writeChatComponent(buf, this.message);
    }


    public static class Handler implements IMessageHandler<PacketAntiSpam, IMessage>{
        @Override
        public IMessage onMessage(PacketAntiSpam message, MessageContext ctx) {
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(message.message, 7706071);
            return null;
        }
    }
}
