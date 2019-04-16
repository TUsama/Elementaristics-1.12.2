package de.aelpecyem.elementaristics.world.structures;

import de.aelpecyem.elementaristics.Elementaristics;
import de.aelpecyem.elementaristics.init.BiomeInit;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGenAnomaly implements IWorldGenerator {
    private static final ResourceLocation ANOMALY = new ResourceLocation(Elementaristics.MODID, "mind_anomally");

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (random.nextInt(1000) == 1) {
            if (world.getBiome(new BlockPos(chunkX * 16, 35, chunkZ * 16)) == BiomeInit.MIND) {
                final BlockPos basePos = new BlockPos(chunkX * 16 + 8, 35, chunkZ * 16 + 8);

                final PlacementSettings settings = new PlacementSettings().setRotation(world.rand.nextBoolean() ? world.rand.nextBoolean() ? Rotation.NONE : Rotation.CLOCKWISE_90 :
                        world.rand.nextBoolean() ? Rotation.COUNTERCLOCKWISE_90 : Rotation.CLOCKWISE_180);
                final Template template = world.getSaveHandler().getStructureTemplateManager().getTemplate(world.getMinecraftServer(), ANOMALY);

                template.addBlocksToWorld(world, basePos, settings);
            }

        }
    }
}