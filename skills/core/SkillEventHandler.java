package net.kirinnee.skills.core;

import java.util.HashSet;
import java.util.Set;

import net.kirinnee.core.CoreUtil;
import net.kirinnee.core.Main;
import net.kirinnee.packets.skills.SyncHotbarPackets;
import net.kirinnee.skills.core.SkillCapability.ICasterCapability;
import net.kirinnee.skills.novice.NoviceSkillPack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class SkillEventHandler {
	int tick = 0;
    @CapabilityInject(ICasterCapability.class)
    private static final Capability<ICasterCapability> CAST_MGR = null;
    
    /**
     * Copies the player's skill stats over to a new copy of the player
     * @param e - player clone event(occurs on death/dimension change)
     */
    @SubscribeEvent
    public void playerDeath(PlayerEvent.Clone e){
    	final ICasterCapability CST = e.getOriginal().getCapability(CAST_MGR, null);
		NBTTagCompound save = (NBTTagCompound) CST.saveSkills();
    	final ICasterCapability nCST = e.getEntityPlayer().getCapability(CAST_MGR, null);
    	nCST.loadSkills(save);
    }
    
    
    //TODO: Test Event
    @SubscribeEvent
    public void onUpdate(WorldTickEvent e){
    	if(!e.world.isRemote){
    		
	    	tick++;
	    	if(tick>100){
	    		tick = 0;
	    		for(EntityPlayer cast:e.world.playerEntities){
					final ICasterCapability caster = cast.getCapability(CAST_MGR, null);
	    			SkillsManagerServer cst = (SkillsManagerServer)caster;
	    			if(SkillsManagerServer.update.contains(cst)){
	    				cst.restartCooldowns(cast);
	    				SkillsManagerServer.update.remove(cst);
	    			}
	    			/*HashSet<String> test = new HashSet<String>();
	    			test.add("00");
	    			cst.registerSkills(test);*/
	    			Main.snw.sendTo(new SyncHotbarPackets(cst.compileSkills()), (EntityPlayerMP) cast);
	    		}
	    	}
	    	if(tick%5==0){
	    		for(EntityPlayer cast:e.world.playerEntities){
	    			//System.out.println(cast.posX + ";" + cast.posY + ";" + cast.posZ);
	    		}
	    	}
    	}
    }
    
    /**
     * Adds the ability to be a skill caster to the player
     * @param e - Attaching Capability Event
     */
	@SubscribeEvent
	public void onEntityConstruct(AttachCapabilitiesEvent.Entity e){
		if(e.getEntity() instanceof EntityPlayer){
			if(!e.getEntity().worldObj.isRemote){
				e.addCapability(new ResourceLocation(Main.MODID, "ICasterCapability"), new ICapabilitySerializable<NBTTagCompound>(){
					ICasterCapability caster = new SkillsManagerServer();
					@Override
					public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
						// TODO Auto-generated method stub
						return capability == CAST_MGR;
					}
		
					@Override
					public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
						// TODO Auto-generated method stub
						return capability == CAST_MGR ? CAST_MGR.<T>cast(caster) : null;
					}
		
					@Override
					public NBTTagCompound serializeNBT() {
						// TODO Auto-generated method stub
						return (NBTTagCompound) caster.saveSkills();
					}
		
					@Override
					public void deserializeNBT(NBTTagCompound nbt) {
						// TODO Auto-generated method stub
						caster.loadSkills(nbt);
					}
				});
			}else{
				e.addCapability(new ResourceLocation(Main.MODID, "ICasterCapability"), new ICapabilitySerializable<NBTTagCompound>(){
					ICasterCapability caster = new SkillsManagerClient();
					@Override
					public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
						// TODO Auto-generated method stub
						return capability == CAST_MGR;
					}
		
					@Override
					public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
						// TODO Auto-generated method stub
						return capability == CAST_MGR ? CAST_MGR.<T>cast(caster) : null;
					}
		
					@Override
					public NBTTagCompound serializeNBT() {
						// TODO Auto-generated method stub
						return (NBTTagCompound) caster.saveSkills();
					}
		
					@Override
					public void deserializeNBT(NBTTagCompound nbt) {
						// TODO Auto-generated method stub
						caster.loadSkills(nbt);
					}
				});
			}
		}
	}
}
