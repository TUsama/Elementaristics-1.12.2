package de.aelpecyem.elementaristics.blocks.base;

import de.aelpecyem.elementaristics.Elementaristics;
import de.aelpecyem.elementaristics.init.ModBlocks;
import de.aelpecyem.elementaristics.util.IBlockHasModel;
import de.aelpecyem.elementaristics.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockBase extends Block implements IBlockHasModel {
    protected String name;

    public BlockBase(Material material, String name) {
        super(material);
        setHardness(4);

        setResistance(5);
        this.name = name;

        setUnlocalizedName(name);
        setRegistryName(name);
        this.setCreativeTab(Elementaristics.tab);
        ModBlocks.BLOCKS.add(this);
    }

    public BlockBase(Material material, String name, float lightLevel) {
        super(material);
        setHardness(6);
        setResistance(6);
        setLightLevel(lightLevel);
        this.name = name;

        setUnlocalizedName(name);
        setRegistryName(name);
        this.setCreativeTab(Elementaristics.tab);
        ModBlocks.BLOCKS.add(this);
    }


    public void registerItemModel(Item itemBlock) {
        Elementaristics.proxy.registerItemRenderer(itemBlock, 0, name);
    }

    public Item createItemBlock() {
        return new ItemBlock(this).setRegistryName(getRegistryName());
    }


    @Override
    public BlockBase setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }

}
