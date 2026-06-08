package net.ornithemc.osl.commands.api.command.argument;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.resource.Identifier;

import net.ornithemc.osl.commands.api.command.SharedSuggestionProvider;
import net.ornithemc.osl.text.api.TextComponents;

public class EnchantmentArgument implements ArgumentType<Enchantment> {

	private static final Collection<String> EXAMPLES = Arrays.asList("unbreaking", "silk_touch");
	public static final DynamicCommandExceptionType UNKNOWN_ENCHANTMENT_EXCEPTION = new DynamicCommandExceptionType(enchantment -> TextComponents.translatable("enchantment.unknown", enchantment));

	public static EnchantmentArgument enchantment() {
		return new EnchantmentArgument();
	}

	public static Enchantment getEnchantment(CommandContext<?> ctx, String arg) {
		return ctx.getArgument(arg, Enchantment.class);
	}

	@Override
	public Enchantment parse(StringReader reader) throws CommandSyntaxException {
		int startCursor = reader.getCursor();

		Identifier key = IdentifierParser.parseIdentifier(reader);
		Enchantment enchantment = Enchantment.REGISTRY.get(key);

		if (enchantment == null) {
			reader.setCursor(startCursor);
			throw UNKNOWN_ENCHANTMENT_EXCEPTION.createWithContext(reader, key);
		}

		return enchantment;
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
		return SharedSuggestionProvider.suggestResource(Enchantment.REGISTRY.keySet(), builder);
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
