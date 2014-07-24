package com.insane.specificinventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.IPlayerTracker;

/**
 * Created by Michael on 16/07/2014.
 */
public class PlayerTracker implements IPlayerTracker {
    @Override
    public void onPlayerLogin(EntityPlayer player) {
        if (player != null && !player.worldObj.isRemote) {
            NBTTagCompound entityData = player.getEntityData();
            NBTTagCompound d;
            d = entityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
            if (!d.hasKey(SpecificInventory.MODID + "firstSpawn")) { //The tag must have been set by us, so we only need to check existence.
                d.setBoolean(SpecificInventory.MODID + "firstSpawn", false);
                CommandSI.doLoad(player);
                entityData.setCompoundTag(EntityPlayer.PERSISTED_NBT_TAG, d);
            }
        }
    }

    @Override
    public void onPlayerLogout(EntityPlayer player) {
    	;
    }

    @Override
    public void onPlayerChangedDimension(EntityPlayer player) {
    	;
    }

    @Override
    public void onPlayerRespawn(EntityPlayer player) {
    	;
    }
}
