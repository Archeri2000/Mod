package net.kirinnee.skills.core;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.kirinnee.core.Main;
import net.kirinnee.packets.skills.SyncHotbarPackets;
import net.kirinnee.packets.skills.channel.PlayerChannelPacket;
import net.kirinnee.skills.core.SkillCapability.ICasterCapability;
import net.kirinnee.skills.core.SkillCapability.IPassiveCapability;
import net.kirinnee.skills.core.data.SkillDataServer;
import net.kirinnee.skills.novice.NoviceSkillPack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import scala.actors.threadpool.Arrays;

/**
 * This class manages the crucial data of the skills on the server for a player.
 * 
 * @author Arch2K
 */
public class SkillsManagerServer implements ICasterCapability{
	/**
	 * Identifier of the class
	 * @return Identifier
	 */
	public static Set<SkillsManagerServer> update = new HashSet<SkillsManagerServer>();
	public String identifier(){
		return iden;
	}
	/**
	 * The value of the skillID of the skill in slot 0 to 5.
	 */
	public int[] HotBar = new int[]{
		00001,00002,00003,10000,10000,10000
	};
	/**
	 * compiles the hotbar and converts it into NBT to send over.
	 * @return the NBT of the compiled hotbar data.
	 */
	public NBTTagCompound compileSkills(){
		NBTTagCompound nbt = new NBTTagCompound();
		for(Integer i = 0; i <= 5; i++){
			NBTTagCompound skill = new NBTTagCompound();
			skill.setInteger("id", HotBar[i]);
			if(HotBar[i] != 10000 && getSkillData(HotBar[i]/100,HotBar[i]%100)!=null){
				skill.setString("res", playerSkills.get(HotBar[i]/100).getSkillData(HotBar[i]%100).resource);
			}
			nbt.setTag(i.toString(), skill);
		}
		return nbt;
	}
	/**
	 * decompiles the hotbar and converts it from NBT
	 */
	public void decompileSkills(NBTTagCompound nbt){
		for(Integer i = 0; i <= 5; i++){
			NBTTagCompound skill = nbt.getCompoundTag(i.toString());
			HotBar[i] = skill.getInteger("id");
		}
	}
	
	
	private String iden = "skillsdata";
	
	/**
	 * Creating a hashmap of skill packages
	 */
	public static final HashMap<Integer,BaseSkillPackage> SkillPacks;
	static{
		SkillPacks = new HashMap<Integer,BaseSkillPackage>();
		//REPEAT THIS FOR EVERY PACK: SkillPacks.put(key, value);
		SkillPacks.put(00, new NoviceSkillPack());
	}		
			
	/**
	 * The constructor instantiating the skill manager with the caster.
	 * @param caster - The player casting the skills
	 */
	public SkillsManagerServer(){
		//registerSkills(job);
	}
	/**
	 * Creates an HashMap of SkillPackages containing HashMaps of SkillData. 
	 * The skill packages are denoted by job ID.
	 */
	private Map<Integer, BaseSkillPackage> playerSkills = new HashMap<Integer, BaseSkillPackage>();
	
	/**
	 * Adds the skill package of the job into the playerSkills HashMap.
	 * Effectively, it bestows the job and its skills on the player.
	 * @param jobSkills - The list of skills belonging to the job
	 * @param ID - The jobID
	 */
	
	public void addJobSkillList(BaseSkillPackage jobSkills, int ID){
		playerSkills.put(ID, jobSkills);
	}
	
	/**
	 * Removes the skill package of the job from the playerSkills HashMap.
	 * Effectively, it removes the job from the player.
	 * Used for class changes etc.
	 * @param ID
	 */
	
	public void removeJobSkillList(int ID){
		playerSkills.remove(ID);
	}
	
	/**
	 * Allows one to access the data of a skill given a known job ID and skill ID
	 * @param jID - the job ID of the skill(the job the skill belongs to)
	 * @param sID - the skill ID of the skill
	 * @return returns the skill that has the respective job ID and skill ID
	 */
	
	public SkillDataServer getSkillData(int jID, int sID){
		if(playerSkills.containsKey(jID)){
			return playerSkills.get(jID).getSkillData(sID);
		}else{
			return null;
		}
	}
	
	/**
	 * Casts the skill by providing the skill data to the utility method.
	 * @param jID - the job ID of the skill(the job the skill belongs to)
	 * @param sID - the skill ID of the skill
	 */
	
	public void castSkill(int jID, int sID, int augID, EntityPlayer skillCaster, int slot){
		SkillUtils.castSkill(skillCaster, this.getSkillData(jID,sID), slot);
	}

	
    /*==========================
     *      LOAD, SAVE
     ===========================*/
	
	//Saving skills to NBT
	/**
	 * Saves all the skill information into NBT for when the player quits/dies/forces save
	 * @param nbt - The NBT Tag Compound that contains the skill information
	 */
	@Override
	public NBTBase saveSkills(){
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagCompound properties = new NBTTagCompound();
		for(BaseSkillPackage pskill : playerSkills.values()){
			pskill.saveSkills(properties);
		}
		nbt.setTag("hotbar", compileSkills());
		nbt.setTag(iden, properties);
		return nbt;
	}
	
	//Loading skills from NBT
	/**
	 * Loads all the skill information from NBT for when the player joins/revives/forces load
	 * @param nbt - The NBT Tag Compound that contains the skill information
	 * @param job - The job ID of the player.
	 */
	@Override
	public void loadSkills(NBTTagCompound nbt){
    	if(nbt.getTag(iden) != null){
	    	NBTTagCompound properties = (NBTTagCompound) nbt.getTag(iden);
			Set<String> keys = properties.getKeySet();
			registerSkills(keys);
	    	for(BaseSkillPackage pskill : playerSkills.values()){
	    		pskill.loadSkills(properties);
    		}
	    	decompileSkills(nbt.getCompoundTag("hotbar"));
    	reloadPassives();
    	update.add(this);
    	}
	}

	/**
	 * A loop that calls on all the jobs saved
	 * @param job - the set of strings of the jobs of this player.
	 */
	public void registerSkills(Set<String> job) {
		for(String jb:job){
			int jobkey = Integer.parseInt(jb);
			if(!playerSkills.containsKey(jobkey)){
				try {
					playerSkills.put(jobkey,SkillPacks.get(jobkey).getClass().getConstructor(Boolean.class).newInstance(true));
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * A method that first initialises each job using a recursive loop. 
	 * It then initialises all custom packages used by the player.
	 * @param job - the class of the player
	 * @param packs - the hidden packs of the player
	 */
	public void initialiseSkills(int job, int packs) {
		//Registering the job.
		if(!playerSkills.containsKey(job)){
			try {
				playerSkills.put(job,SkillPacks.get(job).getClass().getConstructor(Boolean.class).newInstance(true));
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(playerSkills.get(job).parentID != -1){
			initialiseSkills(playerSkills.get(job).parentID, 0);
		}
		
		//Initialise skill packs
		if(packs != 0){
			boolean[] playerpck = new boolean[32];
			Arrays.fill(playerpck, Boolean.TRUE);
			for(int key:playerSkills.keySet()){
				if(key >= 67){
					playerpck[99-key-1] = false;
				}
			}
			boolean[] pcks = SkillUtils.translateInteger(packs);
			boolean[] arr = new boolean[32];
			for(int j = 0; j < arr.length; j++){
				arr[j] = pcks[j] & playerpck[j];
			}
			for(int i = 0; i < arr.length; i++){
				if(arr[i]){
					int jobkey = 99-i;
					if(!playerSkills.containsKey(jobkey)){
						try {
							playerSkills.put(jobkey,SkillPacks.get(jobkey).getClass().getConstructor(Boolean.class).newInstance(true));
						} catch (InstantiationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public void initialiseSkillSys(EntityPlayer player, int job, int packs){
		initialiseSkills(job, packs);
		restartCooldowns(player);
	}
	
	public void restartCooldowns(EntityPlayer player){
		Main.snw.sendTo(new SyncHotbarPackets(this.compileSkills()), (EntityPlayerMP) player);
		for(BaseSkillPackage pack : this.playerSkills.values()){
			for(SkillDataServer skill: pack.packageSkills.values()){
				if(skill.cdState != 0){
					SkillUtils.Cooldown(player, skill.toID(), skill.cdState, -1);
				}
			}
		}
	}
	
	//TODO: Finish this method eventually
	private void reloadPassives(){
	}
	
	
	public SkillDataServer getSkillData(int fullSkillID) {
		return this.getSkillData(fullSkillID/100, fullSkillID%100);
	}
	
	
	// TODO:CHANNELING
	public float getChannelState = 0;
	public boolean isChannel = false;
	
	public void startChannel(EntityPlayer player){
		getChannelState = 0;
		isChannel = true;
		Main.snw.sendTo(new PlayerChannelPacket(true), (EntityPlayerMP) player);
	}
	
	public void endChannel(EntityPlayer player){
		isChannel = false;
		getChannelState = 0;
		Main.snw.sendTo(new PlayerChannelPacket(false), (EntityPlayerMP) player);
	}
	
	public void controlChannelState(boolean start){
		getChannelState = 0;
		isChannel = start;
	}
	
	public void setChannelState(float state){
		getChannelState = state;
	}
	
	public float getChannelState(){
		return getChannelState;
	}
	
	public boolean isChanneling(){
		return isChannel;
	}
}
