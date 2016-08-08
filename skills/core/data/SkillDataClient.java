package net.kirinnee.skills.core.data;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

/**
 * This is the fundamental skill data that each player requires on their client.
 * 
 * @author Arch2K
 *
 */
public class SkillDataClient extends SkillData{
	public SkillDataClient(int sID, int jID, String resource){
		skillID = sID;
		jobID = jID;
		icon = new ResourceLocation(resource);
	}
	public SkillDataClient(int sID, int jID){
		skillID = sID;
		jobID = jID;
	}
	/**
	 * The ID of the job the skill belongs to.
	 */
	public int jobID;

	/**
	 * The icon of the skill.
	 */
	public ResourceLocation icon = null;

	public String toString(){
		return ((Integer)skillID).toString();
	}
	
	public int toID(){
		return ((Integer)(jobID*100+skillID));
	}
}
