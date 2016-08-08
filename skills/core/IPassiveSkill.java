package net.kirinnee.skills.core;

public interface IPassiveSkill {
	public void Update(int SkillLvl);
	public void Initialise(int SkillLvl);
	public void End();
}
