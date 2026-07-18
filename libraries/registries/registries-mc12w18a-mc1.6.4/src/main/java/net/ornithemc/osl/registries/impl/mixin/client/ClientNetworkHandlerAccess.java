package net.ornithemc.osl.registries.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.network.handler.ClientNetworkHandler;

@Mixin(ClientNetworkHandler.class)
public interface ClientNetworkHandlerAccess {

	@Accessor("disconnected")
	boolean accessDisconnected();

}
