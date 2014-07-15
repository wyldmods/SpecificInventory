package com.insane.specificinventory;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by Michael on 15/07/2014.
 */
public class SaveCommand extends CommandBase {

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
            ItemStack test = player.inventory.mainInventory[0];
            if (test != null) {
                player.addChatMessage("It works!");
                System.out.println(test.getDisplayName());
                boolean status = player.inventory.addItemStackToInventory(test);
                System.out.println(status);
            }
        }
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
