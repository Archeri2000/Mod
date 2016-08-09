package net.kirinnee.packets.skills;
import net.kirinnee.core.Main;
import net.kirinnee.skills.core.SkillsManagerClient;
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
public class SyncHotbarHandler implements IMessageHandler<SyncHotbarPackets, IMessage> { 
	
	
    @Override
    public IMessage onMessage(final SyncHotbarPackets message, final MessageContext ctx) { 
    	Main.proxy.getThreadFromContext(ctx).addScheduledTask(new Runnable() {
            @Override
            public void run() {
            	NBTTagCompound update = message.message;
            	EntityPlayer player = Main.proxy.getPlayerEntity(ctx);
            	final ICasterCapability Cst = player.getCapability(Caster, null);
            	if(Cst != null){
            		((SkillsManagerClient)Cst).unpackSkills(update);
            	}else{
            		
            		throw new NullPointerException("Hotbar Packet: Casting capability not valid!");
            	}
            }
        });
        return null;
    }
    @CapabilityInject(ICasterCapability.class)
	private static Capability<ICasterCapability> Caster = null;

}