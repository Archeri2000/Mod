package net.kirinnee.skills.core;

import java.util.HashMap;

import net.kirinnee.skills.core.data.SkillDataServer;
import net.minecraft.nbt.NBTTagCompound;
/**
 * This is the abstract class detailing the properties of every skill package
 * 
 * @author Arch2K
 *
 */
public abstract class BaseSkillPackage{
	/**
	 * ID of parent job
	 */
	public int parentID;
	/**
	 * ID of this job
	 */
	public int ID;
	private String strID = ((Integer)ID).toString();
	/**
	 * Hashmap of skills
	 */
	public HashMap<Integer, SkillDataServer> packageSkills;
	/**
	 * Default constructor to create a reference to this classtype
	 */
	public BaseSkillPackage(){
		
	}
	/**
	 * This is the true constructor that will instantiate the class proper.
	 * @param b - Boolean parameter to differentiate from default constructor
	 */
	public BaseSkillPackage(Boolean b){
		if(b){
			packageSkills = new HashMap<Integer, SkillDataServer>();
			//packageSkills.put(key , value);
		}
	}
	public SkillDataServer getSkillData(int sID) {
		return packageSkills.get(sID);
	}
	public abstract ISkillServer getSkill(int sID);
	
	public void loadSkills(NBTTagCompound properties) {
		if(properties.hasKey(strID)){
			NBTTagCompound jobSkills = properties.getCompoundTag(strID);
			for(SkillDataServer skillData : packageSkills.values()){
				skillData.loadSkill(jobSkills.getCompoundTag(skillData.toString()));
			}
		}
	}

	public void saveSkills(NBTTagCompound properties) {
		NBTTagCompound jobSkills = new NBTTagCompound();
		for(SkillDataServer skillData : packageSkills.values()){
			NBTTagCompound skillDat = new NBTTagCompound();
			skillData.saveSkill(skillDat);
			jobSkills.setTag(skillData.toString(), skillDat);
		}
		properties.setTag(strID, jobSkills);
	}
}
