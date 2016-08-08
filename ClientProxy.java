package net.kirinnee.core;

import java.util.Random;

import net.kirinnee.customFX.FXHandler;
import net.kirinnee.gui.PlayerStats;
import net.kirinnee.mobs.MobClientManager;
import net.minecraft.client.Minecraft;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientProxy extends CommonProxy{
	// ClientProxy:
	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return (ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntity(ctx));
	}

	@Override
	public IThreadListener getThreadFromContext(MessageContext ctx) {
		return (ctx.side.isClient() ? Minecraft.getMinecraft() : super.getThreadFromContext(ctx));
	}
		
    @Override
    public void registerRenderers(Main ins) {
    }
	@Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
    }

	@Override
	public void customAnimationEffect(World world,Entity e, int fxid, double x, double y, double z, double motionx, double motiony, double motionz){
		Minecraft.getMinecraft().effectRenderer.addEffect(FXHandler.getFx(Minecraft.getMinecraft().getTextureManager(), e, fxid, world, x,y,z, motionx, motiony, motionz));
	}
		
    @Override
    public void init(FMLInitializationEvent e) {
		ClientEventHandler client = new ClientEventHandler();
		MinecraftForge.EVENT_BUS.register(client);
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
    }
    
    @Override
    void openCustomGui(int guiid,EntityPlayer e){
    	switch (guiid){
    		case 1: 
    			PlayerStats ps = new PlayerStats(e);
    			Minecraft.getMinecraft().displayGuiScreen(ps); 
    			break;
    	}
    }
}