package de.aelpecyem.elementaristics.entity;

import com.google.common.base.Predicate;
import de.aelpecyem.elementaristics.Elementaristics;
import de.aelpecyem.elementaristics.capability.player.IPlayerCapabilities;
import de.aelpecyem.elementaristics.capability.player.PlayerCapProvider;
import de.aelpecyem.elementaristics.init.SoulInit;
import de.aelpecyem.elementaristics.misc.advancements.CustomAdvancements;
import de.aelpecyem.elementaristics.networking.PacketHandler;
import de.aelpecyem.elementaristics.networking.player.PacketMessage;
import de.aelpecyem.elementaristics.util.CapabilityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class EntitySilverThread extends EntityMob {
    private static final DataParameter<Integer> COOLDOWN = EntityDataManager.createKey(EntityCultist.class, DataSerializers.VARINT); //Let's do the time warp again!
    private final BossInfoServer bossInfo = (BossInfoServer) (new BossInfoServer(this.getDisplayName(), BossInfo.Color.BLUE, BossInfo.Overlay.PROGRESS)).setDarkenSky(true);

    public EntitySilverThread(World worldIn) {
        super(worldIn);
        setSize(0.6F, 1.8F);

    }

    @Override
    protected void entityInit() {
        dataManager.register(COOLDOWN, 0);
        super.entityInit();
    }

    @Override
    public float getBrightness() {
        return 1F;
    }

    @Override
    public boolean isNonBoss() {
        return false;
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        if(damageSrc.getTrueSource() != null  && damageSrc.getTrueSource().hasCapability(PlayerCapProvider.ELEMENTARISTICS_CAP, null)) {
            IPlayerCapabilities cap = damageSrc.getTrueSource().getCapability(PlayerCapProvider.ELEMENTARISTICS_CAP, null);
            if (cap.getSoulId() == SoulInit.soulUnstable.getId()) {
                damageAmount = 10;

            } else {
                damageAmount = 2;
            }
            super.damageEntity(damageSrc, damageAmount); //should only be hurt by players
         }

    }

    @Override
    public void onDeath(DamageSource cause) {
        List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(50), new Predicate<EntityPlayer>() {
            @Override
            public boolean apply(@Nullable EntityPlayer input) {
                return input.hasCapability(PlayerCapProvider.ELEMENTARISTICS_CAP, null) && input.getCapability(PlayerCapProvider.ELEMENTARISTICS_CAP, null).getPlayerAscensionStage() < 1 && input.getCapability(PlayerCapProvider.ELEMENTARISTICS_CAP, null).knowsSoul();
            }
        });
        for (EntityPlayer player : players){
            CapabilityUtil.ascend(1, player);
            PacketHandler.sendTo(player, new PacketMessage("message.ascension_1.standard"));
            if (!player.world.isRemote){
                CustomAdvancements.Advancements.ASCEND.trigger((EntityPlayerMP) player);
            }
        }
        super.onDeath(cause);
    }


    @Override
    protected void applyEntityAttributes() {

        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.37F);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(6.0D);
    }


    protected void updateAITasks() {
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public void addTrackingPlayer(EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }
    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        if (dataManager.get(COOLDOWN) <= 0) {
            dataManager.set(COOLDOWN, 40);
            return super.attackEntityAsMob(entityIn);
        } else {
            return false;
        }
    }

    @Override
    public boolean hitByEntity(Entity entity) {
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase living = (EntityLivingBase) entity;
            switch (world.rand.nextInt(5)) {
                case 1:
                    if (!attemptTeleport(posX + 3 + rand.nextInt(3), entity.posY, posZ + 3 + rand.nextInt(3))) {

                    } else {
                        for (int i = 0; i < world.rand.nextInt(8); i++) {
                            if (attemptTeleport(posX + 3 + rand.nextInt(3), posY + rand.nextInt(3), posX + rand.nextInt(3)))
                                break;
                        }
                    }
                    break;
                case 2:
                    if (!attemptTeleport(posX - rand.nextInt(5), entity.posY, posZ - rand.nextInt(5))) {

                    } else {
                        for (int i = 0; i < world.rand.nextInt(8); i++) {
                            if (attemptTeleport(posX - 3 - rand.nextInt(3), posY + rand.nextInt(5), posZ - 3 - rand.nextInt(3)))
                                break;
                        }
                    }

                default:
                    BlockPos curPos = getPosition();
                    setPosition(living.posX, living.posY, living.posZ);
                    living.setPosition(curPos.getX(), curPos.getY(), curPos.getZ());
                    setRotation(living.getRotationYawHead(), living.rotationPitch);

                    living.addPotionEffect(new PotionEffect(Potion.getPotionById(15), 80, 3, false, false));

            }
           /* if (!attemptTeleport(rand.nextInt(10), 0, rand.nextInt(10))) {
                this.attemptTeleport((Math.abs(this.posX) - Math.abs(living.posX)) * -1 + posX, living.posY, (Math.abs(this.posZ) - Math.abs(living.posZ)) * -1 + posZ);
            } else {
                for (int i = 0; i < 5; i++) {
                    attemptTeleport(rand.nextInt(10), rand.nextInt(5), rand.nextInt(10));
                }
            }*/
        }


        return super.hitByEntity(entity);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 100.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.applyEntityAI();
    }

    protected void applyEntityAI() {
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[]{}));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));

    }

    @Override
    public void onLivingUpdate() {
        if (dataManager.get(COOLDOWN) > 0) {
            dataManager.set(COOLDOWN, dataManager.get(COOLDOWN) - 1);
        }
        for (int i = 0; i < 2; i++) {
            if (world.isRemote)
                Elementaristics.proxy.generateGenericParticles(
                        world,
                        posX + world.rand.nextFloat() * width
                                * 2.0F - width,
                        posY + 0.5D + world.rand.nextFloat()
                                * height,
                        posZ + world.rand.nextFloat() * width
                                * 2.0F - width,
                        0,
                        0,
                        0,
                        12249855, 4, 120, 0, true, true, 0.4F, true);
        }
        super.onLivingUpdate();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return super.getAmbientSound();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return super.getHurtSound(damageSourceIn);
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return super.getDeathSound();
    }
}
