package net.ornithemc.osl.core.api.util.math;

import java.util.ArrayList;
import java.util.List;

public class BlockPos implements Comparable<BlockPos> {

	private static final int PACKED_X_LENGTH = 26;
	private static final int PACKED_Z_LENGTH = PACKED_X_LENGTH;
	private static final int PACKED_Y_LENGTH = 64 - PACKED_X_LENGTH - PACKED_Z_LENGTH;
	private static final long PACKED_X_MASK = (1 << PACKED_X_LENGTH) - 1;
	private static final long PACKED_Y_MASK = (1 << PACKED_Y_LENGTH) - 1;
	private static final long PACKED_Z_MASK = (1 << PACKED_Z_LENGTH) - 1;
	private static final int Y_OFFSET = 0;
	private static final int Z_OFFSET = PACKED_Y_LENGTH;
	private static final int X_OFFSET = PACKED_Y_LENGTH + PACKED_Z_LENGTH;

	public static BlockPos of(int x, int y, int z) {
		return new BlockPos(x, y, z);
	}

	public static BlockPos pooled(int x, int y, int z) {
		return PooledMutable.acquire(x, y, z);
	}

	public static long pack(BlockPos pos) {
		return pack(pos.x(), pos.y(), pos.z());
	}

	public static long pack(int x, int y, int z) {
		return packX(x) | packY(y) | packZ(z);
	}

	public static long packX(int x) {
		return (x & PACKED_X_MASK) << X_OFFSET;
	}

	public static long packY(int y) {
		return (y & PACKED_Y_MASK) << Y_OFFSET;
	}

	public static long packZ(int z) {
		return (z & PACKED_Z_MASK) << Z_OFFSET;
	}

	public static BlockPos unpack(long pos) {
		return new BlockPos(unpackX(pos), unpackY(pos), unpackZ(pos));
	}

	public static int unpackX(long pos) {
		return (int) ((pos << (64 - (X_OFFSET + PACKED_X_LENGTH))) >> (64 - PACKED_X_LENGTH));
	}

	public static int unpackY(long pos) {
		return (int) ((pos << (64 - (Y_OFFSET + PACKED_Y_LENGTH))) >> (64 - PACKED_Y_LENGTH));
	}

	public static int unpackZ(long pos) {
		return (int) ((pos << (64 - (Z_OFFSET + PACKED_Z_LENGTH))) >> (64 - PACKED_Z_LENGTH));
	}

	private final int x;
	private final int y;
	private final int z;

	public BlockPos(BlockPos pos) {
		this(pos.x(), pos.y(), pos.z());
	}

	public BlockPos(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public boolean equals(Object that) {
		if (this == that) {
			return true;
		}
		if (!(that instanceof BlockPos)) {
			return false;
		}

		BlockPos pos = (BlockPos) that;
		return this.x() == pos.x() && this.y() == pos.y() && this.z() == pos.z();
	}

	@Override
	public int hashCode() {
		return this.x() + 31 * (this.y() + 31 * this.z());
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "{x=" + this.x() + ",y=" + this.y() + ",z=" + this.z() + "}";
	}

	@Override
	public int compareTo(BlockPos that) {
		if (this.y() == that.y()) {
			if (this.z() == that.z()) {
				return this.x() - that.x();
			}

			return this.z() - that.z();
		}

		return this.y() - that.y();
	}

	public int x() {
		return this.x;
	}

	public int y() {
		return this.y;
	}

	public int z() {
		return this.z;
	}

	public long pack() {
		return pack(this);
	}

	public double distanceSquared(BlockPos pos) {
		return this.distanceSquared(pos, true);
	}

	public double distanceSquared(BlockPos pos, boolean center) {
		return this.distanceSquared(pos.x(), pos.y(), pos.z(), center);
	}

	public double distanceSquared(double x, double y, double z, boolean center) {
		double off = center ? 0.5D : 0.0D;
		double dx = this.x() + off - x;
		double dy = this.y() + off - y;
		double dz = this.z() + off - z;

		return dx * dx + dy * dy + dz * dz;
	}

	public int distanceManhattan(BlockPos pos) {
		int dx = Math.abs(this.x() - pos.x());
		int dy = Math.abs(this.y() - pos.y());
		int dz = Math.abs(this.z() - pos.z());

		return dx + dy + dz;
	}

	public BlockPos add(int dx, int dy, int dz) {
		return dx == 0 && dy == 0 && dz == 0 ? this : new BlockPos(this.x() + dx, this.y() + dy, this.z() + dz);
	}

	public BlockPos add(BlockPos pos) {
		return this.add(pos.x(), pos.y(), pos.z());
	}

	public BlockPos subtract(BlockPos pos) {
		return this.add(-pos.x(), -pos.y(), -pos.z());
	}

	public BlockPos up() {
		return this.offset(Direction.UP);
	}

	public BlockPos up(int amount) {
		return this.offset(Direction.UP, amount);
	}

	public BlockPos down() {
		return this.offset(Direction.DOWN);
	}

	public BlockPos down(int amount) {
		return this.offset(Direction.DOWN, amount);
	}

	public BlockPos north() {
		return this.offset(Direction.NORTH);
	}

	public BlockPos north(int amount) {
		return this.offset(Direction.NORTH, amount);
	}

	public BlockPos south() {
		return this.offset(Direction.SOUTH);
	}

	public BlockPos south(int amount) {
		return this.offset(Direction.SOUTH, amount);
	}

	public BlockPos west() {
		return this.offset(Direction.WEST);
	}

	public BlockPos west(int amount) {
		return this.offset(Direction.WEST, amount);
	}

	public BlockPos east() {
		return this.offset(Direction.EAST);
	}

	public BlockPos east(int amount) {
		return this.offset(Direction.EAST, amount);
	}

	public BlockPos offset(Direction dir) {
		return this.offset(dir, 1);
	}

	public BlockPos offset(Direction dir, int amount) {
		return this.add(dir.offsetX() * amount, dir.offsetY() * amount, dir.offsetZ() * amount);
	}

	public BlockPos immutable() {
		return this;
	}

	public static class Mutable extends BlockPos {

		private int x;
		private int y;
		private int z;

		public Mutable(BlockPos pos) {
			this(pos.x(), pos.y(), pos.z());
		}

		public Mutable(int x, int y, int z) {
			super(x, y, z);
		}

		@Override
		public int x() {
			return this.x;
		}

		@Override
		public int y() {
			return this.y;
		}

		@Override
		public int z() {
			return this.z;
		}

		@Override
		public BlockPos add(int dx, int dy, int dz) {
			return super.add(dx, dy, dz).immutable();
		}

		@Override
		public BlockPos immutable() {
			return new BlockPos(this);
		}

		public void setX(int x) {
			this.x = x;
		}

		public void setY(int y) {
			this.y = y;
		}

		public void setZ(int z) {
			this.z = z;
		}

		public Mutable set(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;

			return this;
		}

		public Mutable set(BlockPos pos) {
			return this.set(pos.x(), pos.y(), pos.z());
		}

		public Mutable move(int dx, int dy, int dz) {
			return this.set(this.x() + dx, this.y() + dy, this.z() + dz);
		}

		public Mutable move(BlockPos dpos) {
			return this.move(dpos.x(), dpos.y(), dpos.z());
		}

		public Mutable move(Direction dir) {
			return this.move(dir, 1);
		}

		public Mutable move(Direction dir, int amount) {
			return this.move(dir.offsetX() * amount, dir.offsetY() * amount, dir.offsetZ() * amount);
		}
	}

	public static class PooledMutable extends Mutable implements AutoCloseable {

		private static final List<PooledMutable> POOL = new ArrayList<>();

		public static PooledMutable acquire() {
			return acquire(0, 0, 0);
		}

		public static PooledMutable acquire(int x, int y, int z) {
			synchronized (POOL) {
				if (!POOL.isEmpty()) {
					PooledMutable pos = POOL.remove(POOL.size() - 1);

					if (pos != null && pos.free) {
						pos.free = false;
						pos.set(x, y, z);

						return pos;
					}
				}
			}

			return new PooledMutable(x, y, z);
		}

		private boolean free;

		private PooledMutable(int x, int y, int z) {
			super(x, y, z);
		}

		@Override
		public PooledMutable set(int x, int y, int z) {
			return (PooledMutable) super.set(x, y, z);
		}

		@Override
		public PooledMutable set(BlockPos pos) {
			return (PooledMutable) super.set(pos);
		}

		@Override
		public PooledMutable move(int dx, int dy, int dz) {
			return (PooledMutable) super.move(dx, dy, dz);
		}

		@Override
		public PooledMutable move(BlockPos dpos) {
			return (PooledMutable) super.move(dpos);
		}

		@Override
		public PooledMutable move(Direction dir) {
			return (PooledMutable) super.move(dir);
		}

		@Override
		public PooledMutable move(Direction dir, int amount) {
			return (PooledMutable) super.move(dir, amount);
		}

		@Override
		public void close() {
			synchronized (POOL) {
				if (POOL.size() < 100) {
					POOL.add(this);
				}

				this.free = true;
			}
		}
	}
}
