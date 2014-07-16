package com.insane.specificinventory;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;

import java.io.File;
import java.io.IOException;

/**
 * Created by Michael on 15/07/2014.
 */

@Mod(modid="SpecificInventory", name="Specific Inventory", version="1.0")
@NetworkMod(clientSideRequired = true)
public class SpecificInventory {
    @Mod.Instance("SpecificInventory")
    public static SpecificInventory instance;
    @SidedProxy(clientSide="com.insane.specificinventory.client.ClientProxy", serverSide="com.insane.specificinventory.CommonProxy")
    public static CommonProxy proxy;

    public static String directory;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        directory = event.getModConfigurationDirectory().toString();
        File file = new File(event.getModConfigurationDirectory() + "/SpecificInventory.txt");
        try {
            file.createNewFile();
        }
        catch (IOException e) {
            System.out.print("Could not create file for Specific Inventory. Reason: ");
            System.out.println(e);
        }
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        ICommandManager server = MinecraftServer.getServer().getCommandManager();
       // ICommandManager command = server.getCommandManager();
        ((ServerCommandManager) server).registerCommand(new SaveCommand());
        ((ServerCommandManager) server).registerCommand(new LoadCommand());
    }
}

