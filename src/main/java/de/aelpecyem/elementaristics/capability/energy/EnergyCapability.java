package de.aelpecyem.elementaristics.capability.energy;

import de.aelpecyem.elementaristics.init.SoulInit;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.EnergyStorage;

public class EnergyCapability extends EnergyStorage {

    public EnergyCapability(int capacity) {
        super(capacity);
    }

    public EnergyCapability(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public EnergyCapability(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public EnergyCapability(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public void readFromNBT(NBTTagCompound compound) {
        this.capacity = compound.getInteger("capacity");
        this.energy = compound.getInteger("energy");
        this.maxExtract = compound.getInteger("maxExtract");
        this.maxReceive = compound.getInteger("maxReceive");
    }

    public void writeToNBT(NBTTagCompound compound) {
        compound.setInteger("capacity", this.capacity);
        compound.setInteger("energy", this.energy);
        compound.setInteger("maxExtract", this.maxExtract);
        compound.setInteger("maxReceive", this.maxReceive);
    }


}