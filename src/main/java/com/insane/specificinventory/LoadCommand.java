package com.insane.specificinventory;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by Michael on 16/07/2014.
 */
public class LoadCommand extends CommandBase {

    private Gson gson;
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return (par1ICommandSender instanceof EntityPlayer);
    }

    @Override
    public String getCommandName() {
        return "siload";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {
        return "Loads inventory from previous save";
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring) {
        if (icommandsender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) icommandsender;
            player.inventory.clearInventory(-1, -1); //Clears all inventory slots
            player.addChatMessage("Inventory loaded successfully");
           try {
                BufferedReader reader = new BufferedReader(new FileReader(new File((SpecificInventory.directory + "/inventorySave.txt"))));
                String input;
                int i=0;
                while ((input = reader.readLine()) != null) {
                    System.out.println(input);
                    ItemStack item = ItemStack.loadItemStackFromNBT(gson.fromJson(input, NBTTagCompound.class));
                    System.out.println(item.getDisplayName());
                    player.inventory.mainInventory[i]=gson.fromJson(input,ItemStack.class);
                    System.out.println(i);
                    i++;
                }

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
