package net.kirinnee.skills.core;

import net.kirinnee.core.Main;
import net.kirinnee.packets.skills.channel.PlayerChannelFromClientPacket;
import net.kirinnee.skills.core.SkillCapability.ICasterCapability;
import net.kirinnee.skills.core.data.SkillDataClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class SkillsManagerClient implements ICasterCapability{

	public String identifier() {
		// TODO Auto-generated method stub
		return "skillsdataclient";
	}
	
	/**
	 * The value of the skillID of the skill in slot 0 to 5.
	 */
	public SkillDataClient[] HotBar = new SkillDataClient[]{
			new SkillDataClient(0,100), 
			new SkillDataClient(0,100), 
			new SkillDataClient(0,100), 
			new SkillDataClient(0,100), 
			new SkillDataClient(0,100), 
			new SkillDataClient(0,100)
	};
	//TODO:Implement
	public boolean isMagicGuardOn;
	
	public void unpackSkills(NBTTagCompound nbt){
		for(Integer i = 0; i <= 5; i++){
			NBTTagCompound skill = nbt.getCompoundTag(i.toString());
			int id = skill.getInteger("id");
			if(id != 10000){
				HotBar[i].jobID = id/100;
				HotBar[i].skillID = id%100;
				HotBar[i].icon = new ResourceLocation(skill.getString("res"));
			}
		}
	}

	@Override
	public NBTBase saveSkills() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadSkills(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		
	}
	
	// TODO:CHANNELING
	public float getChannelState = 0;
	public boolean isChannel = false;
	
	public void startChannel(EntityPlayer player){
		getChannelState = 0;
		isChannel = true;
		Main.snw.sendToServer(new PlayerChannelFromClientPacket(true));
	}
	
	public void endChannel(EntityPlayer player){
		isChannel = false;
		getChannelState = 0;
		Main.snw.sendToServer(new PlayerChannelFromClientPacket(false));
	}
	
	public void controlChannelState(boolean start){
		getChannelState = 0;
		isChannel = start;
	}
	
	public void setChannelState(float state){
		getChannelState = state;
	}

	
	public float getChannelState(){
		return getChannelState;
	}
	
	public boolean isChanneling(){
		return isChannel;
	}
}
