package de.aelpecyem.elementaristics.misc.rites.misc;

import de.aelpecyem.elementaristics.Elementaristics;
import de.aelpecyem.elementaristics.entity.nexus.EntityDimensionalNexus;
import de.aelpecyem.elementaristics.misc.elements.Aspects;
import de.aelpecyem.elementaristics.misc.rites.RiteBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class RiteNight extends RiteBase {

    public RiteNight() {
        super(new ResourceLocation(Elementaristics.MODID, "rite_night"), 400, 0.5F, 12, Aspects.ice, Aspects.water);
    }

    @Override
    public void doMagic(EntityDimensionalNexus nexus) {
        List<EntitySheep> targets = nexus.world.getEntitiesWithinAABB(EntitySheep.class, nexus.getEntityBoundingBox().grow(4));
        boolean flag = false;
        for (EntitySheep sheepAffected : targets) {
            if (sheepAffected.getFleeceColor() == EnumDyeColor.BLACK) {
                sheepAffected.setDropItemsWhenDead(false);
                sheepAffected.attackEntityFrom(DamageSource.GENERIC, 200);
                flag = true;
                break;
            }
        }
        if (flag) {
            nexus.world.setWorldTime(18000);
        }
    }

    @Override
    public void onRitual(EntityDimensionalNexus nexus) {
        if (nexus.getRiteTicks() % 2 == 0) {
            Elementaristics.proxy.generateGenericParticles(nexus.world, nexus.posX, nexus.posY + 0.5F, nexus.posZ, Aspects.water.getColor(), 3, 100, -0.1F, false, false);
            Elementaristics.proxy.generateGenericParticles(nexus.world, nexus.posX, nexus.posY + 0.5F, nexus.posZ, Aspects.ice.getColor(), 2, 80, -0.05F, false, false);
        }
    }
}
