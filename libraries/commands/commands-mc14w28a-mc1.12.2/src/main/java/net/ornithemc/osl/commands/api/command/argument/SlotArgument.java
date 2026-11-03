package net.ornithemc.osl.commands.api.command.argument;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.ornithemc.osl.commands.api.command.SharedSuggestionProvider;
import net.ornithemc.osl.commands.api.server.command.CommandSourceStack;
import net.ornithemc.osl.text.api.TextComponents;

public class SlotArgument implements ArgumentType<Integer> {

	private static final Collection<String> EXAMPLES = Arrays.asList("container.5", "12", "weapon");
	private static final DynamicCommandExceptionType UNKNOWN_SLOT_EXCEPTION = new DynamicCommandExceptionType(slot -> TextComponents.translatable("slot.unknown", slot));
	private static final Map<String, Integer> SLOTS = new HashMap<>();

	static {
		for (int i = 0; i < 54; i++) {
			SLOTS.put("container." + i, i);
		}

		for (int j = 0; j < 9; j++) {
			SLOTS.put("hotbar." + j, j);
		}

		for (int k = 0; k < 27; k++) {
			SLOTS.put("inventory." + k, 9 + k);
		}

		for (int l = 0; l < 27; l++) {
			SLOTS.put("enderchest." + l, 200 + l);
		}

		for (int m = 0; m < 8; m++) {
			SLOTS.put("villager." + m, 300 + m);
		}

		for (int n = 0; n < 15; n++) {
			SLOTS.put("horse." + n, 500 + n);
		}

		SLOTS.put("weapon", 98);
		SLOTS.put("weapon.mainhand", 98);
		SLOTS.put("weapon.offhand", 99);
		SLOTS.put("armor.feet", 100);
		SLOTS.put("armor.legs", 101);
		SLOTS.put("armor.chest", 102);
		SLOTS.put("armor.head", 103);
		SLOTS.put("horse.saddle", 400);
		SLOTS.put("horse.armor", 401);
		SLOTS.put("horse.chest", 499);
	}

	public static SlotArgument slot() {
		return new SlotArgument();
	}

	public static int getSlot(CommandContext<CommandSourceStack> context, String arg) {
		return context.getArgument(arg, Integer.class);
	}

	@Override
	public Integer parse(StringReader reader) throws CommandSyntaxException {
		String name = reader.readUnquotedString();

		if (!SLOTS.containsKey(name)) {
			throw UNKNOWN_SLOT_EXCEPTION.create(name);
		} else {
			return SLOTS.get(name);
		}
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
		return SharedSuggestionProvider.suggestMatching(SLOTS.keySet(), builder);
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
