package com.insane.specificinventory;

import java.io.FileWriter;
import java.io.IOException;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * Created by Michael on 15/07/2014.
 */
public class SaveCommand extends CommandBase
{

	public static ItemStack[] inventoryCopy = new ItemStack[36];
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
	{
		return (par1ICommandSender instanceof EntityPlayer);
	}

	@Override
	public String getCommandName()
	{
		return "sisave";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender)
	{
		return "Saves current inventory to file";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring)
	{
		if (icommandsender instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) icommandsender;
			inventoryCopy = player.inventory.mainInventory.clone();

			try
			{
				FileWriter fw = new FileWriter(SpecificInventory.saveDat);

				String json = gson.toJson(player.inventory.writeToNBT(new NBTTagList()));
				fw.write(json);
				
				fw.flush();
				fw.close();
			}
			catch (IOException error)
			{
				error.printStackTrace();
			}
			
			player.addChatMessage("Inventory saved successfully!");
		}
	}

	@Override
	public int compareTo(Object o)
	{
		return 0;
	}
}
