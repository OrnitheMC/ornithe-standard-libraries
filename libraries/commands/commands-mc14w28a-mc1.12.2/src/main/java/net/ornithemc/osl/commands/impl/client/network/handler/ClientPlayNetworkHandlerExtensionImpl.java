package net.ornithemc.osl.commands.impl.client.network.handler;

import com.mojang.brigadier.CommandDispatcher;

import net.ornithemc.osl.commands.api.client.network.handler.ClientPlayNetworkHandlerExtension;
import net.ornithemc.osl.commands.api.command.SharedSuggestionProvider;
import net.ornithemc.osl.commands.impl.client.command.ClientSuggestionProvider;
import net.ornithemc.osl.commands.impl.network.CommandSuggestionsPayload;
import net.ornithemc.osl.commands.impl.network.CommandsPayload;

public interface ClientPlayNetworkHandlerExtensionImpl extends ClientPlayNetworkHandlerExtension {

	@Override
	default CommandDispatcher<SharedSuggestionProvider> getCommands(){
		throw new AbstractMethodError();
	}

	@Override
	default ClientSuggestionProvider getSuggestionProvider() {
		throw new AbstractMethodError();
	}

	@Override
	default void handleCommands(CommandsPayload payload) {
		throw new AbstractMethodError();
	}

	@Override
	default void handleCommandSuggestions(CommandSuggestionsPayload payload) {
		throw new AbstractMethodError();
	}
}
