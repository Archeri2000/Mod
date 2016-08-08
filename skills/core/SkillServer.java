package net.kirinnee.skills.core;

import net.minecraft.entity.player.EntityPlayer;

/**
 * This interface determines what methods are required in skills though it is designed for active skills. 
 * NOTE: Mana deduction and cooldown have to be factored in within either precast, oncast or postcast.
 * 
 * @author Arch2K
 *
 */
public interface SkillServer {
	/**
	 * 
	 * @return
	 * The Skill ID
	 */
	public int ID();//TODO:Send
	public int jID();
	public int getID();
	/**
	 * Given the skill level and augmentation ID, returns the mana cost before passives etc are applied
	 * @param skillLevel - Skill Level
 	 * @param augID - The augmentation ID used for passives that directly affect the skill.
	 * @return
	 * Mana Cost
	 */
	public float MPCost(int skillLevel, int augID);
	/**
	 * Given the skill level and augmentation ID, returns the cooldown duration before passives etc are applied
	 * @param skillLevel - Skill Level
	 * @param augID - The augmentation ID used for passives that directly affect the skill.
	 * @return
	 * Cooldown duration
	 */
	public float Cooldown(int skilLevel, int augID);
	/**
	 * Given the skill level and augmentation ID, returns the precast(conditions) before passives etc are applied
	 * @param skillLevel - Skill Level
	 * @param augID - The augmentation ID used for passives that directly affect the skill.
	 * @param slot - The slot the skill is in. Used primarily for cooldowns
	 * @return
	 * boolean indicating whether the skill will proceed
	 */
	public boolean PreCast(EntityPlayer caster, int skillLevel, int augID, int slot);
	/**
	 * Given the skill level and augmentation ID, returns the oncast(main effect) before passives etc are applied
	 * @param skillLevel - Skill Level
 	 * @param augID - The augmentation ID used for passives that directly affect the skill.
 	 * @param slot - The slot the skill is in. Used primarily for cooldowns
 	 * @return
	 * boolean indicating whether the skill will proceed
	 */
	public boolean OnCast(EntityPlayer caster, int skillLevel, int augID, int slot);
	/**
	 * Given the skill level and augmentation ID, returns the postcast(after effect) before passives etc are applied
	 * @param skillLevel - Skill Level
	 * @param augID - The augmentation ID used for passives that directly affect the skill.
	 * @param slot - The slot the skill is in. Used primarily for cooldowns
	 * @return
	 * boolean indicating whether the skill will proceed
	 */
	public boolean PostCast(EntityPlayer caster, int skillLevel, int augID, int slot);
	
	public String resourceAddress(int augID);//TODO:Send
	
	public int getMaxSPLevel();
}
