package net.ornithemc.osl.commands.api.argument;

import java.util.Set;
import java.util.function.Predicate;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.pattern.BlockPointer;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.property.Property;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockInput implements Predicate<BlockPointer> {

	private final BlockState state;
	private final Set<Property<?>> properties;
	private final NbtCompound nbt;

	public BlockInput(BlockState state, Set<Property<?>> properties, NbtCompound nbt) {
		this.state = state;
		this.properties = properties;
		this.nbt = nbt;
	}

	public BlockState getState() {
		return this.state;
	}

	@Override
	public boolean test(BlockPointer ptr) {
		BlockState state = ptr.getState();

		if (state.getBlock() != this.state.getBlock()) {
			return false;
		}
		for (Property<?> property : this.properties) {
			if (state.get(property) != this.state.get(property)) {
				return false;
			}
		}
		if (this.nbt != null) {
			BlockEntity blockEntity = ptr.getBlockEntity();

			if (blockEntity != null) {
				return NbtUtils.matches(this.nbt, blockEntity.writeNbt(new NbtCompound()), true);
			}
		}

		return true;
	}

	public boolean set(World world, BlockPos pos, int flags) {
		if (!world.setBlockState(pos, this.state, flags)) {
			return false;
		}
		if (this.nbt != null) {
			BlockEntity blockEntity = world.getBlockEntity(pos);

			if (blockEntity != null) {
				NbtCompound nbt = this.nbt.copy();
				nbt.putInt("x", pos.getX());
				nbt.putInt("y", pos.getY());
				nbt.putInt("z", pos.getZ());
				blockEntity.readNbt(nbt);
			}
		}

		return true;
	}
}
