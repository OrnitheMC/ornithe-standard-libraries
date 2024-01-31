package net.ornithemc.osl.commands.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.command.handler.CommandRegistry;
import net.minecraft.server.command.source.CommandSource;

import net.ornithemc.osl.commands.api.BrigadierCommandSource;
import net.ornithemc.osl.commands.api.server.CommandSourceStack;
import net.ornithemc.osl.commands.impl.server.ServerCommandManagerImpl;

@Mixin(CommandRegistry.class)
public class CommandRegistryMixin {

	@Inject(
		method = "run(Lnet/minecraft/server/command/source/CommandSource;Ljava/lang/String;)I",
		cancellable = true,
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$commands$runBrigadierCommand(CommandSource source, String command, CallbackInfoReturnable<Integer> cir) {
		if (source instanceof BrigadierCommandSource) {
			CommandSourceStack brigadierSource = (CommandSourceStack)((BrigadierCommandSource)source).createCommandSourceStack();
			Integer result = ServerCommandManagerImpl.run(brigadierSource, command);

			if (result != null) {
				cir.setReturnValue(result);
			}
		}
	}
}
