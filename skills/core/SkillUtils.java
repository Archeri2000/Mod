package net.kirinnee.skills.core;

import java.util.HashMap;

import net.kirinnee.core.CommonEventHandler;
import net.kirinnee.core.CoreUtil;
import net.kirinnee.core.EPosition;
import net.kirinnee.core.Main;
import net.kirinnee.core.playerstats.StatUtils;
import net.kirinnee.packets.skills.SkillCDStatePacket;
import net.kirinnee.skills.core.SkillCapability.ICasterCapability;
import net.kirinnee.skills.core.data.SkillDataServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public final class SkillUtils {
	private SkillUtils(){
		
	}
   @CapabilityInject(ICasterCapability.class)
   private static final Capability<ICasterCapability> CAST_MGR = null;
	/**
	 * Consumes mana for various purposes
	 * @param caster - the player losing mana
	 * @param mpCost - the amount of mana lost
	 */
	public static void ConsumeMana(EntityPlayer caster, float mpCost){
		StatUtils.setStats(caster, StatUtils.MANA, true, -mpCost);
	}
	
	/**
	 * Starts a channeling skill
	 * @param caster - the player casting
	 * @param fullSkillID - the ID of the skill
	 * @param secondsChannelDuration - the seconds of channeling
	 * @param slot - the slot
	 * @return
	 */
	public static boolean startChannel(EntityPlayer caster, int fullSkillID, float secondsChannelDuration, int slot){
		final ICasterCapability cst = caster.getCapability(CAST_MGR, null);
		if(!cst.isChanneling()){
			EPosition b = new EPosition(caster.posX,caster.posY,caster.posZ);
			cst.startChannel(caster);
			CommonEventHandler.ticker.add(fullSkillID + caster.getDisplayNameString(), secondsChannelDuration, 1, fullSkillID, slot, caster, b);
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Continues a channel.
	 * @param IChannelingSkill sk - the skill being channelled
	 * @param EPosition b - the caster's position
	 * @param caster - the player channeling
	 * @param secondsChannelDuration - the duration of the channel in seconds
	 * @param toEnd - whether the channel will be terminated
	 */
	public static void Channel(IChannelingSkill sk, EPosition b, int fullSkillID, EntityPlayer caster, boolean toEnd, int maxTicks, int currentTicks){
		final ICasterCapability cst = caster.getCapability(CAST_MGR, null);
		if(cst != null){
			if((b.distanceSq(caster.posX, caster.posY, caster.posZ) < 1) && !caster.isHandActive() && !caster.isSwingInProgress){
				int lvl = ((SkillsManagerServer) cst).getSkillData(fullSkillID).skillLevel;
				int augID = ((SkillsManagerServer) cst).getSkillData(fullSkillID).augmentationID;
				if(sk.onChannel(caster, lvl, augID, toEnd, maxTicks, currentTicks)){
					sk.onChannelSuccess();
					cst.endChannel(caster);
				}else{
					return;
				}
			}
			cst.endChannel(caster);
		}
	}
	
	/**
	 * Starts a cooldown.
	 * @param caster - the player casting
	 * @param fullSkillID - the skillid+jobid
	 * @param ticksCooldownDuration - the number of ticks
	 * @param slot - the slot the skill belongs to
	 */
	public static void Cooldown(EntityPlayer caster, int fullSkillID, float secondsCooldownDuration, int slot){
		if(!caster.getEntityWorld().isRemote){
			final SkillsManagerServer cast = (SkillsManagerServer) caster.getCapability(CAST_MGR, null);
			cast.getSkillData(fullSkillID).cdState = secondsCooldownDuration;
			Main.snw.sendTo(new SkillCDStatePacket(fullSkillID, slot, (int) secondsCooldownDuration), (EntityPlayerMP) caster);
			CommonEventHandler.ticker.add(fullSkillID + caster.getDisplayNameString(), secondsCooldownDuration, 0, fullSkillID, slot, caster);
		}
	}
	
	/**
	 * Casts a skill. SERVER ONLY
	 * @param caster - the player casting the skill
	 * @param skilldat - the skilldata(such as skill level) of the skill
	 * @param slot - the slot the skill is binded to
	 */
	public static void castSkill(EntityPlayer caster, SkillDataServer skilldat, int slot){
		ISkillServer skill = getSkill(skilldat.jobID, skilldat.skillID);
		int lvl = skilldat.skillLevel;
		int augID = skilldat.augmentationID;
		//TODO: Placeholder until stats handler comes up, then replace the 9999 with the getter.
		if(skilldat.cdState == 0 && skill.MPCost(lvl, augID) <= StatUtils.getStatsByName(caster.getDisplayNameString(), "cMP")){
			if(skill.PreCast(caster, lvl, augID, slot)){
				if(skill.OnCast(caster, lvl, augID, slot)){
					if(skill.PostCast(caster, lvl, augID, slot)){
						//Put something here?
						//Should mana costs etc be put under cast skill, or perhaps under pre/post cast.
					}
				}
			}
		}
	}
	/**
	 * Gets the skill data.
	 * @param jobID - the jobID of the skill
	 * @param skillID - the skillID of the skill
	 * @return
	 * the Skill as saved on the server.
	 */
	public static ISkillServer getSkill(int jobID, int skillID){
		return SkillsManagerServer.SkillPacks.get(jobID).getSkill(skillID);
	}
	
	/**
	 * Translates an integer into a boolean array, used to store extra skillpack data.
	 * @param packs - integer key of the skillpacks
	 * @return the boolean array of skillpacks which the player has
	 */
	public static boolean[] translateInteger(int packs){
		boolean[] result = new boolean[32];
		for(int i = 0; i < 32; i++){
			result[i] = (packs & (1 << i)) != 0;
		}
		return result;
	}
	
	/**
	 * Translates a boolean array back into an integer, used to obtain the integer key to the skillpack combination.
	 * @param array - the boolean array of skillpacks which the player has
	 * @return the integer key of the skillpacks
	 */
	public static int translateBoolArr(boolean[] array){
		int result = 0;
		for (int i = array.length-1; i >= 0; i--) {
		    result = (result << 1) + (array[i] ? 1 : 0);
		}
		return result;
	}
}
