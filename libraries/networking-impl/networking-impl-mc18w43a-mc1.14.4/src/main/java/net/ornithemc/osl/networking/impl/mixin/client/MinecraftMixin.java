package net.ornithemc.osl.networking.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.handler.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.networking.api.client.ClientConnectionEvents;
import net.ornithemc.osl.networking.impl.access.ClientNetworkHandlerAccess;
import net.ornithemc.osl.networking.impl.client.ClientConnectionContext;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Shadow
	private ClientWorld world;

	@Shadow
	private ClientPlayNetworkHandler getNetworkHandler() { return null; }

	@Conditional(minecraftVersion = @Version("<19w06a"))
	@Inject(
		method = "m_62116716", // setWorld
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$networking$disconnect(ClientWorld world, Screen screen, CallbackInfo ci) {
		if (this.world != null && world == null) {
			this.triggerDisconnectEvent();
		}
	}

	@Conditional(minecraftVersion = @Version(">=19w06a"))
	@Inject(
		method = "m_62273487", // disconnect
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$networking$disconnect(CallbackInfo ci) {
		if (world != null) {
			this.triggerDisconnectEvent();
		}
	}

	@Unique
	private void triggerDisconnectEvent() {
		ClientNetworkHandlerAccess networkHandler = (ClientNetworkHandlerAccess) this.getNetworkHandler();
		ClientConnectionContext connectionContext = networkHandler.osl$networking$connectionContext();

		ClientConnectionEvents.DISCONNECT.invoker().accept(connectionContext);
	}
}
