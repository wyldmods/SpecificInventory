package com.insane.specificinventory;

import java.io.IOException;
import java.util.Scanner;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.nbt.NBTTagList;

/**
 * Created by Michael on 16/07/2014.
 */
public class LoadCommand extends CommandBase
{
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
	{
		return (par1ICommandSender instanceof EntityPlayer);
	}

	@Override
	public String getCommandName()
	{
		return "siload";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender)
	{
		return "Loads inventory from previous save";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring)
	{
		if (icommandsender instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) icommandsender;
			player.inventory.clearInventory(-1, -1); // Clears all inv slots
			try
			{
				Scanner scan = new Scanner(SpecificInventory.saveDat);
				String text = "";
				
				while (scan.hasNextLine())
				{
					String line = scan.nextLine();
					text += line + "\n";
				}
				
				//ItemStack[] inv = null;
                NBTTagList inv = null;

				//inv = gson.fromJson(text, new TypeToken<ItemStack[]>(){}.getType());
                //inv = gson.fromJson(text, new TypeToken<NBTTagList>(){}.getType());
                inv = gson.fromJson(text, NBTTagList.class);

				//player.inventory.mainInventory = inv;
                player.inventory.readFromNBT(inv);
				
				scan.close();
			}
			catch (IOException error)
			{
				error.printStackTrace();
			}
			
			player.addChatMessage("Inventory loaded successfully");
		}
	}

	@Override
	public int compareTo(Object o)
	{
		return 0;
	}
}
