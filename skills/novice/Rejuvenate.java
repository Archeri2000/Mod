package net.kirinnee.skills.novice;

import java.util.Random;

import net.kirinnee.core.KirinoUtil;
import net.kirinnee.core.Main;
import net.kirinnee.core.playerstats.StatUtils;
import net.kirinnee.skills.core.IChannelingSkill;
import net.kirinnee.skills.core.SkillUtils;
import net.minecraft.entity.player.EntityPlayer;

public class Rejuvenate implements IChannelingSkill{

	@Override
	public int ID() {
		// TODO Auto-generated method stub
		return 03;
	}

	@Override
	public int jID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return jID()*100+ID();
	}

	@Override
	public float MPCost(int skillLevel, int augID) {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public float Cooldown(int skilLevel, int augID) {
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public boolean PreCast(EntityPlayer caster, int skillLevel, int augID, int slot) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean OnCast(EntityPlayer caster, int skillLevel, int augID, int slot) {
		SkillUtils.startChannel(caster, getID(), getChannelTime(), slot);
		SkillUtils.Cooldown(caster, getID(), Cooldown(skillLevel, augID), slot);
		return true;
	}

	@Override
	public boolean PostCast(EntityPlayer caster, int skillLevel, int augID, int slot) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String resourceAddress(int augID) {
		// TODO Auto-generated method stub
		return Main.MODID + ":skills/novice/Rejuvenate.png";
	}

	@Override
	public int getMaxSPLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean onChannel(EntityPlayer caster, int skillLevel, int augID, boolean ch, int maxTicks, int currentTicks) {
		if(!caster.worldObj.isRemote && currentTicks % 2 == 0){
			
			//ANIMATION =======================================
				//ONE-TIME
				KirinoUtil.Animate(1, 2, caster, 0, 0, 0, 0, 0, 0);

				//CONTINUOUS
				Random rand = new Random();
				int numberofparticle = 8;
				double ranx[] = new double[numberofparticle];
				double rany[] = new double[numberofparticle];
				double ranz[] = new double[numberofparticle];
				for (int i=0; i<numberofparticle;i++){
					ranx[i] = caster.posX + (double) rand.nextInt(1) + rand.nextDouble() - (double) rand.nextInt(1) - rand.nextDouble();
					rany[i] = caster.posY+ (double) rand.nextInt(1) + rand.nextDouble();
					ranz[i] = caster.posZ + (double)rand.nextInt(1) + rand.nextDouble() - (double) rand.nextInt(1) - rand.nextDouble();
					KirinoUtil.Animate(0, 1, caster, ranx[i], rany[i], ranz[i], 0, 0, 0);
				}
			
			
			
			//EFFECT===================================================================================
			StatUtils.setStats(caster, StatUtils.CURRENT_HEALTH, true, 1);
			if(currentTicks%4 == 0){
				StatUtils.setStats(caster, StatUtils.CURRENT_HEALTH, true, StatUtils.getStatsByIndex(caster.getDisplayNameString(), StatUtils.HEALTH)/400*skillLevel);
			}
		}
		return ch;
	}

	@Override
	public float getChannelTime() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public void onChannelSuccess() {
		// TODO Auto-generated method stub
		
	}

}
