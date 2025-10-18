package com.eightsidedsquare.angling.client.particle;

import net.minecraft.block.Block;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.*;
import org.joml.Quaternionf;

public class WormParticle extends SpriteBillboardParticle {

    private final SpriteProvider spriteProvider;

    protected WormParticle(ClientWorld clientWorld, double x, double y, double z, SpriteProvider spriteProvider) {
        super(clientWorld, x, y, z);
        this.spriteProvider = spriteProvider;
        this.scale = 0.3f;
        setMaxAge(200);
    }

    @Override
    protected void render(VertexConsumer vertexConsumer, Camera camera, Quaternionf quaternionf, float tickProgress) {
        Vec3d vec3d = camera.getPos();
        float currentX = (float)(MathHelper.lerp(tickProgress, this.lastX, this.x) - vec3d.getX());
        float currentY = (float)(MathHelper.lerp(tickProgress, this.lastY, this.y) - vec3d.getY());
        float currentZ = (float)(MathHelper.lerp(tickProgress, this.lastZ, this.z) - vec3d.getZ());
        Quaternionf quaternion = RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw());
        Quaternionf flip = RotationAxis.POSITIVE_Y.rotationDegrees(180 - camera.getYaw());

        float size = this.getSize(tickProgress);
        float minU = this.getMinU();
        float maxU = this.getMaxU();
        float minV = this.getMinV();
        float maxV = this.getMaxV();
        int light = this.getBrightness(tickProgress);

        renderFace(vertexConsumer, quaternion, size, currentX, currentY, currentZ, minU, maxU, minV, maxV, light);
        renderFace(vertexConsumer, flip, size, currentX, currentY, currentZ, minU, maxU, minV, maxV, light);
    }

    private void renderFace(VertexConsumer vertexConsumer, Quaternionf quaternion, float size, float x, float y, float z, float minU, float maxU, float minV, float maxV, int light) {
        Vec3d[] vec3ds = new Vec3d[]{new Vec3d(-1.0F, -1.0F, 0.0F), new Vec3d(-1.0F, 1.0F, 0.0F), new Vec3d(1.0F, 1.0F, 0.0F), new Vec3d(1.0F, -1.0F, 0.0F)};

        for(int k = 0; k < 4; ++k) {
            Vec3d vec3d2 = vec3ds[k];
            vec3d2.rotate(quaternion);
            vec3d2.scale(size);
            vec3d2.add(x, y, z);
        }
        vertexConsumer.vertex(vec3ds[0].getX(), vec3ds[0].getY(), vec3ds[0].getZ()).texture(maxU, maxV).color(this.red, this.green, this.blue, this.alpha).light(light).next();
        vertexConsumer.vertex(vec3ds[1].getX(), vec3ds[1].getY(), vec3ds[1].getZ()).texture(maxU, minV).color(this.red, this.green, this.blue, this.alpha).light(light).next();
        vertexConsumer.vertex(vec3ds[2].getX(), vec3ds[2].getY(), vec3ds[2].getZ()).texture(minU, minV).color(this.red, this.green, this.blue, this.alpha).light(light).next();
        vertexConsumer.vertex(vec3ds[3].getX(), vec3ds[3].getY(), vec3ds[3].getZ()).texture(minU, maxV).color(this.red, this.green, this.blue, this.alpha).light(light).next();
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        if(age <= 10) {
            setVelocity(0, 0.023f, 0);
        }else if(age >= maxAge - 10) {
            setVelocity(0, -0.023f, 0);
        }else {
            setVelocity(0, 0, 0);
        }
        super.tick();
        if(!Block.isFaceFullSquare(world.getBlockState(BlockPos.ofFloored(x, y - 0.5d, z)).getSidesShape(world, BlockPos.ofFloored(x, y - 0.5d, z)), Direction.UP)) {
            markDead();
        }
        setSpriteForAge(this.spriteProvider);
    }

    public record Factory(
            SpriteProvider spriteProvider) implements ParticleFactory<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            WormParticle particle = new WormParticle(world, x, y, z, this.spriteProvider);
            particle.setSprite(this.spriteProvider);
            return particle;
        }
    }
}
