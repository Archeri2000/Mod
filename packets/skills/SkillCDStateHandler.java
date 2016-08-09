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
public class SkillCDStateHandler implements IMessageHandler<SkillCDStatePacket, IMessage> { 
	
	
    @Override
    public IMessage onMessage(final SkillCDStatePacket message, final MessageContext ctx) { 
        Main.proxy.getThreadFromContext(ctx).addScheduledTask(new Runnable() {
            @Override
            public void run() {
            	NBTTagCompound update = message.message;
            	EntityPlayer player = Main.proxy.getPlayerEntity(ctx);
            	if(player.worldObj.isRemote){
	            	final SkillsManagerClient Cst = (SkillsManagerClient) player.getCapability(Caster, null);

	            	if(Cst != null){
	            		int skillID = update.getInteger("skillID");
	            		int duration = update.getInteger("duration");
	            		int slot = update.getInteger("slot");
	            		if(slot > -1){
	            			if(slot < 6){
	            				if(Cst.HotBar[slot].toID()==skillID){
	            					Cst.HotBar[slot].cdState = duration;
	            				}
	            			}
	            		}else{
	            			for(int i = 0; i < 6; i++){
	            				if(Cst.HotBar[i].toID() == skillID){
	            					Cst.HotBar[i].cdState = duration;
	            					NBTTagCompound nbt = new NBTTagCompound();
	            					nbt.setInteger("skillID", skillID);
	            					nbt.setInteger("slot", i);
	            					Main.snw.sendToServer(new SyncSkillSlotPacketsFromClient(nbt));
	            					break;
	            				}else{
	            					
	            				}
	            			}
	            		}
	            	}else{
	            	    throw new NullPointerException("Cool Down Packet: CoolDown capability not valid!");
	            	}
            	}
            }
        });
        return null;
    }
    @CapabilityInject(ICasterCapability.class)
	private static Capability<ICasterCapability> Caster = null;

}