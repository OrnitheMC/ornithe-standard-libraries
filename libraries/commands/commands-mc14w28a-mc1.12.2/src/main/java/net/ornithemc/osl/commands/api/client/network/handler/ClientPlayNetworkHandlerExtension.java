package net.ornithemc.osl.commands.api.client.network.handler;

import com.mojang.brigadier.CommandDispatcher;

import net.ornithemc.osl.commands.api.command.SharedSuggestionProvider;
import net.ornithemc.osl.commands.impl.client.command.ClientSuggestionProvider;
import net.ornithemc.osl.commands.impl.network.CommandSuggestionsPayload;
import net.ornithemc.osl.commands.impl.network.CommandsPayload;

public interface ClientPlayNetworkHandlerExtension {

	CommandDispatcher<SharedSuggestionProvider> getCommands();

	ClientSuggestionProvider getSuggestionProvider();

	void handleCommands(CommandsPayload payload);

	void handleCommandSuggestions(CommandSuggestionsPayload payload);

}
