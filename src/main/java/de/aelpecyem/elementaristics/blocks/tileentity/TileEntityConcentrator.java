package de.aelpecyem.elementaristics.blocks.tileentity;

import de.aelpecyem.elementaristics.Elementaristics;
import de.aelpecyem.elementaristics.init.ModItems;
import de.aelpecyem.elementaristics.items.base.ItemEssence;
import de.aelpecyem.elementaristics.misc.elements.Aspects;
import de.aelpecyem.elementaristics.networking.PacketHandler;
import de.aelpecyem.elementaristics.networking.concentrator.PacketUpdateConcentrator;
import de.aelpecyem.elementaristics.particles.ParticleGeneric;
import de.aelpecyem.elementaristics.recipe.ConcentratorRecipes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityConcentrator extends TileEntity implements ITickable {


    public EnumParticleTypes particle = EnumParticleTypes.CRIT_MAGIC;
    public ItemStackHandler inventory = new ItemStackHandler(2) {

        @Override
        protected void onContentsChanged(int slot) {
            if (!inventory.getStackInSlot(1).isEmpty()) {
                if (!(inventory.getStackInSlot(1).getItem() instanceof ItemEssence)) {
                    EntityItem item = new EntityItem(world, pos.getX(), pos.getY() + 1.5, pos.getZ(), inventory.getStackInSlot(slot));
                    world.spawnEntity(item);
                    inventory.setStackInSlot(slot, ItemStack.EMPTY);
                }
            }

        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

    };
    public int tickCount;
    public long lastChangeTime;
    public ItemStack stackCrafting = ItemStack.EMPTY;
    private boolean crafting;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("inventory", inventory.serializeNBT());
        compound.setInteger("tickCount", tickCount);
        compound.setLong("lastChangeTime", lastChangeTime);
        compound.setBoolean("crafting", crafting);
        compound.setTag("stackCrafting", stackCrafting.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        tickCount = compound.getInteger("tickCount");
        lastChangeTime = compound.getInteger("lastChangeTime");
        crafting = compound.getBoolean("crafting");
        stackCrafting.deserializeNBT(compound.getCompoundTag("stackCrafting"));
        super.readFromNBT(compound);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getPos(), getPos().add(1, 2, 1));
    }

    public boolean isCrafting() {
        return crafting;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) inventory : super.getCapability(capability, facing);
    }


    @Override
    public void update() {
        if (!world.isRemote) {
            lastChangeTime = world.getTotalWorldTime();
            PacketHandler.sendToAllAround(world, pos, 64, new PacketUpdateConcentrator(TileEntityConcentrator.this));
        }
        if (!inventory.getStackInSlot(0).isEmpty() && !inventory.getStackInSlot(1).isEmpty()) {
            tickCount++;
            doParticleShow();

            if (tickCount >= 300) {
                if (ConcentratorRecipes.getRecipeForInput(inventory.getStackInSlot(0)) != null) {
                    if (ConcentratorRecipes.getRecipeForInput(inventory.getStackInSlot(0)).inputInfluencing.isItemEqual(inventory.getStackInSlot(1))) {
                        inventory.setStackInSlot(0, ConcentratorRecipes.getRecipeForInput(inventory.getStackInSlot(0)).output);
                    } else {
                        inventory.setStackInSlot(0, new ItemStack(ModItems.maganized_matter));
                        inventory.setStackInSlot(1, ItemStack.EMPTY);
                    }
                } else {
                    inventory.setStackInSlot(0, new ItemStack(ModItems.chaotic_matter));
                    inventory.setStackInSlot(1, ItemStack.EMPTY);
                }
                tickCount = 0;
            }
        } else {
            tickCount = 0;
        }
    }

    private void doParticleShow() {
        Elementaristics.proxy.generateGenericParticles(new ParticleGeneric(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, world.rand.nextGaussian() * 0.02F, Math.abs(world.rand.nextGaussian()) * 0.06F, world.rand.nextGaussian() * 0.02F, Aspects.getElementById(inventory.getStackInSlot(1).getMetadata()).getColor(), 2, 10, 0, true, false, 0.5F));
    }


}
