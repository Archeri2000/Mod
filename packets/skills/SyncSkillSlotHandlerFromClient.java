package net.kirinnee.packets.skills;
import net.kirinnee.core.CommonEventHandler;
import net.kirinnee.core.Main;
import net.kirinnee.skills.core.SkillsManagerServer;
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
public class SyncSkillSlotHandlerFromClient implements IMessageHandler<SyncSkillSlotPacketsFromClient, IMessage> { 
	@CapabilityInject(ICasterCapability.class)
	private static Capability<ICasterCapability> Caster = null;
	
    @Override
    public IMessage onMessage(final SyncSkillSlotPacketsFromClient message, final MessageContext ctx) { 
    	Main.proxy.getThreadFromContext(ctx).addScheduledTask(new Runnable() {
            @Override
            public void run() {
            	NBTTagCompound update = message.message;
            	EntityPlayer player = Main.proxy.getPlayerEntity(ctx);
        		int skillID = update.getInteger("skillID");
        		int slot = update.getInteger("slot");
            	if(CommonEventHandler.ticker.get(skillID+player.getDisplayNameString()) != null){
            		CommonEventHandler.ticker.get(skillID+player.getDisplayNameString()).slt = slot;
            	}else{
            		throw new NullPointerException("SkillUpdate Packet:Casting capability not valid!");
            	}
            }
        });
        return null;
    }

}