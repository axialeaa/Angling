package com.eightsidedsquare.angling.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

public class ClamBlock extends HorizontalFacingBlock implements Waterloggable, FilterFeeder {

    private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private static final VoxelShape X_SHAPE = Block.createCuboidShape(5.5, 0, 4, 10.5, 2, 12);
    private static final VoxelShape Z_SHAPE = Block.createCuboidShape(4, 0, 5.5, 12, 2, 10.5);

    public ClamBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH).with(WATERLOGGED, true));
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return null;
    }

    @Override
    public float getMaxHorizontalModelOffset() {
        return super.getMaxHorizontalModelOffset();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        boolean bl = fluidState.getFluid() == Fluids.WATER;
        return getDefaultState().with(WATERLOGGED, bl).with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        if (state.get(WATERLOGGED)) {
            tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        if(!canPlaceAt(state, world, pos)) {
            return Blocks.AIR.getDefaultState();
        }

        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos belowPos = pos.down();
        BlockState belowState = world.getBlockState(belowPos);
        return Block.isFaceFullSquare(belowState.getSidesShape(world, belowPos), Direction.UP);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Vec3d offset = state.getModelOffset(pos);
        return (state.get(FACING).getAxis().equals(Direction.Axis.X) ? X_SHAPE : Z_SHAPE).offset(offset.getX(), offset.getY(), offset.getZ());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public void onFeed(BlockPos pos, BlockState state, ServerWorld world) {
        if(state.get(WATERLOGGED) && world.getRandom().nextFloat() < 0.25f) {
            Direction.Type.HORIZONTAL.getShuffled(world.getRandom()).stream()
                    .filter(d -> world.getFluidState(pos.offset(d)).isIn(FluidTags.WATER)
                            && world.getBlockState(pos.offset(d)).isOf(Blocks.WATER)
                            && canPlaceAt(world.getBlockState(pos.offset(d)), world, pos.offset(d)))
                    .findFirst()
                    .ifPresent(d -> {
                        world.setBlockState(pos.offset(d), getDefaultState().with(FACING, Direction.Type.HORIZONTAL.random(world.getRandom())));
                        createFedParticles(5, pos, world);
                    });
        }
    }
}
