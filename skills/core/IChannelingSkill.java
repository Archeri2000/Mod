package net.kirinnee.skills.core;

import net.kirinnee.core.EPosition;
import net.minecraft.entity.player.EntityPlayer;

public interface IChannelingSkill extends ISkillServer{	
	//Channelling effect
	/**
	 * The effect casted when channeling
	 * @param caster - the skill caster
	 * @param skillLevel - the skill level
	 * @param augID - the augmentation id
	 * @param ch - whether the channel has ended
	 * @param maxTicks - max ticks of channel
	 * @param currentTicks - current ticks of channel
	 * @return
	 * whether the channel was successful
	 */
	public abstract boolean onChannel(EntityPlayer caster, int skillLevel, int augID, boolean ch, int maxTicks, int currentTicks);
	
	//Channelling duration
	/**
	 * A float value indicating the channel duration in seconds
	 */
	public abstract float getChannelTime();
	
	//Channel success
	/**
	 * What to do when the channel succeeds
	 */
	public void onChannelSuccess();
}
