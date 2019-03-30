package de.aelpecyem.elementaristics.misc.rites.killing;

import de.aelpecyem.elementaristics.Elementaristics;
import de.aelpecyem.elementaristics.capability.IPlayerCapabilities;
import de.aelpecyem.elementaristics.capability.PlayerCapProvider;
import de.aelpecyem.elementaristics.capability.souls.Soul;
import de.aelpecyem.elementaristics.init.ModItems;
import de.aelpecyem.elementaristics.misc.elements.Aspect;
import de.aelpecyem.elementaristics.misc.rites.RiteBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class RiteSacrifice extends RiteBase{//Soul's Conflagration
    Aspect aspect;
    Soul soul;
    DamageSource dmgSource;
    public RiteSacrifice(String name, Aspect aspectInvoked, Soul soulToAscend, DamageSource damageSource) {
        super(new ResourceLocation(Elementaristics.MODID, name), 100,1, 8, soulToAscend, aspectInvoked); //set itempower higher, once the artifacts exist (also set maganPerTick higher)
        aspect = aspectInvoked;
        soul = soulToAscend;
        dmgSource = damageSource;
    }

    public RiteSacrifice(String name, Aspect aspectInvoked, DamageSource damageSource) {
        super(new ResourceLocation(Elementaristics.MODID, name), 100,1, 8, aspectInvoked); //set itempower higher, once the artifacts exist (also set maganPerTick higher)
        aspect = aspectInvoked;
        dmgSource = damageSource;
    }

    @Override
    public void doMagic(World world, BlockPos pos, EntityPlayer player) {
        if (!world.isRemote) {
            int killCount = 0;
            Elementaristics.proxy.generateGenericParticles(player, aspect.getColor(), 4, 100, 0, false, true);
            if (isSoulSpecific()) {
                if (player.hasCapability(PlayerCapProvider.ELEMENTARISTICS_CAP, null)) {
                    IPlayerCapabilities caps = player.getCapability(PlayerCapProvider.ELEMENTARISTICS_CAP, null);
                    if (caps.getPlayerAscensionStage() > 6) {
                        if (caps.getSoulId() == soul.getId()) {
                            //do final ascension
                            return;
                        }
                    }
                }
            }
            ItemStack stack = new ItemStack(ModItems.essence, 1, aspect.getId());
            EntityPlayer sacrifice = world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 5, false);
            if (sacrifice != null){
                sacrifice.attackEntityFrom(dmgSource, 100);
                killCount++;
            }
            List<Entity> entities = world.getEntitiesInAABBexcluding(null, new AxisAlignedBB(pos.getX() - 2, pos.getY() - 1, pos.getZ() - 2, pos.getX() + 2, pos.getY() + 2, pos.getZ() + 2), null);
            System.out.println(entities);
            for (int i = 0; i < 5; i++){
                if (i >= entities.size()){
                    break;
                }Entity entity = entities.get(i);
                if (entity instanceof EntityLivingBase && entity.isNonBoss()) {
                    entity.attackEntityFrom(dmgSource, 22);
                    if (world.rand.nextBoolean()) {
                        world.spawnEntity(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, stack));
                    }

                }

            }
            world.spawnEntity(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, new ItemStack(stack.getItem(), killCount, aspect.getId())));
        }
    }

    @Override
    public void onRitual(World world, BlockPos altarPos, List<EntityPlayer> players, int tickCount) {
        if (tickCount % 2 == 0) {
            if (world.getClosestPlayer(altarPos.getX(), altarPos.getY(), altarPos.getZ(), 5, false) != null) {
                EntityPlayer player = world.getClosestPlayer(altarPos.getX(), altarPos.getY(), altarPos.getZ(), 5, false);
                List<Entity> entities = world.getEntitiesInAABBexcluding(player, new AxisAlignedBB(altarPos.getX() - 2, altarPos.getY() - 1, altarPos.getZ() - 2, altarPos.getX() + 2, altarPos.getY() + 2, altarPos.getZ() + 2), null);
                for (Entity entity : entities) {
                    if (entity instanceof EntityLiving && entity.isNonBoss()) {
                        Elementaristics.proxy.generateGenericParticles(player, aspect.getColor(), 2, 100, 0, false, true);
                    }
                }
                Elementaristics.proxy.generateGenericParticles(world, altarPos.getX() + 0.5F, altarPos.getY() + 1.2F, altarPos.getZ() + 0.5F, aspect.getColor(), 3, 60, 0, false, false);

                Elementaristics.proxy.generateGenericParticles(player, aspect.getColor(), 2, 100, 0, false, true);
            }
        }
    }
}
