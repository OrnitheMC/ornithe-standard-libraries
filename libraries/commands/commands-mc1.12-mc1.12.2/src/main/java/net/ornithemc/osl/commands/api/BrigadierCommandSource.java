package net.ornithemc.osl.commands.api;

import net.minecraft.server.command.source.CommandSource;

public interface BrigadierCommandSource extends CommandSource {

	boolean sendCommandSuccess();

	boolean sendCommandFailure();

	boolean sendCommandSuccessToOps();

	AbstractCommandSourceStack<?> createCommandSourceStack();

}
