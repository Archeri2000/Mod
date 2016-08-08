package net.kirinnee.skills.core;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;

public class RangeFinder {
	public static EntityLivingBase coneSingleTarget(EntityPlayer skillcaster, float range, float coneangle){
			if(!skillcaster.worldObj.isRemote){
				List<Entity> entities = skillcaster.worldObj.getEntitiesWithinAABBExcludingEntity(skillcaster, skillcaster.getEntityBoundingBox().expand(range, range, range));				
				EntityLivingBase target = null;
				Vec3d playerlook = skillcaster.getLook(1.0f).normalize();
				double tan = Math.tan(coneangle/180d*Math.PI);
				double X = 0.5*playerlook.xCoord/tan;
				double Y = 0.5*playerlook.yCoord/tan; 
				double Z = 0.5*playerlook.zCoord/tan; 
				Vec3d closestdist = new Vec3d(range+1,range+1,range+1);
				if(entities != null){
					for(Entity e:entities){
						if(e instanceof EntityLivingBase && e != skillcaster){							
							Vec3d edist = new Vec3d(e.posX - skillcaster.posX, e.posY - skillcaster.posY, e.posZ - skillcaster.posZ);
							if((edist.lengthVector() < closestdist.lengthVector()) && (Math.acos(playerlook.dotProduct(edist.normalize())) < (Math.PI))){
								Vec3d cdist = edist.subtract(-X, -Y, -Z);
								if(Math.acos(playerlook.dotProduct(cdist.normalize()))< (coneangle/180d*Math.PI)){
									target = (EntityLivingBase) e;
									closestdist = edist;
								}
							}
						}
					}
					if(target != null){
						RayTraceResult objectMouseOver = rayTracing(skillcaster, range, 1.0f); 
						if(objectMouseOver.typeOfHit.equals(Type.BLOCK)){
							if(objectMouseOver.getBlockPos().distanceSqToCenter(skillcaster.posX, skillcaster.posY, skillcaster.posZ)>target.getDistanceSqToEntity(skillcaster)){
								return target;
							}
						}else{
							return target;
						}
					}
				}
			}
			return null;
		}
		
	public static List<EntityLivingBase> coneMultiTarget(EntityPlayer skillcaster, float range, float coneangle){
		if(!skillcaster.worldObj.isRemote){
			List<Entity> entities = skillcaster.worldObj.getEntitiesWithinAABBExcludingEntity(skillcaster, skillcaster.getEntityBoundingBox().expand(range, range, range));
			List<EntityLivingBase> target = new ArrayList<EntityLivingBase>();
			Vec3d playerlook = skillcaster.getLook(1.0f).normalize();
			double tan = Math.tan(coneangle/180d*Math.PI);
			double X = 0.5*playerlook.xCoord/tan;
			double Y = 0.5*playerlook.yCoord/tan; 
			double Z = 0.5*playerlook.zCoord/tan; 
			Vec3d closestdist = new Vec3d(range+1,range+1,range+1);
			EntityLivingBase closeTarget = null;
			double smallestangle = coneangle/180d*Math.PI;
			if(entities != null){
				for(Entity e:entities){
					if(e instanceof EntityLivingBase){
						Vec3d edist = new Vec3d(e.posX - skillcaster.posX, e.posY - skillcaster.posY, e.posZ - skillcaster.posZ);
						if((edist.lengthVector() < closestdist.lengthVector()) && (Math.acos(playerlook.dotProduct(edist.normalize())) < (Math.PI))){
							Vec3d cdist = edist.subtract(-X, -Y, -Z);
							double eangle = Math.acos(playerlook.dotProduct(cdist.normalize()));
							if(eangle < (coneangle/180d*Math.PI)){
								target.add((EntityLivingBase) e);
								if((eangle < smallestangle)){
									closeTarget = (EntityLivingBase) e;
									smallestangle = eangle;
								}
							}
						}
					}
				}
				if(target.size() != 0){
					RayTraceResult objectMouseOver = rayTracing(skillcaster, range, 1.0f); 
					if(objectMouseOver.typeOfHit.equals(Type.BLOCK)){
						if(objectMouseOver.getBlockPos().distanceSqToCenter(skillcaster.posX, skillcaster.posY, skillcaster.posZ)>closeTarget.getDistanceSqToEntity(skillcaster)){
							return target;
						}
					}else{
						return target;
					}
				}
			}
		}
		return null;
	}
	
	public static List<EntityLivingBase> aoeMultiTarget(EntityPlayer skillcaster, float range){
		if(!skillcaster.worldObj.isRemote){
			List<Entity> entities = skillcaster.worldObj.getEntitiesWithinAABBExcludingEntity(skillcaster, skillcaster.getEntityBoundingBox().expand(range, range, range));
			List<EntityLivingBase> target = new ArrayList<EntityLivingBase>();
			for(Entity e:entities){
				if(e instanceof EntityLivingBase){
					target.add((EntityLivingBase) e);
				}
			}
			return target;
		}
		return null;
	}
	
	public static RayTraceResult rayTracing(EntityPlayer e, double floatA, float floatB){
	 	Vec3d Vec3d = eyePosition(e,floatB);
        Vec3d Vec3d1 = e.getLook(floatB);
        Vec3d Vec3d2 = Vec3d.addVector(Vec3d1.xCoord * floatA, Vec3d1.yCoord * floatA, Vec3d1.zCoord * floatA);
        return e.worldObj.rayTraceBlocks(Vec3d, Vec3d2, false, false, true);
	}
	
	public static Vec3d eyePosition(EntityPlayer e, float float1){
		 if (float1 == 1.0F)
	        {
	            return new Vec3d(e.posX, e.posY + (double)e.getEyeHeight(), e.posZ);
	        }
	        else
	        {
	            double d0 = e.prevPosX + (e.posX - e.prevPosX) * (double)float1;
	            double d1 = e.prevPosY + (e.posY - e.prevPosY) * (double)float1 + (double)e.getEyeHeight();
	            double d2 = e.prevPosZ + (e.posZ - e.prevPosZ) * (double)float1;
	            return new Vec3d(d0, d1, d2);
	        }
	}
}
