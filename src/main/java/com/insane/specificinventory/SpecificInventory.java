package com.insane.specificinventory;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.common.Configuration;

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
    public static File recipeDat;
    private ItemStack[] recipeArrayStack = new ItemStack[2];

    public static boolean canModify;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		saveDat = new File(event.getSuggestedConfigurationFile().getParentFile().getAbsolutePath() + "/" + MODID + "/savedInv.dat");
		create(saveDat);
        recipeDat = new File(event.getSuggestedConfigurationFile().getParentFile().getAbsolutePath() + "/" + MODID + "/savedRecipe.dat");
        create(recipeDat);

        GameRegistry.registerPlayerTracker(new PlayerTracker());

        //Config
        File configFile = new File(event.getSuggestedConfigurationFile().getParentFile().getAbsolutePath() + "/" + MODID + "/specificInventory.cfg");
        create(configFile);

        Configuration config = new Configuration(configFile);
        config.load();
        canModify = config.get("canModify", "general", false, "Can modify starting inventory.").getBoolean(false);
        config.save();
	}

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        addRecipe();
    }
    
    /**
     * Adds custom recipe that was saved to file
     */
    public void addRecipe()
    {
    	try  {
            NBTTagList list = (NBTTagList) NBTTagList.readNamedTag(new DataInputStream(new FileInputStream(recipeDat)));
            if (list.tagCount()!=0) {
                for (int i = 0; i < list.tagCount(); i++) {
                    NBTTagCompound nbttc = (NBTTagCompound) list.tagAt(i);
                    recipeArrayStack[i] = ItemStack.loadItemStackFromNBT(nbttc);
                }
            }
        }
        catch (IOException error)  {
            error.printStackTrace();
        }
        if (recipeArrayStack[0] != null) {
            GameRegistry.addShapelessRecipe(recipeArrayStack[1], recipeArrayStack[0]);
        }
    }

	@Mod.EventHandler
	public void serverStart(FMLServerStartingEvent event)
	{
		ICommandManager server = MinecraftServer.getServer().getCommandManager();
        ((ServerCommandManager) server).registerCommand(new CommandSI());
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
