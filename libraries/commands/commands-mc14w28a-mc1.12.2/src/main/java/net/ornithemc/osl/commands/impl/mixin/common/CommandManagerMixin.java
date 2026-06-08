package net.ornithemc.osl.commands.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.handler.CommandManager;
import net.minecraft.server.command.handler.CommandRegistry;

import net.ornithemc.osl.commands.api.server.ServerCommands;
import net.ornithemc.osl.commands.impl.server.ServerCommandsImpl;

@Mixin(CommandManager.class)
public abstract class CommandManagerMixin extends CommandRegistry {

	@WrapOperation(
		method = "<init>",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/MinecraftServer;isDedicated()Z"
		)
	)
	private boolean osl$commands$captureServerInstance(MinecraftServer server, Operation<Boolean> op, @Share("osl$commands$server") LocalRef<MinecraftServer> serverRef) {
		serverRef.set(server);
		return op.call(server);
	}

	@Inject(
		method="<init>",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/command/AbstractCommand;setListener(Lnet/minecraft/server/command/handler/CommandListener;)V"
		)
	)
	private void osl$commands$initServerCommands(CallbackInfo ci, @Share("osl$commands$server") LocalRef<MinecraftServer> serverRef) {
		ServerCommandsImpl.start(
			this::register,
			serverRef.get().isDedicated() ? ServerCommands.Selection.DEDICATED : ServerCommands.Selection.INTEGRATED
		);
	}
}
