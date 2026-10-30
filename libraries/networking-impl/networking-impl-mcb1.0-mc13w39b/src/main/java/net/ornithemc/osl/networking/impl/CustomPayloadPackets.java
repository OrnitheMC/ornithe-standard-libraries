package net.ornithemc.osl.networking.impl;

import net.minecraft.network.packet.Packet;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.networking.api.StringChannelIdentifierParser;

class CustomPayloadPackets {

	static Packet of(NamespacedIdentifier channel, byte[] data) {
		net.minecraft.network.packet.CustomPayloadPacket packet = new net.minecraft.network.packet.CustomPayloadPacket();

		packet.channel = StringChannelIdentifierParser.toString(channel);
		packet.data = data;
		packet.size = data.length;

		return packet;
	}
}
