package net.kirinnee.core;


import net.kirinnee.customFX.FXHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CommonProxy {
	
	/**
	 * Returns a side-appropriate EntityPlayer for use during message handling
	 */
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return ctx.getServerHandler().playerEntity;
	}

	/**
	 * Returns the current thread based on side during message handling,
	 * used for ensuring that the message is being handled by the main thread
	 */
	public IThreadListener getThreadFromContext(MessageContext ctx) {
		return ctx.getServerHandler().playerEntity.getServerWorld();
	}
	
	
	public void registerRenderers(Main ins) {
	}
	public void preInit(FMLPreInitializationEvent e){	
	}
	
	public void init(FMLInitializationEvent e) {
		
	}
	
	public void postInit(FMLPostInitializationEvent e){
		
	}

	public void customAnimationEffect(World world, Entity e, int fxid, double x, double y, double z, double motionx,
			double motiony, double motionz) {
		Minecraft.getMinecraft().effectRenderer.addEffect(FXHandler.getFx(Minecraft.getMinecraft().getTextureManager(), e, fxid, world, x,y,z, motionx, motiony, motionz));
	}
	

	void openCustomGui(int guiid, EntityPlayer e) {
		// TODO Auto-generated method stub
		
	}
	public void syncStaterino(EntityPlayer e) {
		// TODO Auto-generated method stub
		
	}

}
