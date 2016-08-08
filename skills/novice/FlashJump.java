package net.kirinnee.skills.novice;

import net.kirinnee.core.CoreUtil;
import net.kirinnee.core.Main;
import net.kirinnee.core.playerstats.StatUtils;
import net.kirinnee.packets.fx.FXClientPacket;
import net.kirinnee.skills.core.RangeFinder;
import net.kirinnee.skills.core.ISkillServer;
import net.kirinnee.skills.core.SkillUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

public class FlashJump implements ISkillServer{
	public FlashJump() {
	}

	@Override
	public int getMaxSPLevel() {
		return 1;
	}

	@Override
	public int ID() {
		return 01;
	}

	@Override
	public float MPCost(int skillLevel, int augID) {
		return 2;
	}

	@Override
	public float Cooldown(int skilLevel, int augID) {
		return 0;
	}

	@Override
	public boolean PreCast(EntityPlayer skillcaster, int skillLevel, int augID, int slot) {
		if(!skillcaster.onGround && !skillcaster.isInLava() && !skillcaster.isInWater() && !skillcaster.isRiding()){
			SkillUtils.ConsumeMana(skillcaster, MPCost(skillLevel, augID));
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean OnCast(EntityPlayer skillcaster, int skillLevel, int augID, int slot) {
		//ANIMATION
		Main.snw.sendToAll(new FXClientPacket(0,skillcaster.posX,skillcaster.posY,skillcaster.posZ,0,0,0));
		
		//EFFECTS
		StatUtils.addStats(skillcaster, StatUtils.AP);
		SkillUtils.Cooldown(skillcaster, getID(), Cooldown(skillLevel, augID), slot);
		Vec3d v = new Vec3d(skillcaster.getLook(1.0f).xCoord,0,skillcaster.getLook(1.0f).zCoord);
		skillcaster.addVelocity(1f*v.normalize().xCoord, 0.7f-skillcaster.motionY, 1f*v.normalize().zCoord);
		((EntityPlayerMP)skillcaster).connection.sendPacket(new SPacketEntityVelocity(skillcaster));
		return true;
	}

	@Override
	public boolean PostCast(EntityPlayer caster, int skillLevel, int augID, int slot) {
		return true;
	}

	@Override
	public String resourceAddress(int augID) {
		return Main.MODID+":skills/novice/FlashJump.png";
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

}
