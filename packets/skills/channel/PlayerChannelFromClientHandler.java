package net.kirinnee.packets.skills.channel;
import net.kirinnee.core.Main;
import net.kirinnee.packets.*;
import net.kirinnee.skills.core.SkillCapability.ICasterCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

//Client Sided
public class PlayerChannelFromClientHandler implements IMessageHandler<PlayerChannelFromClientPacket, IMessage> { 
	@CapabilityInject(ICasterCapability.class)
	private static Capability<ICasterCapability> Caster = null;
	
    @Override
    public IMessage onMessage(final PlayerChannelFromClientPacket message, final MessageContext ctx) { 
        Main.proxy.getThreadFromContext(ctx).addScheduledTask(new Runnable() {
            @Override
            public void run() {
            	EntityPlayer player = Main.proxy.getPlayerEntity(ctx);
            	final ICasterCapability Cst = player.getCapability(Caster, null);
            	if(Cst != null){
	            	Cst.controlChannelState(message.starting);
            	}
            }
        });
        return null;
    }

}