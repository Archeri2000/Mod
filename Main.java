package net.kirinnee.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.kirinnee.core.hiddenstats.HStatsDataOrganizer;
import net.kirinnee.core.playerstats.StatsDataOrganizer;
import net.kirinnee.gui.HUD;
import net.kirinnee.packets.fx.FXClientHandler;
import net.kirinnee.packets.fx.FXClientHandlerChannel;
import net.kirinnee.packets.fx.FXClientPacket;
import net.kirinnee.packets.fx.FXClientPacketChannel;
import net.kirinnee.packets.skills.SkillCDStateHandler;
import net.kirinnee.packets.skills.SkillCDStatePacket;
import net.kirinnee.packets.skills.SkillCastFromClientHandler;
import net.kirinnee.packets.skills.SkillCastFromClientPackets;
import net.kirinnee.packets.skills.SyncHotbarHandler;
import net.kirinnee.packets.skills.SyncHotbarPackets;
import net.kirinnee.packets.skills.SyncSkillSlotHandlerFromClient;
import net.kirinnee.packets.skills.SyncSkillSlotPacketsFromClient;
import net.kirinnee.packets.skills.channel.PlayerChannelFromClientHandler;
import net.kirinnee.packets.skills.channel.PlayerChannelFromClientPacket;
import net.kirinnee.packets.skills.channel.PlayerChannelHandler;
import net.kirinnee.packets.skills.channel.PlayerChannelPacket;
import net.kirinnee.packets.skills.channel.SkillChnlStateHandler;
import net.kirinnee.packets.skills.channel.SkillChnlStatePacket;
import net.kirinnee.packets.stat.SyncStatsHandler;
import net.kirinnee.packets.stat.SyncStatsPackets;
import net.kirinnee.packets.stat.draw.SyncdStatOnClientHandler;
import net.kirinnee.packets.stat.draw.SyncdStatOnClientPacket;
import net.kirinnee.packets.stat.ini.SynciStatOnClientHandler;
import net.kirinnee.packets.stat.ini.SynciStatOnClientPacket;
import net.kirinnee.packets.stat.modifications.RunStatUtilOnServerHandler;
import net.kirinnee.packets.stat.modifications.RunStatUtilOnServerPacket;
import net.kirinnee.packets.stat.modifications.SetEnhancementOnServerHandler;
import net.kirinnee.packets.stat.modifications.SetEnhancementOnServerPacket;
import net.kirinnee.packets.stat.modifications.SetStatOnServerHandler;
import net.kirinnee.packets.stat.modifications.SetStatOnServerPacket;
import net.kirinnee.packets.stat.saved.SyncStatOnClientHandler;
import net.kirinnee.packets.stat.saved.SyncStatOnClientPacket;
import net.kirinnee.packets.stat.saved.SyncStatOnServerHandler;
import net.kirinnee.packets.stat.saved.SyncStatOnServerPacket;
import net.kirinnee.skills.core.SkillCapability;
import net.kirinnee.skills.core.SkillEventHandler;
import net.kirinnee.things.EnhanceCapability;
import net.kirinnee.things.FireArmDataCapability;
import net.kirinnee.things.ItemEventHandler;
import net.kirinnee.things.ToolDataCapability;
import net.kirinnee.zone.ZoneHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;


@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION)
public class Main {

	public static final String MODID = "kirinoCore";
	public static final String VERSION = "1.0.3"; // major . minor . snapshot
	public static final String NAME = "Linear Reborn";

	/*
	 * ============================= PROXY SETUP ==============================
	 */
	@SidedProxy(clientSide = "net.kirinnee.core.ClientProxy", serverSide = "net.kirinnee.core.ServerProxy")
	public static CommonProxy proxy;
	public static SimpleNetworkWrapper snw;

	@Instance(Main.MODID)
	public static Main instance = new Main();

	public static boolean timestop = false;
	
	public static List<Entity> unstop = new ArrayList<Entity>();

	/*
	 * ============================= PREINIT ==============================
	 */
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		//Obfuscation Helper Debug
		Field[] fieldList = MinecraftServer.class.getDeclaredFields();
		for (int i = 0; i < fieldList.length; i++) {
		        System.out.println("[" + i + "] " + fieldList[ i ]);
		}
		//FMLCommonHandler.instance().exitJava(0, true);
		
		//Remove Crafting Recipes
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		Iterator<IRecipe> iterated = recipes.iterator();
				 
		while (iterated.hasNext()) {
			ItemStack is = iterated.next().getRecipeOutput();
			for (int ii = 0; ii < ItemEventHandler.unwanted.size(); ii++){
				if (is != null && is.getItem().equals(ItemEventHandler.unwanted.get(ii))){
					iterated.remove();
				}
			}
		};

		// PREINIT SKILL CAPABILITIES
		SkillCapability.PreInit(event);
		StatCapabilityManager.PreInit(event);
		EnhanceCapability.PreInit(event);
		ToolDataCapability.PreInit(event);
		FireArmDataCapability.PreInit(event);
		
		// PREINIT PROXY
		this.proxy.preInit(event);

		// REGISTERING SIMPLE NW AND PACKETS
		// ==========================================
		snw = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
		snw.registerMessage(SyncStatsHandler.class, SyncStatsPackets.class, 0, Side.CLIENT);
		snw.registerMessage(SyncHotbarHandler.class, SyncHotbarPackets.class, 1, Side.CLIENT);
		snw.registerMessage(SkillCastFromClientHandler.class, SkillCastFromClientPackets.class, 2, Side.SERVER);
		snw.registerMessage(SyncStatOnClientHandler.class, SyncStatOnClientPacket.class, 3, Side.CLIENT);
		snw.registerMessage(SyncStatOnServerHandler.class, SyncStatOnServerPacket.class, 4, Side.SERVER);
		snw.registerMessage(SynciStatOnClientHandler.class, SynciStatOnClientPacket.class, 5, Side.CLIENT);
		snw.registerMessage(SyncdStatOnClientHandler.class, SyncdStatOnClientPacket.class, 6, Side.CLIENT);
		snw.registerMessage(SkillCDStateHandler.class, SkillCDStatePacket.class, 7, Side.CLIENT);
		snw.registerMessage(RunStatUtilOnServerHandler.class, RunStatUtilOnServerPacket.class, 8, Side.SERVER);
		snw.registerMessage(SetStatOnServerHandler.class, SetStatOnServerPacket.class, 9, Side.SERVER);
		snw.registerMessage(FXClientHandler.class, FXClientPacket.class, 10, Side.CLIENT);
		snw.registerMessage(FXClientHandlerChannel.class, FXClientPacketChannel.class, 11, Side.CLIENT);
		snw.registerMessage(SyncSkillSlotHandlerFromClient.class, SyncSkillSlotPacketsFromClient.class, 12,Side.SERVER);
		snw.registerMessage(SetEnhancementOnServerHandler.class, SetEnhancementOnServerPacket.class, 13, Side.SERVER);
		snw.registerMessage(PlayerChannelFromClientHandler.class, PlayerChannelFromClientPacket.class, 14, Side.SERVER);
		snw.registerMessage(PlayerChannelHandler.class, PlayerChannelPacket.class, 15, Side.CLIENT);
		snw.registerMessage(SkillChnlStateHandler.class, SkillChnlStatePacket.class, 16, Side.CLIENT);
	}

	/*
	 * ============================= INIT ==============================
	 */
	@EventHandler
	public void init(FMLInitializationEvent event) {
		// REGISTERING KEY HANDLER
		// ====================================================
		if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
			MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
			KeyBindings.init();
		}
	
		ZoneHandler handler3 = ZoneHandler.getHandler();
		MinecraftForge.EVENT_BUS.register(handler3);		
		

		// INTIALIZING EVENT HANDLERS
		// ===========================================
		CommonEventHandler handler = new CommonEventHandler();
		MinecraftForge.EVENT_BUS.register(handler);
		MinecraftForge.EVENT_BUS.register(new StatsDataOrganizer());
		MinecraftForge.EVENT_BUS.register(new HStatsDataOrganizer());
		MinecraftForge.EVENT_BUS.register(new SkillEventHandler());
		MinecraftForge.EVENT_BUS.register(new Controller());
		MinecraftForge.EVENT_BUS.register(new ItemEventHandler());

		// INITIALIZING PROXY
		// ==========================================================
		proxy.init(event);

		// REGISTER GUI HANDLER
		// =======================================================
	

	}

	/*
	 * ============================= POST INIT ==============================
	 */
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

		if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
			// REGISTER GUI RENDER
			MinecraftForge.EVENT_BUS.register(new HUD(Minecraft.getMinecraft()));
		}
	}
	
	/*
	 * @EventHandler public void serverLoad(FMLServerStartingEvent event) { //
	 * register server commands event.registerServerCommand(new
	 * SetPlayerStatCommand()); event.registerServerCommand(new
	 * PlayerResetStats()); event.registerServerCommand(new StatList());
	 * event.registerServerCommand(new RewardExp());
	 * event.registerServerCommand(new LoadJsonZoneCommand());
	 * event.registerServerCommand(new RemoveZoneCommand());
	 * event.registerServerCommand(new ReplaceCircularZoneCommand());
	 * event.registerServerCommand(new ReplaceRectZoneCommand());
	 * event.registerServerCommand(new SetCircularZoneCommand());
	 * event.registerServerCommand(new SetRectZoneCommand());
	 * event.registerServerCommand(new ZoneList());
	 * event.registerServerCommand(new SetPriorityZoneCommand());
	 * event.registerServerCommand(new ToggleJsonZoneStats());
	 * event.registerServerCommand(new StatsResetAll());
	 * event.registerServerCommand(new UnlockAllCommand());
	 * event.registerServerCommand(new TargetPlayerResetStats());
	 * 
	 * }
	 */

}