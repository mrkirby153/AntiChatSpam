package me.mrkirby153.AntiChatSpam.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class PacketAntiSpam implements IMessage {

    public static void sendMessage(EntityPlayer entityPlayer, String message) {

    }

    public String message;

    public PacketAntiSpam() {

    }

    public PacketAntiSpam(String message) {
        this.message = message;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        message = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.message);
    }


    public static class Handler implements IMessageHandler<PacketAntiSpam, IMessage>{
        @Override
        public IMessage onMessage(PacketAntiSpam message, MessageContext ctx) {
            return null;
        }
    }
}
