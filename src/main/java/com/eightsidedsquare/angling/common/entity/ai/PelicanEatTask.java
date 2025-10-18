package com.eightsidedsquare.angling.common.entity.ai;

import com.eightsidedsquare.angling.common.entity.PelicanEntity;
import com.eightsidedsquare.angling.core.ai.AnglingMemoryModuleTypes;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;

public class PelicanEatTask implements Task<PelicanEntity> {
    public PelicanEatTask() {
        super(ImmutableMap.of(AnglingMemoryModuleTypes.HAS_TRADED, MemoryModuleState.VALUE_PRESENT));
    }

    @Override
    protected boolean shouldRun(ServerWorld world, PelicanEntity entity) {
        return entity.hasEntityInBeak();
    }

    @Override
    public MultiTickTask.Status getStatus() {
        return null;
    }

    @Override
    public boolean tryStarting(ServerWorld world, PelicanEntity entity, long time) {
        return false;
    }

    @Override
    public void tick(ServerWorld world, PelicanEntity entity, long time) {
        entity.setEntityInBeak(new NbtCompound());
        entity.setBeakOpen(false);
        entity.getBrain().forget(AnglingMemoryModuleTypes.HAS_TRADED);
    }

    @Override
    public void stop(ServerWorld world, PelicanEntity entity, long time) {

    }

    @Override
    public String getName() {
        return "";
    }

}
