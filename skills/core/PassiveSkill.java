package net.kirinnee.skills.core;

public interface PassiveSkill {
	public void Update(int SkillLvl);
	public void Initialise(int SkillLvl);
	public void End();
}
