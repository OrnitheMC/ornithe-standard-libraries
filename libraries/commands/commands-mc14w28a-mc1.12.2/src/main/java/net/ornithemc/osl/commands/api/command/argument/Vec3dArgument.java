package net.ornithemc.osl.commands.api.command.argument;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.util.math.Vec3d;

import net.ornithemc.osl.commands.api.Commands;
import net.ornithemc.osl.commands.api.command.SharedSuggestionProvider;
import net.ornithemc.osl.commands.api.command.SharedSuggestionProvider.TextCoordinates;
import net.ornithemc.osl.commands.api.server.command.CommandSourceStack;
import net.ornithemc.osl.text.api.TextComponents;

public class Vec3dArgument implements ArgumentType<Coordinates> {

	private static final Collection<String> EXAMPLES = Arrays.asList("0 0 0", "~ ~ ~", "^ ^ ^", "^1 ^ ^-5","0.1 -0.5 .9", "~0.5 ~1 ~-5");
	public static final SimpleCommandExceptionType INCOMPLETE_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("argument.pos3d.incomplete"));
	public static final SimpleCommandExceptionType MIXED_TYPE_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("argument.pos.mixed"));

	private final boolean centered;

	public Vec3dArgument(boolean centered) {
		this.centered = centered;
	}

	public static Vec3dArgument vec3d() {
		return new Vec3dArgument(true);
	}

	public static Vec3dArgument vec3d(boolean centered) {
		return new Vec3dArgument(centered);
	}

	public static Vec3d getVec3d(CommandContext<CommandSourceStack> ctx, String arg) throws CommandSyntaxException {
		return ctx.getArgument(arg, Coordinates.class).getPosition(ctx.getSource());
	}

	public static Coordinates getCoordinates(CommandContext<CommandSourceStack> ctx, String arg) {
		return ctx.getArgument(arg, Coordinates.class);
	}

	@Override
	public Coordinates parse(StringReader reader) throws CommandSyntaxException {
		if (reader.canRead() && reader.peek() == '^') {
			return LocalCoordinates.parse(reader);
		} else {
			return GlobalCoordinates.parseDouble(reader, this.centered);
		}
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
		if (ctx.getSource() instanceof SharedSuggestionProvider) {
			SharedSuggestionProvider source = (SharedSuggestionProvider) ctx.getSource();
			String prefix = builder.getRemaining();

			Collection<TextCoordinates> coordinates = !prefix.isEmpty() && prefix.charAt(0) == '^'
				? Collections.singleton(TextCoordinates.DEFAULT_LOCAL)
				: source.getRelevantCoordinates(false);

			return SharedSuggestionProvider.suggestHorizontalCoordinates(prefix, coordinates, builder, Commands.validator(this::parse));
		}

		return Suggestions.empty();
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
