package de.aelpecyem.elementaristics.world.dimensions;

import de.aelpecyem.elementaristics.init.BiomeInit;
import de.aelpecyem.elementaristics.init.ModDimensions;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.gen.ChunkGeneratorFlat;
import net.minecraft.world.gen.ChunkGeneratorHell;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DimensionMind extends WorldProvider{
    public DimensionMind(){
           this.biomeProvider = new BiomeProviderSingle(BiomeInit.MIND);
    }
    @Override
    public void init(){

        this.biomeProvider = new BiomeProviderSingle(BiomeInit.MIND);
    }



    public IChunkGenerator createChunkGenerator()
    {
        return new ChunkGeneratorMind(this.world, this.world.getWorldInfo().isMapFeaturesEnabled(), this.world.getSeed());
    }

    /**
     * Returns 'true' if in the "main surface world", but 'false' if in the Nether or End dimensions.
     */
    public boolean isSurfaceWorld()
    {
        return false;
    }

    /**
     * Will check if the x, z position specified is alright to be set as the map spawn point
     */
    public boolean canCoordinateBeSpawn(int x, int z)
    {
        return false;
    }

    @Override
    public boolean isDaytime() {
        return true;
    }
/**
     * True if the player can respawn in this dimension (true = overworld, false = nether).
     */

    /**
     * Returns true if the given X,Z coordinate should show environmental fog.
     */
    @SideOnly(Side.CLIENT)
    public boolean doesXZShowFog(int x, int z)
    {
        return true;
    }

    public WorldBorder createWorldBorder()
    {
        return new WorldBorder()
        {
            public double getCenterX()
            {
                return super.getCenterX() / 8.0D;
            }
            public double getCenterZ()
            {
                return super.getCenterZ() / 8.0D;
            }
        };
    }

    @Override
    public long getWorldTime() {
        return 1000;
    }

    @Override
    public DimensionType getDimensionType() {
        return ModDimensions.MIND;
    }

    @Override
    public float getStarBrightness(float par1) {
        return 1;
    }

    @Override
    public float getSunBrightness(float par1) {
        return 1;
    }

    @Override
    public float getSunBrightnessFactor(float par1) {
        return 1;
    }

    @Override
    public boolean canRespawnHere() {
        return false;
    }


}
