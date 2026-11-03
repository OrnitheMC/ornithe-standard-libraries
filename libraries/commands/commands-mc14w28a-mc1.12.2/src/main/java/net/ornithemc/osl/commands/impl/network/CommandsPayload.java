package net.ornithemc.osl.commands.impl.network;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import net.ornithemc.osl.commands.api.command.SharedSuggestionProvider;
import net.ornithemc.osl.commands.api.command.SuggestionProviders;
import net.ornithemc.osl.commands.api.command.argument.serdes.ArgumentTypes;
import net.ornithemc.osl.commands.impl.BrigadierCommandsImpl;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.networking.api.ChannelIdentifiers;
import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.networking.api.PacketPayload;

public class CommandsPayload implements PacketPayload {

	public static final NamespacedIdentifier CHANNEL = ChannelIdentifiers.register("osl", "commands");
	public static final int VERSION = 1; // bump when arguments are changed/added/removed

	private static final byte MASK_TYPE               = 0b00011;
	private static final byte TYPE_ROOT               = 0b00000;
	private static final byte TYPE_LITERAL            = 0b00001;
	private static final byte TYPE_ARGUMENT           = 0b00010;
	private static final byte FLAG_EXECUTABLE         = 0b00100;
	private static final byte FLAG_REDIRECT           = 0b01000;
	private static final byte FLAG_CUSTOM_SUGGESTIONS = 0b10000;

	private RootCommandNode<SharedSuggestionProvider> root;

	public CommandsPayload() {
	}

	public CommandsPayload(RootCommandNode<SharedSuggestionProvider> root) {
		this.root = root;
	}

	public RootCommandNode<SharedSuggestionProvider> getRoot() {
		return this.root;
	}

	@Override
	public void read(PacketBuffer buffer) throws IOException {
		int version = buffer.readVarInt();
		List<Entry> entries = buffer.readList((_buffer) -> readEntry(buffer, version));
		int rootIndex = buffer.readVarInt();

		this.root = resolveRoot(entries, rootIndex);
	}

	private static Entry readEntry(PacketBuffer buffer, int version) {
		byte flags = buffer.readByte();
		int[] children = buffer.readIntArray();
		int redirect = (flags & FLAG_REDIRECT) != 0 ? buffer.readVarInt() : 0;

		ArgumentBuilder<SharedSuggestionProvider, ?> argumentBuilder = createBuilder(buffer, version, flags);

		return new Entry(argumentBuilder, flags, redirect, children);
	}

	private static ArgumentBuilder<SharedSuggestionProvider, ?> createBuilder(PacketBuffer buffer, int version, byte flags) {
		int nodeType = flags & MASK_TYPE;

		switch (nodeType) {
		case TYPE_ROOT:
			return null;
		case TYPE_LITERAL:
			return LiteralArgumentBuilder.literal(buffer.readString());
		case TYPE_ARGUMENT:
			String name = buffer.readString();
			ArgumentType<?> type = ArgumentTypes.deserialize(buffer);

			if (type == null) {
				return null;
			}

			RequiredArgumentBuilder<SharedSuggestionProvider, ?> builder = RequiredArgumentBuilder.argument(name, type);

			if ((flags & FLAG_CUSTOM_SUGGESTIONS) != 0) {
				builder.suggests(SuggestionProviders.get(buffer.readNamespacedIdentifier()));
			}

			return builder;
		}

		BrigadierCommandsImpl.LOGGER.error("Received unknown command node type {} in commands payload", nodeType);
		return null;
	}

	private static RootCommandNode<SharedSuggestionProvider> resolveRoot(List<Entry> entries, int rootIndex) {
		List<Entry> unresolved = new ArrayList<>(entries);

		while (!unresolved.isEmpty()) {
			boolean valid = unresolved.removeIf(entry -> entry.resolve(entries));

			if (!valid) {
				throw new IllegalStateException("Server sent an impossible command tree");
			}
		}

		return (RootCommandNode<SharedSuggestionProvider>) entries.get(rootIndex).node;
	}

	@Override
	public void write(PacketBuffer buffer) throws IOException {
		Object2IntMap<CommandNode<SharedSuggestionProvider>> nodeIds = getNodeIdMap(this.root);
		List<CommandNode<SharedSuggestionProvider>> nodesInOrder = listNodesInIdOrder(nodeIds);

		buffer.writeVarInt(VERSION);
		buffer.writeCollection(nodesInOrder, (_buffer, node) -> writeEntry(buffer, node, nodeIds));
		buffer.writeVarInt(nodeIds.getInt(this.root));
	}

	private static Object2IntMap<CommandNode<SharedSuggestionProvider>> getNodeIdMap(RootCommandNode<SharedSuggestionProvider> root) {
		Object2IntMap<CommandNode<SharedSuggestionProvider>> nodeIds = new Object2IntOpenHashMap<>();
		Queue<CommandNode<SharedSuggestionProvider>> queue = new ArrayDeque<>();

		queue.add(root);

		while (!queue.isEmpty()) {
			CommandNode<SharedSuggestionProvider> node = queue.poll();

			if (!nodeIds.containsKey(node)) {
				int id = nodeIds.size();

				nodeIds.put(node, id);
				queue.addAll(node.getChildren());

				if (node.getRedirect() != null) {
					queue.add(node.getRedirect());
				}
			}
		}

		return nodeIds;
	}

	private static List<CommandNode<SharedSuggestionProvider>> listNodesInIdOrder(Object2IntMap<CommandNode<SharedSuggestionProvider>> nodeIds) {
		ObjectArrayList<CommandNode<SharedSuggestionProvider>> sortedNodes = new ObjectArrayList<>(nodeIds.size());
		sortedNodes.size(nodeIds.size());

		for (Object2IntMap.Entry<CommandNode<SharedSuggestionProvider>> entry : Object2IntMaps.fastIterable(nodeIds)) {
			sortedNodes.set(entry.getIntValue(), entry.getKey());
		}

		return sortedNodes;
	}

	private static void writeEntry(PacketBuffer buffer, CommandNode<SharedSuggestionProvider> node, Map<CommandNode<SharedSuggestionProvider>, Integer> nodeIds) {
		byte flags = 0;

		if (node instanceof RootCommandNode) {
			flags |= TYPE_ROOT;
		} else if (node instanceof LiteralCommandNode) {
			flags |= TYPE_LITERAL;
		} else if (node instanceof ArgumentCommandNode) {
			flags |= TYPE_ARGUMENT;

			if (((ArgumentCommandNode<SharedSuggestionProvider, ?>) node).getCustomSuggestions() != null) {
				flags |= FLAG_CUSTOM_SUGGESTIONS;
			}
		} else {
			throw new UnsupportedOperationException("Unknown node type " + node);
		}

		if (node.getRedirect() != null) {
			flags |= FLAG_REDIRECT;
		}

		if (node.getCommand() != null) {
			flags |= FLAG_EXECUTABLE;
		}

		buffer.writeByte(flags);
		buffer.writeVarInt(node.getChildren().size());

		for (CommandNode<SharedSuggestionProvider> commandNode2 : node.getChildren()) {
			buffer.writeVarInt(nodeIds.get(commandNode2));
		}

		if (node.getRedirect() != null) {
			buffer.writeVarInt(nodeIds.get(node.getRedirect()));
		}

		if (node instanceof ArgumentCommandNode) {
			ArgumentCommandNode<SharedSuggestionProvider, ?> argNode = (ArgumentCommandNode<SharedSuggestionProvider, ?>) node;

			buffer.writeString(argNode.getName());
			ArgumentTypes.serialize(buffer, argNode.getType());

			if (argNode.getCustomSuggestions() != null) {
				buffer.writeNamespacedIdentifier(SuggestionProviders.getIdentifier(argNode.getCustomSuggestions()));
			}
		} else if (node instanceof LiteralCommandNode) {
			buffer.writeString(((LiteralCommandNode<?>) node).getLiteral());
		}
	}

	private static class Entry {

		private final ArgumentBuilder<SharedSuggestionProvider, ?> builder;
		private final byte flags;
		private final int redirect;
		private final int[] children;

		private CommandNode<SharedSuggestionProvider> node;

		private Entry(ArgumentBuilder<SharedSuggestionProvider, ?> builder, byte flags, int redirect, int[] children) {
			this.builder = builder;
			this.flags = flags;
			this.redirect = redirect;
			this.children = children;
		}

		public boolean resolve(List<Entry> unresolved) {
			if (this.node == null) {
				if (this.builder == null) {
					this.node = new RootCommandNode<>();
				} else {
					if ((this.flags & FLAG_REDIRECT) != 0) {
						if (unresolved.get(this.redirect).node == null) {
							return false;
						}

						this.builder.redirect(unresolved.get(this.redirect).node);
					}

					if ((this.flags & FLAG_EXECUTABLE) != 0) {
						this.builder.executes(commandContext -> 0);
					}

					this.node = this.builder.build();
				}
			}

			for (int child : this.children) {
				if (unresolved.get(child).node == null) {
					return false;
				}
			}

			for (int child : this.children) {
				CommandNode<SharedSuggestionProvider> node = unresolved.get(child).node;

				if (!(node instanceof RootCommandNode)) {
					this.node.addChild(node);
				}
			}

			return true;
		}
	}
}
