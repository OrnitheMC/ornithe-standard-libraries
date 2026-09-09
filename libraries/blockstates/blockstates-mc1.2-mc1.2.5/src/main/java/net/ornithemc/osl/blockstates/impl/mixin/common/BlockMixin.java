package net.ornithemc.osl.blockstates.impl.mixin.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import net.ornithemc.osl.blockstates.api.block.BlockExtension;
import net.ornithemc.osl.blockstates.api.block.state.BlockState;
import net.ornithemc.osl.blockstates.api.state.StateDefinition;
import net.ornithemc.osl.blockstates.impl.block.state.BlockStateImpl;
import net.ornithemc.osl.blockstates.impl.state.StateDefinitionImpl;
import net.ornithemc.osl.core.api.util.math.BlockPos;
import net.ornithemc.osl.core.api.util.math.Direction;

@Mixin(Block.class)
public class BlockMixin implements BlockExtension {

	@Unique
	private StateDefinition<Block, BlockState> stateDefinition;
	@Unique
	private BlockState defaultState;

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "TAIL"
		)
	)
	private static void osl$blockstates$registerBlockStates(CallbackInfo ci) {
		for (int blockId = 0; blockId < Block.BY_ID.length; blockId++) {
			Block block = Block.BY_ID[blockId];

			if (block != null) {
				for (BlockState state : block.stateDefinition().all()) {
					int metadata = block.getMetadataFromState(state);
					int stateId = blockId << 4 | metadata;

					Block.STATE_REGISTRY.put(state, stateId);
				}
			}
		}
	}

	@Inject(
		method = "<init>",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$blockstates$createStateDefinition(CallbackInfo ci) {
		StateDefinition.Builder<Block, BlockState> stateDefinition = new StateDefinitionImpl.Builder<>((Block) (Object) this);
		this.buildStateDefinition(stateDefinition);

		this.stateDefinition = stateDefinition.build(BlockStateImpl::new);
		this.defaultState = this.stateDefinition.any();
	}

	@Override
	public void buildStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		// add data_value property by default, this ensures block/metadata
		// can always be converted to block states even for blocks that do
		// not define their properties properly
		if (!this.isAir()) {
			builder.add(DATA_VALUE);
		}
	}

	@Override
	public StateDefinition<Block, BlockState> stateDefinition() {
		return this.stateDefinition;
	}

	@Override
	public void setDefaultState(BlockState state) {
		this.defaultState = state;
	}

	@Override
	public BlockState defaultState() {
		return this.defaultState;
	}

	@Override
	public BlockState getStateFromMetadata(int metadata) {
		return this.isAir() ? this.defaultState : this.defaultState.set(DATA_VALUE, metadata);
	}

	@Override
	public int getMetadataFromState(BlockState state) {
		return this.isAir() ? 0 : state.get(DATA_VALUE);
	}

	@Override
	public boolean is(Block block) {
		return this == (Object) block;
	}

	@Override
	public boolean isAir() {
		return false;
	}

	@Shadow
	public int getColor(int metadata) { return 0; }

	@Override
	public int getColor(BlockState state) {
		return this.getColor(this.getMetadataFromState(state));
	}

	@Shadow
	public int getColor(WorldView world, int x, int y, int z) { return 0; }

	@Override
	public int getColorTint(WorldView world, BlockPos pos) {
		return this.getColor(world, pos.x(), pos.y(), pos.z());
	}

	@Shadow
	public int getSprite(int face, int metadata) { return 0; }

	@Override
	public int getSprite(BlockState state, Direction face) {
		return this.getSprite(face.data3d(), this.getMetadataFromState(state));
	}

	@Shadow
	public int getLightColor(WorldView world, int x, int y, int z) { return 0; }

	@Override
	public int getLightColor(WorldView world, BlockPos pos) {
		return this.getLightColor(world, pos.x(), pos.y(), pos.z());
	}

	@Shadow
	public float getAmbientOcclusionLight(WorldView world, int x, int y, int z) { return 0.0F; }

	@Override
	public float getAmbientOcclusionLight(WorldView world, BlockPos pos) {
		return this.getAmbientOcclusionLight(world, pos.x(), pos.y(), pos.z());
	}

	@Shadow
	public boolean hasSignal(WorldView world, int x, int y, int z, int dir) { return false; }

	@Override
	public boolean hasSignal(WorldView world, BlockPos pos, BlockState state, Direction dir) {
		return this.hasSignal(world, pos.x(), pos.y(), pos.z(), dir.data3d());
	}

	@Shadow
	public boolean hasDirectSignal(World world, int x, int y, int z, int dir) { return false; }

	@Override
	public boolean hasDirectSignal(World world, BlockPos pos, BlockState state, Direction dir) {
		return this.hasDirectSignal(world, pos.x(), pos.y(), pos.z(), dir.data3d());
	}

	@Override
	public BlockState resolveVirtualProperties(BlockState state, WorldView world, BlockPos pos) {
		return state;
	}

	@Shadow
	public Box getOutlineShape(World world, int x, int y, int z) { return null; }

	@Override
	public Box getOutlineShape(World world, BlockPos pos) {
		return this.getOutlineShape(world, pos.x(), pos.y(), pos.z());
	}

	@Shadow
	public boolean shouldRenderFace(WorldView world, int x, int y, int z, int face) { return false; }

	@Override
	public boolean shouldRenderFace(WorldView world, BlockPos pos, Direction face) {
		return this.shouldRenderFace(world, pos.x(), pos.y(), pos.z(), face.data3d());
	}

	@Shadow
	public Box getCollisionShape(World world, int x, int y, int z) { return null; }

	@Override
	public Box getCollisionShape(World world, BlockPos pos, BlockState state) {
		return this.getCollisionShape(world, pos.x(), pos.y(), pos.z());
	}

	@Shadow
	public void addCollisions(World world, int x, int y, int z, Box shape, ArrayList<Box> collisions) { }

	@Override
	public void addCollisions(World world, BlockPos pos, BlockState state, Box shape, List<Box> collisions) {
		this.addCollisions(world, pos.x(), pos.y(), pos.z(), shape, collisions instanceof ArrayList ? (ArrayList<Box>) collisions : new ArrayList<>(collisions));
	}

	@Shadow
	public HitResult rayTrace(World world, int x, int y, int z, Vec3d from, Vec3d to) { return null; }

	@Override
	public HitResult rayTrace(World world, BlockPos pos, Vec3d from, Vec3d to) {
		return this.rayTrace(world, pos.x(), pos.y(), pos.z(), from, to);
	}

	@Shadow
	public boolean canWalkThrough(WorldView world, int x, int y, int z) { return false; }

	@Override
	public boolean canWalkThrough(WorldView world, BlockPos pos) {
		return this.canWalkThrough(world, pos.x(), pos.y(), pos.z());
	}

	@Shadow
	public boolean canBePlaced(World world, int x, int y, int z) { return false; }

	@Override
	public boolean canBePlaced(World world, BlockPos pos) {
		return this.canBePlaced(world, pos.x(), pos.y(), pos.z());
	}

	@Shadow
	public void doEvent(World world, int x, int y, int z, int type, int data) { }

	@Override
	public void doEvent(World world, BlockPos pos, BlockState state, int type, int data) {
		this.doEvent(world, pos.x(), pos.y(), pos.z(), type, data);
	}

	@Shadow
	public void neighborChanged(World world, int x, int y, int z, int neighborBlock) { }

	@Override
	public void neighborChanged(World world, BlockPos pos, BlockState state, Block neighborBlock) {
		this.neighborChanged(world, pos.x(), pos.y(), pos.z(), neighborBlock.id);
	}

	@Shadow
	public void onAdded(World world, int x, int y, int z) { }

	@Override
	public void onAdded(World world, BlockPos pos, BlockState state) {
		this.onAdded(world, pos.x(), pos.y(), pos.z());
	}

	@Shadow
	public void onRemoved(World world, int x, int y, int z) { }

	@Override
	public void onRemoved(World world, BlockPos pos, BlockState state) {
		this.onRemoved(world, pos.x(), pos.y(), pos.z());
	}

	@Shadow
	public void tick(World world, int x, int y, int z, Random random) { }

	@Override
	public void tick(World world, BlockPos pos, BlockState state, Random random) {
		this.tick(world, pos.x(), pos.y(), pos.z(), random);
	}

	@Shadow
	public void onEntityCollision(World world, int x, int y, int z, Entity entity) { }

	@Override
	public void onEntityCollision(World world, BlockPos pos, BlockState state, Entity entity) {
		this.onEntityCollision(world, pos.x(), pos.y(), pos.z(), entity);
	}

	@Shadow
	public final void dropItems(World world, int x, int y, int z, int metadata, int fortuneLevel) { }

	@Override
	public final void dropItems(World world, BlockPos pos, BlockState state, int fortuneLevel) {
		this.dropItems(world, pos.x(), pos.y(), pos.z(), this.getMetadataFromState(state), fortuneLevel);
	}

	@Shadow
	public void dropItems(World world, int x, int y, int z, int metadata, float luck, int fortuneLevel) { }

	@Override
	public void dropItems(World world, BlockPos pos, BlockState state, float luck, int fortuneLevel) {
		this.dropItems(world, pos.x(), pos.y(), pos.z(), this.getMetadataFromState(state), luck, fortuneLevel);
	}

	@Shadow
	public boolean use(World world, int x, int y, int z, PlayerEntity player) { return false; }

	@Override
	public boolean use(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		return this.use(world, pos.x(), pos.y(), pos.z(), player);
	}

	@Shadow
	public void startMining(World world, int x, int y, int z, PlayerEntity player) { }

	@Override
	public void startMining(World world, BlockPos pos, PlayerEntity player) {
		this.startMining(world, pos.x(), pos.y(), pos.z(), player);
	}
}
