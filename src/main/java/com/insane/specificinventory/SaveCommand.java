package com.insane.specificinventory;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.io.FileWriter;
import java.io.IOException;
import com.google.gson.Gson;

/**
 * Created by Michael on 15/07/2014.
 */
public class SaveCommand extends CommandBase {

    public static ItemStack[] inventoryCopy = new ItemStack[36];
    private Gson gson = new Gson();

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return (par1ICommandSender instanceof EntityPlayer);
    }

    @Override
    public String getCommandName() {
        return "sisave";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {
        return "Saves current inventory to file";
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring) {
        if (icommandsender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) icommandsender;
            //ItemStack test = player.inventory.mainInventory[0];
            inventoryCopy = player.inventory.mainInventory.clone();
            player.addChatMessage("Inventory saved successfully!");

            try {
                FileWriter inventoryFile = new FileWriter(SpecificInventory.directory + "/inventorySave.txt");

                for (int i=0; i<35; i++) {
                    if (inventoryCopy[i]!=null) {
                        String json = gson.toJson(inventoryCopy[i].writeToNBT(new NBTTagCompound()));
                        inventoryFile.write(json + "\n");
                    }
                }
                inventoryFile.flush();
                inventoryFile.close();
            }
            catch(IOException error) {
                error.printStackTrace();
            }
        }
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
