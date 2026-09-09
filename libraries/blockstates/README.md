# Block States API

The Block States API implements a block state system in Minecraft versions 1.7.10 and below.

## Usage

To make use of block states, extend `StatefulBlock` and implement the three methods to define the block states and metadata conversion. The `StatefulBlock` class deprecates many methods in `Block` that take in `int` parameters for block metadata, block positions, or directions, in favor of equivalent methods that use `BlockState`, `BlockPos`, and `Direction` parameters. Calls to the original methods are forwarded to the new methods so all block logic can be defined in those with no further compatibility code needed.

```java
package com.example;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

import net.ornithemc.osl.blockstates.api.block.StatefulBlock;
import net.ornithemc.osl.blockstates.api.state.BlockState;
import net.ornithemc.osl.blockstates.api.state.StateDefinition;
import net.ornithemc.osl.blockstates.api.state.property.BooleanProperty;
import net.ornithemc.osl.blockstates.api.state.property.IntegerProperty;
import net.ornithemc.osl.core.api.util.math.BlockPos;

public class CookieBlock extends StatefulBlock {

	public static final IntegerProperty BITES_TAKEN = IntegerProperty.of("bites_taken", 0, 7);
	public static final BooleanProperty BAKED = BooleanProperty.of("baked");

	public CookieBlock() {
		super(Material.COOKIE);
	}

	@Override
	public void buildStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BITES_TAKEN, BAKED);
	}

	@Override
	public BlockState getStateFromMetadata(int metadata) {
		int bitesTaken = metadata & 0x7
		boolean baked = (metadata & 0x8) != 0;

		return this.defaultState()
					.set(BITES_TAKEN, bitesTaken)
					.set(BAKED, baked);
	}

	@Override
	public int getMetadataFromState(BlockState state) {
		int bitesTaken = state.get(BITES_TAKEN);
		int baked = state.get(BAKED) ? 0x8 : 0;

		return baked | bitesTaken;
	}

	@Override
	public void onAdded(World world, BlockPos pos, BlockState state) {
		...
	}

	@Override
	public void onRemoved(World world, BlockPos pos, BlockState state) {
		...
	}
}
```
