package net.kirinnee.packets.skills;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SkillCDStatePacket implements IMessage{
	public NBTTagCompound message;
    public NBTTagCompound Stats; 
    public SkillCDStatePacket() {}
    public SkillCDStatePacket(int skillid, int slot, int duration){
    	NBTTagCompound data = new NBTTagCompound();
    	data.setInteger("skillID", skillid);
    	data.setInteger("slot", slot);
    	data.setInteger("duration", duration);
    	message = data;
    }
    public SkillCDStatePacket(NBTTagCompound nbt) {
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