package net.ornithemc.osl.commands.impl.mixin.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.server.command.handler.CommandManager;
import net.minecraft.server.command.source.CommandSourceStack;

import net.ornithemc.osl.commands.api.CommandEvents;

@Mixin(CommandManager.class)
public class CommandManagerMixin {

	@Shadow @Final private CommandDispatcher<CommandSourceStack> dispatcher;

	@Inject(
		method="<init>",
		at = @At(
			value = "INVOKE",
			target = "Lcom/mojang/brigadier/CommandDispatcher;findAmbiguities(Lcom/mojang/brigadier/AmbiguityConsumer;)V"
		)
	)
	private void registerCommands(boolean isDedicatedServer, CallbackInfo ci) {
		CommandEvents.REGISTER_SERVER_COMMANDS.invoker().accept(dispatcher);
	}
}
