package net.ornithemc.osl.text.api;

import java.util.Optional;

import net.ornithemc.osl.core.api.util.Unit;

public final class TextVisitResults {

	public static final Optional<Unit> CONTINUE  = Optional.empty();
	public static final Optional<Unit> TERMINATE = Optional.of(Unit.INSTANCE);

}
