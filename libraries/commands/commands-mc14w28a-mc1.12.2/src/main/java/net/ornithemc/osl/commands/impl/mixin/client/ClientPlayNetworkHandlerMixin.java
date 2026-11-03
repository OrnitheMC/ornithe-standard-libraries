package net.ornithemc.osl.commands.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.handler.ClientPlayNetworkHandler;

import net.ornithemc.osl.commands.api.client.ClientCommands;
import net.ornithemc.osl.commands.api.client.network.handler.ClientPlayNetworkHandlerExtension;
import net.ornithemc.osl.commands.api.command.SharedSuggestionProvider;
import net.ornithemc.osl.commands.impl.client.ClientCommandsImpl;
import net.ornithemc.osl.commands.impl.client.command.ClientSuggestionProvider;
import net.ornithemc.osl.commands.impl.network.CommandSuggestionsPayload;
import net.ornithemc.osl.commands.impl.network.CommandsPayload;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin implements ClientPlayNetworkHandlerExtension {

	@Shadow
	private Minecraft minecraft;

	@Unique
	private CommandDispatcher<SharedSuggestionProvider> commands;
	@Unique
	private ClientSuggestionProvider suggestionProvider;

	@Inject(
		method = "<init>*",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$commands$initSuggestionProvider(CallbackInfo ci) {
		this.commands = new CommandDispatcher<>();
		this.suggestionProvider = new ClientSuggestionProvider(this.minecraft, (ClientPlayNetworkHandler) (Object) this);
	}

	@Inject(
		method = "handleLogin",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$commands$initClientCommands(CallbackInfo ci) {
		ClientCommandsImpl.init(
			this.minecraft.isIntegratedServerRunning() ? ClientCommands.Selection.SINGLEPLAYER : ClientCommands.Selection.MULTIPLAYER
		);
	}

	@Override
	public CommandDispatcher<SharedSuggestionProvider> getCommands() {
		return this.commands;
	}

	@Override
	public ClientSuggestionProvider getSuggestionProvider() {
		return this.suggestionProvider;
	}

	@Override
	public void handleCommands(CommandsPayload payload) {
		this.commands = new CommandDispatcher<>(payload.getRoot());

		// put client and server commands in one dispatcher for suggestions to work
		ClientCommandsImpl.fillCommands(this.commands, this.suggestionProvider);
	}

	@Override
	public void handleCommandSuggestions(CommandSuggestionsPayload payload) {
		this.suggestionProvider.completeSuggestions(payload.getId(), payload.getSuggestions());
	}
}
