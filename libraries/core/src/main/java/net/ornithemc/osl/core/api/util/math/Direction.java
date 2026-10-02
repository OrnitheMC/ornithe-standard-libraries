package net.ornithemc.osl.core.api.util.math;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public enum Direction {

	DOWN ( "down", -1, 0, 1, Axis.Y, AxisDirection.NEGATIVE),
	UP   (   "up", -1, 1, 0, Axis.Y, AxisDirection.POSITIVE),
	NORTH("north",  2, 2, 3, Axis.Z, AxisDirection.NEGATIVE),
	SOUTH("south",  0, 3, 2, Axis.Z, AxisDirection.POSITIVE),
	WEST ( "west",  1, 4, 5, Axis.X, AxisDirection.NEGATIVE),
	EAST ( "east",  3, 5, 4, Axis.X, AxisDirection.POSITIVE);

	private final String name;
	private final int data2d;
	private final int data3d;
	private final int opposite;
	private final Axis axis;
	private final AxisDirection axisDirection;

	private Direction(String name, int data2d, int data3d, int opposite, Axis axis, AxisDirection axisDirection) {
		this.name = name;
		this.data2d = data2d;
		this.data3d = data3d;
		this.opposite = opposite;
		this.axis = axis;
		this.axisDirection = axisDirection;

		if (this.axis.plane == Plane.VERTICAL) {
			this.axis.plane.faces[this.data3d] = this;
		}
		if (this.axis.plane == Plane.HORIZONTAL) {
			this.axis.plane.faces[this.data2d] = this;
		}
	}

	public static final Direction[] VALUES = values();

	private static final Map<String, Direction> BY_NAME = Arrays.stream(VALUES)
		.collect(Collectors.toMap(Direction::toString, Function.identity()));
	private static final Direction[] BY_DATA2D = Arrays.stream(VALUES)
		.filter(Plane.HORIZONTAL)
		.sorted(Comparator.comparingInt(Direction::data2d))
		.toArray(Direction[]::new);
	private static final Direction[] BY_DATA3D = Arrays.stream(VALUES)
		.sorted(Comparator.comparingInt(Direction::data3d))
		.toArray(Direction[]::new);

	public static Direction byName(String name) {
		return name == null ? null : BY_NAME.get(name);
	}

	public static Direction byData2d(int data2d) {
		return BY_DATA2D[Math.abs(data2d % BY_DATA2D.length)];
	}

	public static Direction byData3d(int data3d) {
		return BY_DATA3D[Math.abs(data3d % BY_DATA3D.length)];
	}

	@Override
	public String toString() {
		return this.name;
	}

	public int data2d() {
		return this.data2d;
	}

	public int data3d() {
		return this.data3d;
	}

	public Axis axis() {
		return this.axis;
	}

	public AxisDirection axisDirection() {
		return this.axisDirection;
	}

	public int offsetX() {
		return this.axis == Axis.X ? this.axisDirection.offset() : 0;
	}

	public int offsetY() {
		return this.axis == Axis.Y ? this.axisDirection.offset() : 0;
	}

	public int offsetZ() {
		return this.axis == Axis.Z ? this.axisDirection.offset() : 0;
	}

	public Direction opposite() {
		return BY_DATA3D[this.opposite];
	}

	public Direction clockwise() {
		return this.clockwiseY();
	}

	public Direction counterClockwise() {
		return this.counterClockwiseY();
	}

	public Direction clockwiseX() {
		switch (this) {
		case DOWN:
			return SOUTH;
		case UP:
			return NORTH;
		case SOUTH:
			return UP;
		case NORTH:
			return DOWN;
		default:
			throw new IllegalStateException("Unable to get x-clockwise rotation of " + this);
		}
	}

	public Direction clockwiseY() {
		switch (this) {
		case NORTH:
			return EAST;
		case SOUTH:
			return WEST;
		case WEST:
			return NORTH;
		case EAST:
			return SOUTH;
		default:
			throw new IllegalStateException("Unable to get y-clockwise rotation of " + this);
		}
	}

	public Direction clockwiseZ() {
		switch (this) {
		case DOWN:
			return WEST;
		case UP:
			return EAST;
		case WEST:
			return UP;
		case EAST:
			return DOWN;
		default:
			throw new IllegalStateException("Unable to get z-clockwise rotation of " + this);
		}
	}

	public Direction counterClockwiseX() {
		switch (this) {
		case DOWN:
			return NORTH;
		case UP:
			return SOUTH;
		case SOUTH:
			return DOWN;
		case NORTH:
			return UP;
		default:
			throw new IllegalStateException("Unable to get x-counter-clockwise rotation of " + this);
		}
	}

	public Direction counterClockwiseY() {
		switch (this) {
		case NORTH:
			return WEST;
		case SOUTH:
			return EAST;
		case WEST:
			return SOUTH;
		case EAST:
			return NORTH;
		default:
			throw new IllegalStateException("Unable to get y-counter-clockwise rotation of " + this);
		}
	}

	public Direction counterClockwiseZ() {
		switch (this) {
		case DOWN:
			return EAST;
		case UP:
			return WEST;
		case WEST:
			return DOWN;
		case EAST:
			return UP;
		default:
			throw new IllegalStateException("Unable to get z-counter-clockwise rotation of " + this);
		}
	}

	public enum Axis implements Predicate<Direction> {

		X("x", Plane.HORIZONTAL) {

			@Override
			public int choose(int x, int y, int z) {
				return x;
			}

			@Override
			public double choose(double x, double y, double z) {
				return x;
			}
		},
		Y("y", Plane.VERTICAL) {

			@Override
			public int choose(int x, int y, int z) {
				return y;
			}

			@Override
			public double choose(double x, double y, double z) {
				return y;
			}
		},
		Z("z", Plane.HORIZONTAL) {

			@Override
			public int choose(int x, int y, int z) {
				return z;
			}

			@Override
			public double choose(double x, double y, double z) {
				return z;
			}
		};

		private final String name;
		private final Plane plane;

		private Axis(String name, Plane plane) {
			this.name = name;
			this.plane = plane;
		}

		@Override
		public String toString() {
			return this.name;
		}

		@Override
		public boolean test(Direction dir) {
			return dir != null && dir.axis() == this;
		}

		public Plane plane() {
			return this.plane;
		}

		public boolean isVertical() {
			return this.plane == Plane.VERTICAL;
		}

		public boolean isHorizontal() {
			return this.plane == Plane.HORIZONTAL;
		}

		public abstract int choose(int x, int y, int z);

		public abstract double choose(double x, double y, double z);

	}

	public enum AxisDirection {

		POSITIVE("towards positive",  1),
		NEGATIVE("towards negative", -1);

		private final String name;
		private final int offset;

		private AxisDirection(String name, int offset) {
			this.name = name;
			this.offset = offset;
		}

		@Override
		public String toString() {
			return this.name;
		}

		public int offset() {
			return this.offset;
		}
	}

	public enum Plane implements Iterable<Direction>, Predicate<Direction> {

		VERTICAL  (2),
		HORIZONTAL(4);

		private final Direction[] faces;

		private Plane(int faceCount) {
			// filled in Direction::<init>
			this.faces = new Direction[faceCount];
		}

		@Override
		public Iterator<Direction> iterator() {
			return Arrays.stream(this.faces).iterator();
		}

		@Override
		public boolean test(Direction dir) {
			return dir != null && dir.axis().plane() == this;
		}

		public Direction pickDirection(Random random) {
			return this.faces[random.nextInt(this.faces.length)];
		}
	}
}
