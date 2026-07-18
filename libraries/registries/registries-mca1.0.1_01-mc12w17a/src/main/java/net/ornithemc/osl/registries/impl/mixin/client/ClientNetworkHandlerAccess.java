package net.ornithemc.osl.registries.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.network.handler.ClientNetworkHandler;
import net.minecraft.network.Connection;

@Mixin(ClientNetworkHandler.class)
public interface ClientNetworkHandlerAccess {

	@Accessor("connection")
	Connection accessConnection();

	@Accessor("disconnected")
	boolean accessDisconnected();

}
