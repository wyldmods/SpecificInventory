package com.insane.specificinventory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.StatCollector;

import org.apache.commons.lang3.ArrayUtils;

public class CommandSI extends CommandBase
{
	// last must always be help
	public static final String[] validCommands = { "save", "load", "addRecipe", "help" };

	@Override
	public String getCommandName()
	{
		return "si";
	}

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return SpecificInventory.canModify;
    }


    @Override
	public String getCommandUsage(ICommandSender player)
	{
		return StatCollector.translateToLocal("si.command.help.main");
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List addTabCompletionOptions(ICommandSender player, String[] args)
	{
		if (args.length == 1)
		{
			return getListOfStringsMatchingLastWord(args, validCommands);
		}
		else if (args.length == 2 && args[0].equals("help"))
		{
			return getListOfStringsMatchingLastWord(args, ArrayUtils.remove(validCommands, validCommands.length - 1));
		}
		else
		{
			return null;
		}
	}

	@Override
	public void processCommand(ICommandSender player, String[] args)
	{
		if (args.length < 1)
		{
			sendChatMessage(player, StatCollector.translateToLocal("si.command.notenoughargs"));
		}

		if (!(player instanceof EntityPlayer))
		{
			sendChatMessage(player, "si.command.notaplayer");
		}

		if (args[0].equals("help"))
		{
			if (args.length == 1)
			{
				sendChatMessageLocalized(player, StatCollector.translateToLocal("si.command.help.allcommands") + " " + Arrays.deepToString(ArrayUtils.remove(validCommands, validCommands.length - 1)));
			}
			else if (args[1].equals("save"))
			{
				sendChatMessage(player, "si.command.help.save");
			}
			else if (args[1].equals("load"))
			{
				sendChatMessage(player, "si.command.help.load");
			}
			else if (args[1].equals("addRecipe"))
			{
				sendChatMessage(player, "si.command.help.addrecipe");
			}
			else
			{
				sendChatMessage(player, "si.command.help.none");
			}
		}
		else
		{
			if (args[0].equals("save"))
			{
				doSave((EntityPlayer) player);
			}
			else if (args[0].equals("load"))
			{
                if (SpecificInventory.freshInventory) {
                    doLoad((EntityPlayer) player);
                } else {
                    doLoadWithoutReplace((EntityPlayer) player);
                }
			}
			else if (args[0].equals("addRecipe"))
			{
				doAddRecipe((EntityPlayer) player);
			}
			else
			{
				sendChatMessage(player, "si.command.nosuchcommand");
			}
		}
	}

	private static void doSave(EntityPlayer player)
	{
		try
		{
			DataOutputStream out = new DataOutputStream(new FileOutputStream(SpecificInventory.saveDat));

			NBTTagList list = player.inventory.writeToNBT(new NBTTagList());

			NBTBase.writeNamedTag(list, out);

			out.close();
		}
		catch (IOException error)
		{
			sendChatMessage(player, "si.command.save.fail");
			error.printStackTrace();
		}

		sendChatMessage(player, "si.command.save.success");
	}

	public static void doLoad(EntityPlayer player)
	{
		player.inventory.clearInventory(-1, -1); // Clears all inv slots

		try
		{
			NBTTagList list = (NBTTagList) NBTTagList.readNamedTag(new DataInputStream(new FileInputStream(SpecificInventory.saveDat)));
			player.inventory.readFromNBT(list);
		}
		catch (IOException error)
		{
			sendChatMessage(player, "si.command.load.fail");
			error.printStackTrace();
		}

		sendChatMessage(player, "si.command.load.success");
	}

    public static void doLoadWithoutReplace(EntityPlayer player)
    {
        try {
            NBTTagList list = (NBTTagList) NBTTagList.readNamedTag(new DataInputStream(new FileInputStream(SpecificInventory.saveDat)));
            InventoryPlayer ghost = new InventoryPlayer(null);
            ghost.readFromNBT(list);

            for (int i=0; i<36; i++) {
                if (ghost.mainInventory[i]!=null) {
                    player.inventory.addItemStackToInventory(ghost.mainInventory[i]);
                }
            }

        } catch (IOException error) {
            sendChatMessage(player, "si.command.load.fail");
            error.printStackTrace();
        }


    }

	private static void doAddRecipe(EntityPlayer player)
	{
		try
		{
			DataOutputStream out = new DataOutputStream(new FileOutputStream(SpecificInventory.recipeDat));
			NBTTagCompound ingredient = player.inventory.mainInventory[0].writeToNBT(new NBTTagCompound());
			NBTTagCompound result = player.inventory.mainInventory[1].writeToNBT(new NBTTagCompound());

			NBTTagList list = new NBTTagList();
			list.appendTag(ingredient);
			list.appendTag(result);

			NBTBase.writeNamedTag(list, out);
			out.close();
			
			SpecificInventory.instance.addRecipe();
		}
		catch (IOException error)
		{
			sendChatMessage(player, "si.command.addrecipe.fail");
			error.printStackTrace();
		}
		
		sendChatMessage(player, "si.command.addrecipe.success");
	}

	@Override
	public int compareTo(Object o)
	{
		if (o instanceof ICommand)
		{
			return this.compareTo((ICommand) o);
		}
		else
		{
			return 0;
		}
	}

	@Override
	public boolean equals(Object obj)
	{
		return this.compareTo(obj) == 0;
	}

	private static void sendChatMessage(ICommandSender player, String unloc)
	{
		sendChatMessageLocalized(player, StatCollector.translateToLocal(unloc));
	}

	private static void sendChatMessageLocalized(ICommandSender player, String loc)
	{
		player.sendChatToPlayer(new ChatMessageComponent().addText(loc));
	}
}
