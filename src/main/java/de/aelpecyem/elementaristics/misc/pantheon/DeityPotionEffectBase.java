package de.aelpecyem.elementaristics.misc.pantheon;

import com.google.common.base.Predicate;
import de.aelpecyem.elementaristics.blocks.tileentity.TileEntityAltar;
import de.aelpecyem.elementaristics.blocks.tileentity.pantheon.TileEntityDeityShrine;
import de.aelpecyem.elementaristics.capability.player.IPlayerCapabilities;
import de.aelpecyem.elementaristics.capability.player.PlayerCapProvider;
import de.aelpecyem.elementaristics.init.SoulInit;
import de.aelpecyem.elementaristics.misc.elements.Aspect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;
import java.util.List;

public class DeityPotionEffectBase extends DeityBase {
    Potion effect;
    boolean playersOnly;

    public DeityPotionEffectBase(long tickTimeBegin, ResourceLocation name, int color, Potion effect) {
        super(tickTimeBegin, name, color);
        this.effect = effect;
        this.playersOnly = true;
    }
    public DeityPotionEffectBase(long tickTimeBegin, ResourceLocation name, int color, Potion effect, boolean playersOnly) {
        super(tickTimeBegin, name, color);
        this.effect = effect;
        this.playersOnly = playersOnly;
    }

    @Override
    public void symbolEffect(TileEntityDeityShrine te) {
        if (te.storage.extractIfPossible(5)) {
            PotionEffect p = new PotionEffect(effect, 400, 0, false, false);
        List<EntityLivingBase> targets = te.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(te.getPos().getX() - 24, te.getPos().getY() - 12, te.getPos().getZ() - 24, te.getPos().getX() + 24, te.getPos().getY() + 12, te.getPos().getZ() + 24), new Predicate<EntityLivingBase>() {
            @Override
            public boolean apply(@Nullable EntityLivingBase input) {
                if(playersOnly && input instanceof EntityPlayer)
                return true;
                else{
                    if (playersOnly)
                    return false;
                }
                return true;
            }
        });

            if (!targets.isEmpty()) {
                for (EntityLivingBase player : targets) {
                    player.addPotionEffect(p);
                    if (player.hasCapability(PlayerCapProvider.ELEMENTARISTICS_CAP, null)) {
                        IPlayerCapabilities cap = player.getCapability(PlayerCapProvider.ELEMENTARISTICS_CAP, null);
                        if (cap.getTimeStunted() < 1) {
                            cap.fillMagan(0.05F);
                        }
                    }
                }
            }
            super.symbolEffect(te);
        }
    }


    @Override
    public void sacrificeEffect(TileEntityDeityShrine te) {

    }

    @Override
    public void activeStatueEffect(TileEntityDeityShrine te) {

    }
}
