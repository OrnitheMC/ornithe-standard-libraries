package net.ornithemc.osl.blockstates.api.state.property;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.ornithemc.osl.core.api.util.math.Direction;

public class DirectionProperty extends EnumProperty<Direction> {

	public static DirectionProperty of(String name) {
		return of(name, dir -> true);
	}

	public static DirectionProperty of(String name, Predicate<Direction> filter) {
		return of(name, Arrays.stream(Direction.VALUES).filter(filter).collect(Collectors.toList()));
	}

	public static DirectionProperty of(String name, Direction... directions) {
		return of(name, Arrays.asList(directions));
	}

	public static DirectionProperty of(String name, Collection<Direction> directions) {
		return new DirectionProperty(name, directions);
	}

	private DirectionProperty(String name, Collection<Direction> directions) {
		super(name, Direction.class, directions);
	}
}
