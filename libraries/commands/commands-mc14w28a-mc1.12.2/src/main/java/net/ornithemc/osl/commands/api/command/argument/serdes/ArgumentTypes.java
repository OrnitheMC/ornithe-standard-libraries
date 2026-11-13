package net.ornithemc.osl.commands.api.command.argument.serdes;

import java.util.Map;

import com.google.common.collect.Maps;

import com.mojang.brigadier.arguments.ArgumentType;

import net.ornithemc.osl.commands.api.command.argument.AnchorArgument;
import net.ornithemc.osl.commands.api.command.argument.AxesArgument;
import net.ornithemc.osl.commands.api.command.argument.BlockStateArgument;
import net.ornithemc.osl.commands.api.command.argument.BlockPosArgument;
import net.ornithemc.osl.commands.api.command.argument.BlockPredicateArgument;
import net.ornithemc.osl.commands.api.command.argument.ColorArgument;
import net.ornithemc.osl.commands.api.command.argument.ColumnPosArgument;
import net.ornithemc.osl.commands.api.command.argument.EnchantmentArgument;
import net.ornithemc.osl.commands.api.command.argument.EntityArgument;
import net.ornithemc.osl.commands.api.command.argument.GameProfileArgument;
import net.ornithemc.osl.commands.api.command.argument.IdentifierArgument;
import net.ornithemc.osl.commands.api.command.argument.ItemArgument;
import net.ornithemc.osl.commands.api.command.argument.ItemPredicateArgument;
import net.ornithemc.osl.commands.api.command.argument.LegacyBlockStateArgument;
import net.ornithemc.osl.commands.api.command.argument.LegacyBlockPredicateArgument;
import net.ornithemc.osl.commands.api.command.argument.MessageArgument;
import net.ornithemc.osl.commands.api.command.argument.NbtCompoundArgument;
import net.ornithemc.osl.commands.api.command.argument.ObjectiveArgument;
import net.ornithemc.osl.commands.api.command.argument.OperationArgument;
import net.ornithemc.osl.commands.api.command.argument.RangeArgument;
import net.ornithemc.osl.commands.api.command.argument.RotationArgument;
import net.ornithemc.osl.commands.api.command.argument.ScoreHolderArgument;
import net.ornithemc.osl.commands.api.command.argument.ScoreboardCriterionArgument;
import net.ornithemc.osl.commands.api.command.argument.ScoreboardDisplaySlotArgument;
import net.ornithemc.osl.commands.api.command.argument.SlotArgument;
import net.ornithemc.osl.commands.api.command.argument.StatusEffectArgument;
import net.ornithemc.osl.commands.api.command.argument.TeamArgument;
import net.ornithemc.osl.commands.api.command.argument.TextArgument;
import net.ornithemc.osl.commands.api.command.argument.Vec2fArgument;
import net.ornithemc.osl.commands.api.command.argument.Vec3dArgument;
import net.ornithemc.osl.commands.impl.BrigadierCommandsImpl;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.networking.api.PacketBuffer;

public final class ArgumentTypes {

	private static final Map<Class<?>, Entry<?>> BY_TYPE = Maps.newHashMap();
	private static final Map<NamespacedIdentifier, Entry<?>> BY_IDENTIFIER = Maps.newHashMap();

	public static void init() {
		BrigadierArgumentSerializers.init();

		register("entity", EntityArgument.class, new EntityArgument.Serializer());
		register("game_profile", GameProfileArgument.class, new EmptyArgumentSerializer<>(GameProfileArgument::gameProfiles));
		register("block_pos", BlockPosArgument.class, new EmptyArgumentSerializer<>(BlockPosArgument::blockPos));
		register("column_pos", ColumnPosArgument.class, new EmptyArgumentSerializer<>(ColumnPosArgument::columnPos));
		register("vec3", Vec3dArgument.class, new EmptyArgumentSerializer<>(Vec3dArgument::vec3d));
		register("vec2", Vec2fArgument.class, new EmptyArgumentSerializer<>(Vec2fArgument::vec2f));
		register("block_state", BlockStateArgument.class, new EmptyArgumentSerializer<>(BlockStateArgument::block));
		register("block_predicate", BlockPredicateArgument.class, new EmptyArgumentSerializer<>(BlockPredicateArgument::blockPredicate));
		register("legacy_block_state", LegacyBlockStateArgument.class, new EmptyArgumentSerializer<>(LegacyBlockStateArgument::block));
		register("legacy_block_predicate", LegacyBlockPredicateArgument.class, new EmptyArgumentSerializer<>(LegacyBlockPredicateArgument::blockPredicate));
		register("item_stack", ItemArgument.class, new EmptyArgumentSerializer<>(ItemArgument::item));
		register("item_predicate", ItemPredicateArgument.class, new EmptyArgumentSerializer<>(ItemPredicateArgument::itemPredicate));
		register("color", ColorArgument.class, new EmptyArgumentSerializer<>(ColorArgument::color));
		register("component", TextArgument.class, new EmptyArgumentSerializer<>(TextArgument::text));
		register("message", MessageArgument.class, new EmptyArgumentSerializer<>(MessageArgument::message));
		register("nbt", NbtCompoundArgument.class, new EmptyArgumentSerializer<>(NbtCompoundArgument::nbtCompound));
		register("objective", ObjectiveArgument.class, new EmptyArgumentSerializer<>(ObjectiveArgument::objective));
		register("objective_criteria", ScoreboardCriterionArgument.class, new EmptyArgumentSerializer<>(ScoreboardCriterionArgument::criterion));
		register("operation", OperationArgument.class, new EmptyArgumentSerializer<>(OperationArgument::operation));
		register("rotation", RotationArgument.class, new EmptyArgumentSerializer<RotationArgument>(RotationArgument::rotation));
		register("scoreboard_slot", ScoreboardDisplaySlotArgument.class, new EmptyArgumentSerializer<>(ScoreboardDisplaySlotArgument::displaySlot));
		register("score_holder", ScoreHolderArgument.class, new ScoreHolderArgument.Serializer());
		register("swizzle", AxesArgument.class, new EmptyArgumentSerializer<>(AxesArgument::axes));
		register("team", TeamArgument.class, new EmptyArgumentSerializer<>(TeamArgument::team));
		register("item_slot", SlotArgument.class, new EmptyArgumentSerializer<>(SlotArgument::slot));
		register("resource_location", IdentifierArgument.class, new EmptyArgumentSerializer<>(IdentifierArgument::identifier));
		register("mob_effect", StatusEffectArgument.class, new EmptyArgumentSerializer<>(StatusEffectArgument::effect));
		register("entity_anchor", AnchorArgument.class, new EmptyArgumentSerializer<>(AnchorArgument::anchor));
		register("int_range", RangeArgument.Ints.class, new RangeArgument.Ints.Serializer());
        register("float_range", RangeArgument.Floats.class, new RangeArgument.Floats.Serializer());
		register("item_enchantment", EnchantmentArgument.class, new EmptyArgumentSerializer<>(EnchantmentArgument::enchantment));
	}

	private static <T extends ArgumentType<?>> void register(String identifier, Class<T> type, ArgumentSerializer<T> serializer) {
		register(NamespacedIdentifiers.from("minecraft", identifier), type, serializer);
	}

	public static <T extends ArgumentType<?>> void register(NamespacedIdentifier identifier, Class<T> type, ArgumentSerializer<T> serializer) {
		if (BY_TYPE.containsKey(type)) {
			throw new IllegalArgumentException("Class " + type.getName() + " already has a serializer!");
		}
		if (BY_IDENTIFIER.containsKey(identifier)) {
			throw new IllegalArgumentException("'" + identifier + "' is already a registered serializer!");
		}

		Entry<T> entry = new Entry<>(type, identifier, serializer);

		BY_TYPE.put(type, entry);
		BY_IDENTIFIER.put(identifier, entry);
	}

	private static Entry<?> get(NamespacedIdentifier identifier) {
		return BY_IDENTIFIER.get(identifier);
	}

	private static Entry<?> get(ArgumentType<?> argument) {
		return BY_TYPE.get(argument.getClass());
	}

	public static <T extends ArgumentType<?>> void serialize(PacketBuffer buffer, T argument) {
		@SuppressWarnings("unchecked")
		Entry<T> entry = (Entry<T>) get(argument);

		if (entry == null) {
			BrigadierCommandsImpl.LOGGER.error("Could not serialize {} ({}) - will not be sent to client!", argument, argument.getClass());
			buffer.writeNamespacedIdentifier(NamespacedIdentifiers.from(""));
		} else {
			buffer.writeNamespacedIdentifier(entry.identifier);
			entry.serializer.serialize(argument, buffer);
		}
	}

	public static ArgumentType<?> deserialize(PacketBuffer buffer) {
		NamespacedIdentifier identifier = buffer.readNamespacedIdentifier();
		Entry<?> entry = ArgumentTypes.get(identifier);

		if (entry == null) {
			BrigadierCommandsImpl.LOGGER.error("Could not deserialize {}", identifier);
			return null;
		} else {
			return entry.serializer.deserialize(buffer);
		}
	}

	public static class Entry<T extends ArgumentType<?>> {

		public final Class<T> type;
		public final NamespacedIdentifier identifier;
		public final ArgumentSerializer<T> serializer;

		private Entry(Class<T> type, NamespacedIdentifier identifier, ArgumentSerializer<T> serializer) {
			this.type = type;
			this.identifier = identifier;
			this.serializer = serializer;
		}
	}
}
