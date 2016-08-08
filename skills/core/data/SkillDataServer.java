package net.kirinnee.skills.core.data;

import net.kirinnee.skills.core.SkillUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * This is the fundamental skill data that each player requires on the server.
 * 
 * @author Arch2K
 *
 */
public class SkillDataServer extends SkillData{
	public SkillDataServer(int sID, int sLevel, int AugID, String resourceLoc){
		skillID = sID;
		skillLevel = sLevel;
		augmentationID = AugID;
		resource = resourceLoc;
	}
	public SkillDataServer(int sID, int sLevel, String resourceLoc){
		skillID = sID;
		skillLevel = sLevel;
		resource = resourceLoc;
	}
	/**
	 * The ID of the job the skill belongs to.
	 */
	public int jobID;
	/**
	 * The skill level of the skill.
	 */
	public int skillLevel = 0;
	/**
	 * The augmentation ID of the skill used for passives.
	 */
	public int augmentationID = 0;
	
	public String resource = null;
	
	public void saveSkill(NBTTagCompound SkillData){
		SkillData.setFloat("cdstate", cdState);
		SkillData.setInteger("skillLVL", skillLevel);
		SkillData.setInteger("augmentationID", augmentationID);
	}
	public void loadSkill(NBTTagCompound SkillData){
		cdState = SkillData.getFloat("cdstate");
		skillLevel = SkillData.getInteger("skillLVL");
		augmentationID = SkillData.getInteger("augmentationID");
		resource = SkillUtils.getSkill(this.jobID, this.skillID).resourceAddress(augmentationID);
	}
	
	public String toString(){
		return ((Integer)skillID).toString();
	}
	
	public int toID(){
		return ((Integer)(jobID*100+skillID));
	}
}
