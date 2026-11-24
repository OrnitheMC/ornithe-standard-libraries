package net.ornithemc.osl.networking.impl;

import net.minecraft.network.packet.Packet;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

@FunctionalInterface
public interface PacketFactory {

	// Packet is not generic prior to 14w31a
	@SuppressWarnings("rawtypes")
	Packet create(NamespacedIdentifier channel, Object data);

}
