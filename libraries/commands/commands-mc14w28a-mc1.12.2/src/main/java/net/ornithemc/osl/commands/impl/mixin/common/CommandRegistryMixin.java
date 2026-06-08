package net.ornithemc.osl.commands.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.command.handler.CommandRegistry;
import net.minecraft.server.command.source.CommandSource;

import net.ornithemc.osl.commands.api.server.command.CommandSourceBuilder;
import net.ornithemc.osl.commands.api.server.command.CommandSourceStack;
import net.ornithemc.osl.commands.impl.server.ServerCommandsImpl;

@Mixin(CommandRegistry.class)
public class CommandRegistryMixin {

	@Inject(
		method = "run(Lnet/minecraft/server/command/source/CommandSource;Ljava/lang/String;)I",
		cancellable = true,
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$commands$runBrigadierCommand(CommandSource commandSource, String command, CallbackInfoReturnable<Integer> cir) {
		if (commandSource instanceof CommandSourceBuilder) {
			CommandSourceBuilder builder = (CommandSourceBuilder) commandSource;
			CommandSourceStack source = builder.createCommandSourceStack();
			Integer result = ServerCommandsImpl.run(source, command, false);

			if (result != null) {
				cir.setReturnValue(result);
			}
		}
	}
}
