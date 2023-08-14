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

import net.minecraft.entity.living.effect.StatusEffect;
import net.minecraft.resource.Identifier;
import net.minecraft.text.TranslatableText;

import net.ornithemc.osl.commands.api.SuggestionProvider;

public class StatusEffectArgument implements ArgumentType<StatusEffect> {

	private static final Collection<String> EXAMPLES = Arrays.asList("spooky", "effect");
	public static final DynamicCommandExceptionType UNKNOWN_STATUS_EFFECT_EXCEPTION = new DynamicCommandExceptionType(effect -> (Message)new TranslatableText("effect.effectNotFound", effect));

	public static StatusEffectArgument effect() {
		return new StatusEffectArgument();
	}

	public static StatusEffect getEffect(CommandContext<?> ctx, String arg) throws CommandSyntaxException {
		return ctx.getArgument(arg, StatusEffect.class);
	}

	@Override
	public StatusEffect parse(StringReader reader) throws CommandSyntaxException {
		int cursor = reader.getCursor();
		Identifier key = IdentifierParser.parse(reader);
		StatusEffect effect = StatusEffect.REGISTRY.get(key);

		if (effect == null) {
			reader.setCursor(cursor);
			throw UNKNOWN_STATUS_EFFECT_EXCEPTION.create(key);
		}

		return effect;
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
		return SuggestionProvider.suggestResource(StatusEffect.REGISTRY.keySet(), builder);
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
