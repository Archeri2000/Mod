package net.kirinnee.packets.skills;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SyncSkillSlotPacketsFromClient implements IMessage{
	public NBTTagCompound message;
    public NBTTagCompound Stats; 
    public SyncSkillSlotPacketsFromClient() {}
    public SyncSkillSlotPacketsFromClient(NBTTagCompound nbt) {
        this.message = nbt;
    }

    @Override
    public void toBytes(ByteBuf buf) { 
        ByteBufUtils.writeTag(buf, message);
    }

    @Override
    public void fromBytes(ByteBuf buf) { 
        this.message = ByteBufUtils.readTag(buf);
    }

}