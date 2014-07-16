package com.insane.specificinventory;

import com.insane.specificinventory.SpecificInventory;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Michael on 16/07/2014.
 */
public class SaveRecipeCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "sirecipe";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return (par1ICommandSender instanceof EntityPlayer);
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {
        return "Saves a custom recipe using items in slot 0 and slot 1.";
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring) {
        if (icommandsender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) icommandsender;

            try {
                DataOutputStream out = new DataOutputStream(new FileOutputStream(SpecificInventory.recipeDat));
                NBTTagCompound ingredient = player.inventory.mainInventory[0].writeToNBT(new NBTTagCompound());
                NBTTagCompound result = player.inventory.mainInventory[1].writeToNBT(new NBTTagCompound());

                NBTTagList list = new NBTTagList();
                list.appendTag(ingredient);
                list.appendTag(result);

                NBTBase.writeNamedTag(list, out);
                out.close();
            }
            catch (IOException error) {
                error.printStackTrace();
            }
        }
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
