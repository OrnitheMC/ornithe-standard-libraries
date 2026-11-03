package net.ornithemc.osl.commands.impl.server.command;

import net.ornithemc.osl.commands.api.server.command.CommandSourceBuilder;
import net.ornithemc.osl.commands.api.server.command.CommandSourceStack;

public interface CommandSourceBuilderImpl extends CommandSourceBuilder {

	@Override
	default CommandSourceStack createCommandSourceStack() {
		throw new AbstractMethodError();
	}
}
