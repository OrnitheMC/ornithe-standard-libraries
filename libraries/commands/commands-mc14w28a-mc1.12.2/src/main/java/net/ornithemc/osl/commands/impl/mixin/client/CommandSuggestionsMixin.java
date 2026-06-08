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

import net.minecraft.client.Minecraft;
import net.minecraft.client.command.CommandSuggestions;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.commands.api.client.command.ClientCommandSource;
import net.ornithemc.osl.commands.impl.client.ClientCommandsImpl;
import net.ornithemc.osl.lifecycle.api.client.MinecraftInstance;

@Conditional(minecraftVersion = @Version(">=15w34b"))
@Mixin(CommandSuggestions.class)
public class CommandSuggestionsMixin {

	@Unique
	private CompletableFuture<List<String>> clientCommandSuggestions;

	@Inject(
		method = "requestSuggestions",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$commands$requestClientCommandSuggestions(String command, CallbackInfo ci) {
		if (this.clientCommandSuggestions != null) {
			this.clientCommandSuggestions.cancel(false);
		}

		Minecraft minecraft = MinecraftInstance.get();
		ClientCommandSource source = minecraft.getNetworkHandler().getSuggestionProvider();

		this.clientCommandSuggestions = ClientCommandsImpl.getLegacySuggestions(source, command);
	}

	@WrapMethod(
		method = "acceptSuggestions"
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
