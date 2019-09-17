package de.aelpecyem.elementaristics.particles;

import de.aelpecyem.elementaristics.Elementaristics;
import de.aelpecyem.elementaristics.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

//snatched directly from https://github.com/Ellpeck/NaturesAura/blob/master/src/main/java/de/ellpeck/naturesaura/particles/ParticleHandler.java
@SideOnly(Side.CLIENT)
public final class ParticleHandler {
    public static final ResourceLocation PARTICLE_TEXTURES = new ResourceLocation(Elementaristics.MODID, "textures/misc/particles.png"); //switch to particle_base_1.png if it's considered fancier

    private static final List<Particle> PARTICLES = new CopyOnWriteArrayList<>();
    private static final List<Particle> PARTICLES_NO_DEPTH = new CopyOnWriteArrayList<>(); //use a larger texture with indexes!
    public static boolean depthEnabled = true;
    public static int range = 32;

    public static void spawnParticle(Supplier<Particle> particle) {
        Minecraft mc = Minecraft.getMinecraft();
        Config.EnumParticles particleAmount = Config.client.particleAmount;
        switch (particleAmount) {
            case STANDARD:
                break;
            case REDUCED:
                if (mc.world.rand.nextInt(3) != 0) {
                    return;
                }
                break;
            case MINIMAL:
                if (mc.world.rand.nextInt(10) != 0) {
                    return;
                }
                break;
        }

        if (depthEnabled) {
            PARTICLES.add(particle.get());
        } else {
            PARTICLES_NO_DEPTH.add(particle.get());
        }
    }

    public static void updateParticles() {
        updateList(PARTICLES);
        updateList(PARTICLES_NO_DEPTH);

        depthEnabled = true;
        range = 32;
    }

    private static void updateList(List<Particle> particles) {
        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle particle = particles.get(i);
            particle.onUpdate();
            if (!particle.isAlive())
                particles.remove(i);
        }
    }

    public static void renderParticles(float partialTicks) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;

        if (player != null) {
            float x = ActiveRenderInfo.getRotationX();
            float z = ActiveRenderInfo.getRotationZ();
            float yz = ActiveRenderInfo.getRotationYZ();
            float xy = ActiveRenderInfo.getRotationXY();
            float xz = ActiveRenderInfo.getRotationXZ();

            Particle.interpPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
            Particle.interpPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
            Particle.interpPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
            Particle.cameraViewDir = player.getLook(partialTicks);

            GlStateManager.pushMatrix();

            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.alphaFunc(516, 0.003921569F);
            GlStateManager.disableCull();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

            GlStateManager.depthMask(false);

            mc.getTextureManager().bindTexture(PARTICLE_TEXTURES);
            Tessellator t = Tessellator.getInstance();
            BufferBuilder buffer = t.getBuffer();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
            for (Particle particle : PARTICLES) {
                particle.renderParticle(buffer, player, partialTicks, x, xz, z, yz, xy);
            }
            t.draw();

            GlStateManager.disableDepth();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
            for (Particle particle : PARTICLES_NO_DEPTH)
                particle.renderParticle(buffer, player, partialTicks, x, xz, z, yz, xy);
            t.draw();
            GlStateManager.enableDepth();

            GlStateManager.enableCull();
            GlStateManager.depthMask(true);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.disableBlend();
            GlStateManager.alphaFunc(516, 0.1F);

            GlStateManager.popMatrix();
        }
    }

    public static int getParticleAmount(boolean depth) {
        return depth ? PARTICLES.size() : PARTICLES_NO_DEPTH.size();
    }

    public static void clearParticles() {
        if (!PARTICLES.isEmpty())
            PARTICLES.clear();
        if (!PARTICLES_NO_DEPTH.isEmpty())
            PARTICLES_NO_DEPTH.clear();
    }
}
