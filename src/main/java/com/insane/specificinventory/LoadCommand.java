package com.insane.specificinventory;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagList;

/**
 * Created by Michael on 16/07/2014.
 */
public class LoadCommand extends CommandBase
{
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
	public void processCommand(ICommandSender icommandsender, String[] astring) {
        if (icommandsender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) icommandsender;
            replaceInventory(player);
        }
    }

	@Override
	public int compareTo(Object o)
	{
		return 0;
	}

    public static void replaceInventory(EntityPlayer player) {
        player.inventory.clearInventory(-1, -1); // Clears all inv slots
        try  {
            NBTTagList list = (NBTTagList) NBTTagList.readNamedTag(new DataInputStream(new FileInputStream(SpecificInventory.saveDat)));
            player.inventory.readFromNBT(list);
        }
        catch (IOException error)  {
            error.printStackTrace();
        }

        player.addChatMessage("Inventory loaded successfully");
    }
}
