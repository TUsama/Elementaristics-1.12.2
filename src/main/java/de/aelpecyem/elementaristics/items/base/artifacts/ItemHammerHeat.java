package de.aelpecyem.elementaristics.items.base.artifacts;


import de.aelpecyem.elementaristics.Elementaristics;
import de.aelpecyem.elementaristics.init.ModItems;
import de.aelpecyem.elementaristics.init.ModMaterials;
import de.aelpecyem.elementaristics.items.base.artifacts.rites.IHasRiteUse;
import de.aelpecyem.elementaristics.misc.elements.Aspect;
import de.aelpecyem.elementaristics.misc.elements.Aspects;
import de.aelpecyem.elementaristics.util.IHasModel;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ItemHammerHeat extends ItemPickaxe implements IHasRiteUse, IHasModel {
    protected String name;

    public ItemHammerHeat() {
        super(ModMaterials.MATERIAL_HAMMER);
        attackDamage = ModMaterials.MATERIAL_HAMMER.getAttackDamage();
        maxStackSize = 1;
        name = "hammer_heat";

        setUnlocalizedName(name);
        setRegistryName(name);
        this.setCreativeTab(Elementaristics.tab);

        ModItems.ITEMS.add(this);
    }


    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tooltip.hammer_heat.name"));

        tooltip.add(I18n.format("tooltip.aspect_tool.power") + " " + getPower());

        tooltip.add(I18n.format("tooltip.aspect_tool.aspects"));
        for (Aspect aspect : this.getAspects()) {
            tooltip.add("-" + aspect.getLocalizedName());
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);

    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        List<EntityItem> items = worldIn.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX() + 2, pos.getY() + 2, pos.getZ() + 2, pos.getX() - 2, pos.getY() - 2, pos.getZ() - 2));
        if (!items.isEmpty()) {
            EntityItem shard = null;
            EntityItem gem = null;
            EntityItem essence = null;
            for (EntityItem item : items) {
                if (item.getItem().getItem() == Items.PRISMARINE_SHARD) {
                    shard = item;
                }
                if (item.getItem().getItem() == ModItems.gem_arcane) {
                    gem = item;
                }
                if (item.getItem().getItem() == ModItems.essence && item.getItem().getMetadata() < 5) {
                    essence = item;
                }
            }
            if (shard != null && gem != null && essence != null) {
                if (!worldIn.isRemote)
                    worldIn.spawnEntity(new EntityItem(worldIn, shard.posX, shard.posY, shard.posZ, new ItemStack(ModItems.scale, 1, essence.getItem().getMetadata())));
                shard.getItem().shrink(1);
                gem.getItem().shrink(1);
                essence.getItem().shrink(1);
                worldIn.playSound(null, pos.add(0.5, 1, 0.5), SoundEvents.BLOCK_ANVIL_USE, SoundCategory.AMBIENT, 1, 1);
                Elementaristics.proxy.generateGenericParticles(worldIn, pos.add(hitX, hitY, hitZ), 15159040, 2, 160, 0, true, true);
                player.getHeldItem(hand).damageItem(1, player);
                return EnumActionResult.SUCCESS;
            }
        }
        if (worldIn.getBlockState(pos.up()).getBlock() == Blocks.AIR) {
            worldIn.setBlockState(pos.up(), Blocks.FIRE.getDefaultState(), 1);
            worldIn.playSound(null, pos.add(0.5, 1, 0.5), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.AMBIENT, 1, 1);
            Elementaristics.proxy.generateGenericParticles(worldIn,   pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ, 15159040, 2, 160, 0, true, true);
            player.getHeldItem(hand).damageItem(1, player);
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        target.setFire(5);
        return super.hitEntity(stack, target, attacker);
    }

    public void registerItemModel() {
        Elementaristics.proxy.registerItemRenderer(this, 0, name);
    }


    @Override
    public List<Aspect> getAspects() {
        List<Aspect> aspects = new ArrayList<>();
        aspects.add(Aspects.fire);
        return aspects;
    }

    @Override
    public int getPower() {
        return 6;
    }

    @Override
    public boolean isConsumed() {
        return false;
    }
}
