package net.ornithemc.osl.commands.impl.network;

import java.io.IOException;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.networking.api.ChannelIdentifiers;
import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.networking.api.PacketPayload;

public class RequestCommandSuggestionsPayload implements PacketPayload {

	public static final NamespacedIdentifier CHANNEL = ChannelIdentifiers.register("osl", "request_command_suggestions");

	private int id;
	private String command;

	public RequestCommandSuggestionsPayload() {
	}

	public RequestCommandSuggestionsPayload(int id, String command) {
		this.id = id;
		this.command = command;
	}

	public int getId() {
		return this.id;
	}

	public String getCommand() {
		return this.command;
	}

	@Override
	public void read(PacketBuffer buffer) throws IOException {
		this.id = buffer.readVarInt();
		this.command = buffer.readString();
	}

	@Override
	public void write(PacketBuffer buffer) throws IOException {
		buffer.writeVarInt(this.id);
		buffer.writeString(this.command);
	}
}
