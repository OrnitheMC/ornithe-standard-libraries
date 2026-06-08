package net.ornithemc.osl.commands.impl.mixin.client;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.handler.ClientPlayNetworkHandler;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.commands.api.client.command.ClientCommandSource;
import net.ornithemc.osl.commands.impl.client.ClientCommandsImpl;

@Conditional(minecraftVersion = @Version("<15w34b"))
@Mixin(ChatScreen.class)
public class ChatScreenMixin extends Screen {

	@Unique
	private CompletableFuture<List<String>> clientCommandSuggestions;

	@Inject(
		method = "m_63691428",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$commands$requestClientCommandSuggestions(String command, String remainder, CallbackInfo ci) {
		if (this.clientCommandSuggestions != null) {
			this.clientCommandSuggestions.cancel(false);
		}

		ClientPlayNetworkHandler networkHandler = this.minecraft.getNetworkHandler();
		ClientCommandSource source = networkHandler.getSuggestionProvider();

		this.clientCommandSuggestions = ClientCommandsImpl.getLegacySuggestions(source, command);
	}

	@WrapMethod(
		method = "m_61836526"
	)
	private void osl$commands$insertClientCommandSuggestions(String[] suggestions, Operation<Void> op) {
		if (this.clientCommandSuggestions == null) {
			op.call((Object) suggestions);
		} else {
			this.clientCommandSuggestions.thenAccept(brigadierSuggestions -> {
				int suggestionsTotal = brigadierSuggestions.size() + suggestions.length;
				String[] legacySuggestions = brigadierSuggestions.toArray(new String[suggestionsTotal]);
				System.arraycopy(suggestions, 0, legacySuggestions, brigadierSuggestions.size(), suggestions.length);

				op.call((Object) legacySuggestions);

				this.clientCommandSuggestions = null;
			});
		}
	}
}
