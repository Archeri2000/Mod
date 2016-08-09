package net.kirinnee.packets.skills;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SkillCastFromClientPackets implements IMessage{
	public NBTTagCompound message;
    public NBTTagCompound Stats; 
    public SkillCastFromClientPackets() {}
    public SkillCastFromClientPackets(int slot, int id) {
        message = new NBTTagCompound();
        message.setInteger("id", id);
        message.setInteger("slot", slot);
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