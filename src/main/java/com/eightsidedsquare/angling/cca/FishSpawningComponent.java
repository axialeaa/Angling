package com.eightsidedsquare.angling.cca;

import com.eightsidedsquare.angling.core.AnglingBlocks;
import net.minecraft.storage.NbtReadView;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MultifaceGrowthBlock;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class FishSpawningComponent implements AutoSyncedComponent {

    private final FishEntity entity;
    private int loveTicks;
    private int loveCooldown;
    private NbtReadView mateData;
    private boolean carryingRoe;
    private boolean canGrowUp;
    private boolean wasFed;

    public FishSpawningComponent(FishEntity entity) {
        this.entity = entity;
        this.canGrowUp = true;
    }

    @Nullable
    public NbtReadView getMateData() {
        return mateData;
    }

    public void setMateData(@Nullable NbtReadView mateData) {
        this.mateData = mateData;
    }

    public boolean isCarryingRoe() {
        return carryingRoe;
    }

    public boolean isInLove() {
        return loveTicks > 0;
    }

    public void setWasFed(boolean wasFed) {
        this.wasFed = wasFed;
    }

    public boolean wasFed() {
        return wasFed;
    }

    public void tick() {
        if(loveCooldown > 0) {
            loveCooldown--;
        }
        if(loveTicks > 0) {
            loveTicks--;
        }
        BlockState state = entity.getBlockStateAtPos();
        if(wasFed() && !entity.getWorld().isClient && entity.getRandom().nextBetween(0, 400) == 0
                && state.isOf(Blocks.WATER)
                && state.getFluidState().isOf(Fluids.WATER)) {
            Util.copyShuffled(Direction.stream(), entity.getRandom()).stream().filter(this::canPlaceAlgaeAt).findFirst().ifPresent(d -> {
                entity.getWorld().setBlockState(entity.getBlockPos(), AnglingBlocks.ALGAE.getDefaultState().with(MultifaceGrowthBlock.getProperty(d), true), Block.NOTIFY_ALL);
                setWasFed(false);
            });
        }
    }

    private boolean canPlaceAlgaeAt(Direction d) {
        BlockPos pos = entity.getBlockPos().offset(d);
        BlockState state = entity.getWorld().getBlockState(pos);
        return MultifaceGrowthBlock.canGrowOn(entity.getWorld(), d, pos, state);
    }

    public boolean canGrowUp() {
        return canGrowUp;
    }

    public void setCanGrowUp(boolean canGrowUp) {
        this.canGrowUp = canGrowUp;
    }

    public boolean hasCooldown() {
        return loveCooldown > 0;
    }

    public void setCarryingRoe(boolean bl) {
        carryingRoe = bl;
    }

    public void setLoveTicks(int ticks) {
        loveTicks = ticks;
    }

    public void setLoveCooldown(int ticks) {
        loveCooldown = ticks;
    }

    public void createHeartParticles() {
        if(!entity.getWorld().isClient) {
            ((ServerWorld) entity.getWorld()).spawnParticles(ParticleTypes.HEART, entity.getParticleX(1), entity.getRandomBodyY() + 0.5d, entity.getParticleZ(1), 7, 0.25d, 0.25d, 0.25d, 0);
        }
    }

    public void createGrowUpParticles() {
        if(!entity.getWorld().isClient) {
            ((ServerWorld) entity.getWorld()).spawnParticles(ParticleTypes.HAPPY_VILLAGER, entity.getParticleX(1), entity.getRandomBodyY() + 0.25d, entity.getParticleZ(1), 1, 0.1d, 0.1d, 0.1d, 0);
        }
    }

    @Override
    public void readData(ReadView readView) {
        loveTicks = readView.getInt("LoveTicks", 0);
        loveCooldown = readView.getInt("LoveCooldown", 0);
        carryingRoe = readView.getBoolean("CarryingRoe", false);
        canGrowUp = readView.getBoolean("CanGrowUp", true);
        if(readView.contains("MateData")) {
            mateData = readView.getReadView("MateData");
        }
        wasFed = readView.getBoolean("WasFed", false);
    }

    @Override
    public void writeData(WriteView writeView) {
        writeView.putInt("LoveTicks", loveTicks);
        writeView.putInt("LoveCooldown", loveCooldown);
        writeView.putBoolean("CarryingRoe", carryingRoe);
        writeView.putBoolean("CanGrowUp", canGrowUp);
        writeView.put("MateData", mateData);
        writeView.putBoolean("WasFed", wasFed);
    }
}
