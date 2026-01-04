package net.ornithemc.osl.networking.impl;

import net.minecraft.network.packet.Packet;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

public interface PacketFactory {

	Packet create(NamespacedIdentifier channel, byte[] data);

}
