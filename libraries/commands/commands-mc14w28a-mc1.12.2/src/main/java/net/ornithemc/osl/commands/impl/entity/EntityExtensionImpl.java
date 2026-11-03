package net.ornithemc.osl.commands.impl.entity;

import net.ornithemc.osl.commands.api.entity.EntityExtension;
import net.ornithemc.osl.commands.impl.server.command.CommandSourceBuilderImpl;

public interface EntityExtensionImpl extends EntityExtension, CommandSourceBuilderImpl {

	@Override
	default boolean hasPermissions(int permissions) {
		throw new AbstractMethodError();
	}
}
