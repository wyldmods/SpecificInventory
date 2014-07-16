package com.insane.specificinventory;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;

import java.io.File;
import java.io.IOException;

/**
 * Created by Michael on 15/07/2014.
 */

@Mod(modid = SpecificInventory.MODID, name = "Specific Inventory", version = "1.0")
@NetworkMod(clientSideRequired = true)
public class SpecificInventory
{
	public static final String MODID = "SpecificInventory";
	
	@Mod.Instance("SpecificInventory")
	public static SpecificInventory instance;
	@SidedProxy(clientSide = "com.insane.specificinventory.client.ClientProxy", serverSide = "com.insane.specificinventory.CommonProxy")
	public static CommonProxy proxy;

	public static File saveDat;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		saveDat = new File(event.getSuggestedConfigurationFile().getParentFile().getAbsolutePath() + "/" + MODID + "/savedInv.txt");
		create(saveDat);

        GameRegistry.registerPlayerTracker(new PlayerTracker());
	}

	@Mod.EventHandler
	public void serverStart(FMLServerStartingEvent event)
	{
		ICommandManager server = MinecraftServer.getServer().getCommandManager();
		// ICommandManager command = server.getCommandManager();
		((ServerCommandManager) server).registerCommand(new SaveCommand());
		((ServerCommandManager) server).registerCommand(new LoadCommand());
	}
	
	private void create(File file)
	{
		if (!file.exists())
		{
			try
			{
				file.getParentFile().mkdirs();
				file.createNewFile();
				System.out.println(file.getAbsolutePath());
			}
			catch (IOException e)
			{
				System.out.print("Could not create " + file.getAbsolutePath() + ". Reason: ");
				System.out.println(e);
			}
		}
	}
}
