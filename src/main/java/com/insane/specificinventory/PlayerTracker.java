package com.insane.specificinventory;

import cpw.mods.fml.common.IPlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;

/**
 * Created by Michael on 16/07/2014.
 */
public class PlayerTracker implements IPlayerTracker {
    @Override
    public void onPlayerLogin(EntityPlayer player) {
        NBTTagCompound entityData = player.getEntityData();
        NBTTagCompound d;
        if (!entityData.hasKey(player.PERSISTED_NBT_TAG)) {
            d = entityData.getCompoundTag(player.PERSISTED_NBT_TAG);
            if (!d.hasKey(SpecificInventory.MODID+"firstSpawn")) { //The tag must have been set by us, so we only need to check existence.
                d.setBoolean(SpecificInventory.MODID+"firstSpawn", false);
                LoadCommand.replaceInventory(player);
                entityData.setCompoundTag(player.PERSISTED_NBT_TAG, d);
            }
        }
    }

    @Override
    public void onPlayerLogout(EntityPlayer player) {

    }

    @Override
    public void onPlayerChangedDimension(EntityPlayer player) {

    }

    @Override
    public void onPlayerRespawn(EntityPlayer player) {

    }
}
