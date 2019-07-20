package de.aelpecyem.elementaristics.items.base.burnable;

import de.aelpecyem.elementaristics.entity.EntityExplosionProjectile;
import de.aelpecyem.elementaristics.items.base.ItemBase;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

public class ItemBurnableAffectingBase extends ItemBase {

    public ItemBurnableAffectingBase(String name) {
        super(name);
    }

    public void affect(World world, double posX, double posY, double posZ) {
        List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(new BlockPos(posX - 5, posY - 2, posZ - 5), new BlockPos(posX + 5, posY + 5, posZ + 5)));
        if (players.size() > 0) {
            Iterator var3 = players.iterator();
            while (var3.hasNext()) {
                EntityPlayer e = (EntityPlayer) var3.next();
                affectPlayer(world, posX, posY, posZ, e);
            }
        }
    }

    public void affectPlayer(World world, double posX, double posY, double posZ, EntityPlayer player) {
        player.world.playSound(posX, posY, posZ, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.AMBIENT, 1, 1.1F, true);

    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        if (entityItem.isBurning()) {
            affect(entityItem.world, entityItem.posX, entityItem.posY, entityItem.posZ);
            entityItem.setDead();
        }
        return super.onEntityItemUpdate(entityItem);
    }
}
