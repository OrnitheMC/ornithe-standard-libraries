package net.ornithemc.osl.commands.api.command.argument;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import net.ornithemc.osl.commands.api.server.command.CommandSourceStack;
import net.ornithemc.osl.text.api.TextComponents;

public class ItemPredicateArgument implements ArgumentType<ItemPredicateArgument.Result> {

	private static final Collection<String> EXAMPLES = Arrays.asList("stick", "minecraft:stick", "#stick", "#stick{foo=bar}");
	private static final DynamicCommandExceptionType UNKNOWN_ITEM_EXCEPTION = new DynamicCommandExceptionType(item -> TextComponents.translatable("arguments.item.unknown", item));

	public static ItemPredicateArgument itemPredicate() {
		return new ItemPredicateArgument();
	}

	public static Predicate<ItemStack> getItemPredicate(CommandContext<CommandSourceStack> ctx, String arg) throws CommandSyntaxException {
		return ctx.getArgument(arg, Result.class).create(ctx);
	}

	@Override
	public Result parse(StringReader reader) throws CommandSyntaxException {
		ItemParser parser = new ItemParser(reader, true).parse();

		if (parser.getItem() != null) {
			ItemPredicate predicate = new ItemPredicate(
				parser.getItem(),
				parser.getNbt()
			);

			return ctx -> predicate;
		} else {
			String key = parser.getId().toString();

			return ctx -> {
				throw UNKNOWN_ITEM_EXCEPTION.create(key);
			};
		}
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
		StringReader reader = new StringReader(builder.getInput());
		reader.setCursor(builder.getStart());
		ItemParser parser = new ItemParser(reader, true);

		try {
			parser.parse();
		} catch (CommandSyntaxException e) {
		}

		return parser.addSuggestions(builder);
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}

	private static class ItemPredicate implements Predicate<ItemStack> {

		private final Item item;
		private final NbtCompound nbt;

		public ItemPredicate(Item item, NbtCompound nbt) {
			this.item = item;
			this.nbt = nbt;
		}

		@Override
		public boolean test(ItemStack item) {
			return item.getItem() == this.item && NbtUtils.matches(this.nbt, item.getNbt(), true);
		}
	}

	@FunctionalInterface
	public interface Result {

		Predicate<ItemStack> create(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException;

	}
}
