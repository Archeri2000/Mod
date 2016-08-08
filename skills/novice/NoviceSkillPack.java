package net.kirinnee.skills.novice;

import java.util.HashMap;

import net.kirinnee.skills.core.BaseSkillPackage;
import net.kirinnee.skills.core.ISkillServer;
import net.kirinnee.skills.core.data.SkillDataServer;

public class NoviceSkillPack extends BaseSkillPackage{
	private static HashMap<Integer, ISkillServer> NoviceSkills;
	static{
		NoviceSkills = new HashMap<Integer, ISkillServer>();
		NoviceSkills.put(01, new FlashJump());
		NoviceSkills.put(02, new HealMana());
		NoviceSkills.put(03, new Rejuvenate());

	}
	public NoviceSkillPack(){
		this.parentID = -1;
		this.ID=0;
	}
	public NoviceSkillPack(Boolean b){
		super(b);
		this.ID=0;
		this.parentID=-1;
		this.packageSkills.put(01, new SkillDataServer(1,0,0,NoviceSkills.get(01).resourceAddress(0)));
		this.packageSkills.put(02, new SkillDataServer(2,0,0,NoviceSkills.get(02).resourceAddress(0)));
		this.packageSkills.put(03, new SkillDataServer(3,0,0,NoviceSkills.get(03).resourceAddress(0)));

	}
	@Override
	public ISkillServer getSkill(int sID) {
		if(NoviceSkills.containsKey(sID)){
			return NoviceSkills.get(sID);
		}
		else{
			return null;
		}
	}

}
