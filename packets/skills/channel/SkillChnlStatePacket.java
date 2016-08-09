package net.kirinnee.packets.skills.channel;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SkillChnlStatePacket implements IMessage{
	public float state;
	public SkillChnlStatePacket(){
	}
    public SkillChnlStatePacket(float st) {
    	state = st;
    }

    @Override
    public void toBytes(ByteBuf buf) {
    	buf.writeFloat(state);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    	state = buf.readFloat();
    }

}
