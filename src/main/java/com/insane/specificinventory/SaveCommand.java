package com.insane.specificinventory;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;

/**
 * Created by Michael on 15/07/2014.
 */
public class SaveCommand extends CommandBase
{

	public static ItemStack[] inventoryCopy = new ItemStack[36];

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
				DataOutputStream out = new DataOutputStream(new FileOutputStream(SpecificInventory.saveDat));
				
				NBTTagList list = player.inventory.writeToNBT(new NBTTagList());
				
				NBTBase.writeNamedTag(list, out);
				
				out.close();
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
