package net.ornithemc.osl.commands.impl.mixin.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.client.Minecraft;
import net.minecraft.client.command.ClientSuggestionProvider;
import net.minecraft.client.network.handler.ClientPlayNetworkHandler;
import net.minecraft.command.SuggestionProvider;

import net.ornithemc.osl.commands.api.client.ClientCommands;
import net.ornithemc.osl.commands.api.client.command.ClientCommandSource;
import net.ornithemc.osl.commands.impl.client.ClientCommandsImpl;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

	@Shadow @Final
	private Minecraft minecraft;
	@Shadow
	private CommandDispatcher<SuggestionProvider> commands;
	@Shadow
	private ClientSuggestionProvider suggestionProvider;

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

	@Inject(
		method = "handleCommands",
		at = @At(
			value = "TAIL"
		)
	)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void osl$(CallbackInfo info) {
		// put client and server commands in one dispatcher for suggestions to work
		ClientCommandsImpl.fillCommands((CommandDispatcher) this.commands, (ClientCommandSource) this.suggestionProvider);
	}
}
