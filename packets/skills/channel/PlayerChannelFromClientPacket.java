package net.kirinnee.packets.skills.channel;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PlayerChannelFromClientPacket implements IMessage{

	public boolean starting;
    public PlayerChannelFromClientPacket() {
    }
    
    public PlayerChannelFromClientPacket(boolean start) {
    	starting = start;
    }

    @Override
    public void toBytes(ByteBuf buf) {
    	buf.writeBoolean(starting);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    	starting = buf.readBoolean();
    }

}