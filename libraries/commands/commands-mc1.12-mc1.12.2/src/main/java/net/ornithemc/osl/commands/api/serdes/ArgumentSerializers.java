package net.ornithemc.osl.commands.api.serdes;
/*
import java.util.Collection;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;

import net.minecraft.command.argument.AxesArgument;
import net.minecraft.command.argument.BlockArgument;
import net.minecraft.command.argument.BlockPosArgument;
import net.minecraft.command.argument.BlockPredicateArgument;
import net.minecraft.command.argument.ColorArgument;
import net.minecraft.command.argument.ColumnPosArgument;
import net.minecraft.command.argument.DimensionTypeArgument;
import net.minecraft.command.argument.EnchantmentArgument;
import net.minecraft.command.argument.EntityArgument;
import net.minecraft.command.argument.EntitySummonArgument;
import net.minecraft.command.argument.FunctionArgument;
import net.minecraft.command.argument.GameProfileArgument;
import net.minecraft.command.argument.IdentifierArgument;
import net.minecraft.command.argument.ItemArgument;
import net.minecraft.command.argument.ItemPredicateArgument;
import net.minecraft.command.argument.MessageArgument;
import net.minecraft.command.argument.NbtCompoundArgument;
import net.minecraft.command.argument.NbtPathArgument;
import net.minecraft.command.argument.ObjectiveArgument;
import net.minecraft.command.argument.OperationArgument;
import net.minecraft.command.argument.ParticleArgument;
import net.minecraft.command.argument.RangeArgument;
import net.minecraft.command.argument.RotationArgument;
import net.minecraft.command.argument.ScoreHolderArgument;
import net.minecraft.command.argument.ScoreboardCriterionArgument;
import net.minecraft.command.argument.ScoreboardDisplaySlotArgument;
import net.minecraft.command.argument.SlotArgument;
import net.minecraft.command.argument.StatusEffectArgument;
import net.minecraft.command.argument.TeamArgument;
import net.minecraft.command.argument.TextArgument;
import net.minecraft.command.argument.Vec2fArgument;
import net.minecraft.command.argument.Vec3dArgument;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.Identifier;
import net.ornithemc.osl.commands.api.command.argument.AnchorArgument;
*/
public class ArgumentSerializers {/*
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Map<Class<?>, Entry<?>> BY_TYPE = Maps.newHashMap();
	private static final Map<Identifier, Entry<?>> BY_NAME = Maps.newHashMap();

	public static <T extends ArgumentType<?>> void register(Identifier name, Class<T> type,
			ArgumentSerializer<T> serializer) {
		if (BY_TYPE.containsKey(type)) {
			throw new IllegalArgumentException("Class " + type.getName() + " already has a serializer!");
		}
		if (BY_NAME.containsKey(name)) {
			throw new IllegalArgumentException("'" + name + "' is already a registered serializer!");
		}
		Entry entry = new Entry(type, serializer, name);
		BY_TYPE.put(type, entry);
		BY_NAME.put(name, entry);
	}

	public static void init() {
		BrigadierArgumentSerializers.init();
		ArgumentSerializers.register(new Identifier("minecraft:entity"), EntityArgument.class,
				new EntityArgument.Serializer());
		ArgumentSerializers.register(new Identifier("minecraft:game_profile"), GameProfileArgument.class,
				new EmptyArgumentSerializer<GameProfileArgument>(GameProfileArgument::gameProfiles));
		ArgumentSerializers.register(new Identifier("minecraft:block_pos"), BlockPosArgument.class,
				new EmptyArgumentSerializer<BlockPosArgument>(BlockPosArgument::blockPos));
		ArgumentSerializers.register(new Identifier("minecraft:column_pos"), ColumnPosArgument.class,
				new EmptyArgumentSerializer<ColumnPosArgument>(ColumnPosArgument::columnPos));
		ArgumentSerializers.register(new Identifier("minecraft:vec3"), Vec3dArgument.class,
				new EmptyArgumentSerializer<Vec3dArgument>(Vec3dArgument::vec3d));
		ArgumentSerializers.register(new Identifier("minecraft:vec2"), Vec2fArgument.class,
				new EmptyArgumentSerializer<Vec2fArgument>(Vec2fArgument::vec2f));
		ArgumentSerializers.register(new Identifier("minecraft:block_state"), BlockArgument.class,
				new EmptyArgumentSerializer<BlockArgument>(BlockArgument::block));
		ArgumentSerializers.register(new Identifier("minecraft:block_predicate"), BlockPredicateArgument.class,
				new EmptyArgumentSerializer<BlockPredicateArgument>(BlockPredicateArgument::blockPredicate));
		ArgumentSerializers.register(new Identifier("minecraft:item_stack"), ItemArgument.class,
				new EmptyArgumentSerializer<ItemArgument>(ItemArgument::item));
		ArgumentSerializers.register(new Identifier("minecraft:item_predicate"), ItemPredicateArgument.class,
				new EmptyArgumentSerializer<ItemPredicateArgument>(ItemPredicateArgument::itemPredicate));
		ArgumentSerializers.register(new Identifier("minecraft:color"), ColorArgument.class,
				new EmptyArgumentSerializer<ColorArgument>(ColorArgument::color));
		ArgumentSerializers.register(new Identifier("minecraft:component"), TextArgument.class,
				new EmptyArgumentSerializer<TextArgument>(TextArgument::text));
		ArgumentSerializers.register(new Identifier("minecraft:message"), MessageArgument.class,
				new EmptyArgumentSerializer<MessageArgument>(MessageArgument::message));
		ArgumentSerializers.register(new Identifier("minecraft:nbt"), NbtCompoundArgument.class,
				new EmptyArgumentSerializer<NbtCompoundArgument>(NbtCompoundArgument::nbtCompound));
		ArgumentSerializers.register(new Identifier("minecraft:nbt_path"), NbtPathArgument.class,
				new EmptyArgumentSerializer<NbtPathArgument>(NbtPathArgument::nbtPath));
		ArgumentSerializers.register(new Identifier("minecraft:objective"), ObjectiveArgument.class,
				new EmptyArgumentSerializer<ObjectiveArgument>(ObjectiveArgument::objective));
		ArgumentSerializers.register(new Identifier("minecraft:objective_criteria"), ScoreboardCriterionArgument.class,
				new EmptyArgumentSerializer<ScoreboardCriterionArgument>(ScoreboardCriterionArgument::criterion));
		ArgumentSerializers.register(new Identifier("minecraft:operation"), OperationArgument.class,
				new EmptyArgumentSerializer<OperationArgument>(OperationArgument::operation));
		ArgumentSerializers.register(new Identifier("minecraft:particle"), ParticleArgument.class,
				new EmptyArgumentSerializer<ParticleArgument>(ParticleArgument::particle));
		ArgumentSerializers.register(new Identifier("minecraft:rotation"), RotationArgument.class,
				new EmptyArgumentSerializer<RotationArgument>(RotationArgument::rotation));
		ArgumentSerializers.register(new Identifier("minecraft:scoreboard_slot"), ScoreboardDisplaySlotArgument.class,
				new EmptyArgumentSerializer<ScoreboardDisplaySlotArgument>(ScoreboardDisplaySlotArgument::displaySlot));
		ArgumentSerializers.register(new Identifier("minecraft:score_holder"), ScoreHolderArgument.class,
				new ScoreHolderArgument.Serializer());
		ArgumentSerializers.register(new Identifier("minecraft:swizzle"), AxesArgument.class,
				new EmptyArgumentSerializer<AxesArgument>(AxesArgument::axes));
		ArgumentSerializers.register(new Identifier("minecraft:team"), TeamArgument.class,
				new EmptyArgumentSerializer<TeamArgument>(TeamArgument::team));
		ArgumentSerializers.register(new Identifier("minecraft:item_slot"), SlotArgument.class,
				new EmptyArgumentSerializer<SlotArgument>(SlotArgument::slot));
		ArgumentSerializers.register(new Identifier("minecraft:resource_location"), IdentifierArgument.class,
				new EmptyArgumentSerializer<IdentifierArgument>(IdentifierArgument::identifier));
		ArgumentSerializers.register(new Identifier("minecraft:mob_effect"), StatusEffectArgument.class,
				new EmptyArgumentSerializer<StatusEffectArgument>(StatusEffectArgument::effect));
		ArgumentSerializers.register(new Identifier("minecraft:function"), FunctionArgument.class,
				new EmptyArgumentSerializer<FunctionArgument>(FunctionArgument::functions));
		ArgumentSerializers.register(new Identifier("minecraft:entity_anchor"), AnchorArgument.class,
				new EmptyArgumentSerializer<AnchorArgument>(AnchorArgument::anchor));
		ArgumentSerializers.register(new Identifier("minecraft:int_range"), RangeArgument.Ints.class,
				new RangeArgument.Ints.Serializer());
		ArgumentSerializers.register(new Identifier("minecraft:float_range"), RangeArgument.Floats.class,
				new RangeArgument.Floats.Serializer());
		ArgumentSerializers.register(new Identifier("minecraft:item_enchantment"), EnchantmentArgument.class,
				new EmptyArgumentSerializer<EnchantmentArgument>(EnchantmentArgument::enchantment));
		ArgumentSerializers.register(new Identifier("minecraft:entity_summon"), EntitySummonArgument.class,
				new EmptyArgumentSerializer<EntitySummonArgument>(EntitySummonArgument::entity));
		ArgumentSerializers.register(new Identifier("minecraft:dimension"), DimensionTypeArgument.class,
				new EmptyArgumentSerializer<DimensionTypeArgument>(DimensionTypeArgument::dimension));
	}

	@Nullable
	private static Entry<?> get(Identifier name) {
		return BY_NAME.get(name);
	}

	@Nullable
	private static Entry<?> get(ArgumentType<?> argument) {
		return BY_TYPE.get(argument.getClass());
	}

	public static <T extends ArgumentType<?>> void serialize(PacketByteBuf buffer, T argument) {
		Entry<?> entry = ArgumentSerializers.get(argument);
		if (entry == null) {
			LOGGER.error("Could not serialize {} ({}) - will not be sent to client!", (Object) argument,
					(Object) argument.getClass());
			buffer.writeIdentifier(new Identifier(""));
			return;
		}
		buffer.writeIdentifier(entry.name);
		entry.serializer.serialize(argument, buffer);
	}

	@Nullable
	public static ArgumentType<?> deserialize(PacketByteBuf buffer) {
		Identifier identifier = buffer.readIdentifier();
		Entry<?> entry = ArgumentSerializers.get(identifier);
		if (entry == null) {
			LOGGER.error("Could not deserialize {}", (Object) identifier);
			return null;
		}
		return entry.serializer.deserialize(buffer);
	}

	private static <T extends ArgumentType<?>> void serializeToJson(JsonObject json, T argument) {
		Entry<?> entry = ArgumentSerializers.get(argument);
		if (entry == null) {
			LOGGER.error("Could not serialize argument {} ({})!", (Object) argument, (Object) argument.getClass());
			json.addProperty("type", "unknown");
		} else {
			json.addProperty("type", "argument");
			json.addProperty("parser", entry.name.toString());
			JsonObject jsonObject = new JsonObject();
			entry.serializer.serialize(argument, jsonObject);
			if (jsonObject.size() > 0) {
				json.add("properties", jsonObject);
			}
		}
	}

	public static <S> JsonObject serializeNodeToJson(CommandDispatcher<S> dispatcher, CommandNode<S> node) {
		Collection<String> collection;
		JsonObject jsonObject = new JsonObject();
		if (node instanceof RootCommandNode) {
			jsonObject.addProperty("type", "root");
		} else if (node instanceof LiteralCommandNode) {
			jsonObject.addProperty("type", "literal");
		} else if (node instanceof ArgumentCommandNode) {
			ArgumentSerializers.serializeToJson(jsonObject, ((ArgumentCommandNode) node).getType());
		} else {
			LOGGER.error("Could not serialize node {} ({})!", (Object) node, (Object) node.getClass());
			jsonObject.addProperty("type", "unknown");
		}
		JsonObject jsonObject2 = new JsonObject();
		for (CommandNode<S> commandNode : node.getChildren()) {
			jsonObject2.add(commandNode.getName(), ArgumentSerializers.serializeNodeToJson(dispatcher, commandNode));
		}
		if (jsonObject2.size() > 0) {
			jsonObject.add("children", jsonObject2);
		}
		if (node.getCommand() != null) {
			jsonObject.addProperty("executable", true);
		}
		if (node.getRedirect() != null && !(collection = dispatcher.getPath(node.getRedirect())).isEmpty()) {
			JsonArray jsonArray = new JsonArray();
			for (String string : collection) {
				jsonArray.add(string);
			}
			jsonObject.add("redirect", jsonArray);
		}
		return jsonObject;
	}

	static class Entry<T extends ArgumentType<?>> {
		public final Class<T> type;
		public final ArgumentSerializer<T> serializer;
		public final Identifier name;

		private Entry(Class<T> type, ArgumentSerializer<T> serializer, Identifier name) {
			this.type = type;
			this.serializer = serializer;
			this.name = name;
		}
	}
*/}
