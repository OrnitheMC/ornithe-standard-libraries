package net.ornithemc.osl.commands.api.argument;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.block.Block;
import net.minecraft.resource.Identifier;
import net.minecraft.text.TranslatableText;

import net.ornithemc.osl.commands.api.SuggestionProvider;

public class LegacyBlockArgument implements ArgumentType<Block> {

	private static final Collection<String> EXAMPLES = Arrays.asList("stone", "minecraft:stone");
	public static final DynamicCommandExceptionType UNKNOWN_BLOCK_EXCEPTION = new DynamicCommandExceptionType(block -> (Message)new TranslatableText("block.unknown", block));

	public static LegacyBlockArgument block() {
		return new LegacyBlockArgument();
	}

	public static Block getBlock(CommandContext<?> ctx, String arg) {
		return ctx.getArgument(arg, Block.class);
	}

	@Override
	public Block parse(StringReader reader) throws CommandSyntaxException {
		int cursor = reader.getCursor();
		Identifier key = IdentifierParser.parse(reader);
		Block block = Block.REGISTRY.get(key);

		if (block == null) {
			reader.setCursor(cursor);
			throw UNKNOWN_BLOCK_EXCEPTION.createWithContext(reader, key);
		}

		return block;
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
		return SuggestionProvider.suggestResource(Block.REGISTRY.keySet(), builder);
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
