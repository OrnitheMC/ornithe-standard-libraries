package net.ornithemc.osl.commands.api.entity;

import net.ornithemc.osl.commands.api.server.command.CommandSourceBuilder;

public interface EntityExtension extends CommandSourceBuilder {

	int getPermissions();

	boolean hasPermissions(int permissions);

}
