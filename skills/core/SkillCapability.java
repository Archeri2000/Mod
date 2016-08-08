package net.kirinnee.skills.core;

import java.util.HashMap;
import java.util.Map;

import net.kirinnee.core.Main;
import net.kirinnee.packets.skills.channel.PlayerChannelPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class SkillCapability{
	public static void PreInit(FMLPreInitializationEvent e){
		CapabilityManager.INSTANCE.register(IPassiveCapability.class, new IPassiveStorage(), IPassiveDef.class);
		CapabilityManager.INSTANCE.register(ICasterCapability.class, new ICastStorage(), ICastDef.class);
	}
	
    /*==========================
     *    Passive Capability
     ===========================*/
	public static interface IPassiveCapability {
		
	}

	public static class IPassiveStorage implements Capability.IStorage<IPassiveCapability> {
		  @Override
		  public NBTBase writeNBT(Capability<IPassiveCapability> capability, IPassiveCapability instance, EnumFacing side) {
		    // return an NBT tag
			  return null;
		  }

		  @Override
		  public void readNBT(Capability<IPassiveCapability> capability, IPassiveCapability instance, EnumFacing side, NBTBase nbt) {
		    // load from the NBT tag
		  }
	}
	
	public static class IPassiveDef implements IPassiveCapability {
		public IPassiveDef(){
			
		}
		
	}
	
    /*==========================
     *    Casting Capability
     ===========================*/
	
	public static interface ICasterCapability {
		public void controlChannelState(boolean start);	
		public float getChannelState();
		public void startChannel(EntityPlayer player);
		public void setChannelState(float state);
		public void endChannel(EntityPlayer player);
		public boolean isChanneling();
		
		public NBTBase saveSkills();

		public void loadSkills(NBTTagCompound nbt);
	}

	public static class ICastStorage implements Capability.IStorage<ICasterCapability> {
		  @Override
		  public NBTBase writeNBT(Capability<ICasterCapability> capability, ICasterCapability instance, EnumFacing side) {
		    // return an NBT tag
			  return null;
		  }

		  @Override
		  public void readNBT(Capability<ICasterCapability> capability, ICasterCapability instance, EnumFacing side, NBTBase nbt) {
		    // load from the NBT tag
		  }
	}
	
	public static class ICastDef implements ICasterCapability {
		public ICastDef(){
			
		}

		@Override
		public NBTBase saveSkills() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void loadSkills(NBTTagCompound nbt) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void controlChannelState(boolean start) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void startChannel(EntityPlayer player) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void endChannel(EntityPlayer player) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setChannelState(float state) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public float getChannelState() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean isChanneling() {
			// TODO Auto-generated method stub
			return false;
		}
	}
}