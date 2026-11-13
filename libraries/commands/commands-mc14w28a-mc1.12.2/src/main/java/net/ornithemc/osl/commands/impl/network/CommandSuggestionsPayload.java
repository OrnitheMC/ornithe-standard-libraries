package net.ornithemc.osl.commands.impl.network;

import java.io.IOException;
import java.util.List;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.networking.api.ChannelIdentifiers;
import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.networking.api.PacketPayload;
import net.ornithemc.osl.text.api.TextComponents;

public class CommandSuggestionsPayload implements PacketPayload {

	public static final NamespacedIdentifier CHANNEL = ChannelIdentifiers.register("osl", "command_suggestions");

	private int id;
	private Suggestions suggestions;

	public CommandSuggestionsPayload() {
	}

	public CommandSuggestionsPayload(int id, Suggestions suggestions) {
		this.id = id;
		this.suggestions = suggestions;
	}

	public int getId() {
		return this.id;
	}

	public Suggestions getSuggestions() {
		return this.suggestions;
	}

	@Override
	public void read(PacketBuffer buffer) throws IOException {
		int id = buffer.readVarInt();
		int rangeStart = buffer.readVarInt();
		int rangeEnd = buffer.readVarInt();

		StringRange range = StringRange.between(rangeStart, rangeStart + rangeEnd);

		List<Suggestion> list = buffer.readList(_buffer -> {
			String text = buffer.readString();
			Message tooltip = buffer.readBoolean() ? buffer.readTextComponent() : null;

			return new Suggestion(range, text, tooltip::getString);
		});

		this.id = id;
		this.suggestions = new Suggestions(range, list);
	}

	@Override
	public void write(PacketBuffer buffer) throws IOException {
		buffer.writeVarInt(this.id);
		buffer.writeVarInt(this.suggestions.getRange().getStart());
		buffer.writeVarInt(this.suggestions.getRange().getEnd());
		buffer.writeCollection(this.suggestions.getList(), (_buffer, suggestion) -> {
			buffer.writeString(suggestion.getText());
			buffer.writeBoolean(suggestion.getTooltip() != null);
			if (suggestion.getTooltip() != null) {
				buffer.writeTextComponent(TextComponents.resolve(suggestion.getTooltip()));
			}
		});
	}
}
