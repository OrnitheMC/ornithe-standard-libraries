package net.ornithemc.osl.commands.api.argument;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Vec3d;

import net.ornithemc.osl.commands.api.AbstractCommandSourceStack;
import net.ornithemc.osl.commands.api.BrigadierCommandManager;
import net.ornithemc.osl.commands.api.SuggestionProvider;

public class Vec3dArgument implements ArgumentType<Coordinates> {

	private static final Collection<String> EXAMPLES = Arrays.asList("0 0 0", "~ ~ ~", "^ ^ ^", "^1 ^ ^-5","0.1 -0.5 .9", "~0.5 ~1 ~-5");
	public static final SimpleCommandExceptionType INCOMPLETE_EXCEPTION = new SimpleCommandExceptionType((Message)new TranslatableText("argument.pos3d.incomplete"));
	public static final SimpleCommandExceptionType MIXED_TYPE_EXCEPTION = new SimpleCommandExceptionType((Message)new TranslatableText("argument.pos.mixed"));

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

	public static Vec3d getVec3d(CommandContext<AbstractCommandSourceStack<?>> ctx, String arg) throws CommandSyntaxException {
		return ctx.getArgument(arg, Coordinates.class).getPosition(ctx.getSource());
	}

	public static Coordinates getCoordinates(CommandContext<AbstractCommandSourceStack<?>> ctx, String arg) {
		return ctx.getArgument(arg, Coordinates.class);
	}

	@Override
	public Coordinates parse(StringReader reader) throws CommandSyntaxException {
		if (reader.canRead() && reader.peek() == '^') {
			return LocalCoordinates.parse(reader);
		}
		return GlobalCoordinates.parseDouble(reader, this.centered);
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
		if (ctx.getSource() instanceof SuggestionProvider) {
			String s = builder.getRemaining();
			Collection<SuggestionProvider.Coordinate> coordinates = !s.isEmpty() && s.charAt(0) == '^'
				? Collections.singleton(SuggestionProvider.Coordinate.DEFAULT_LOCAL)
				: ((SuggestionProvider) ctx.getSource()).getCoordinates(true);
			return SuggestionProvider.suggestCoordinates(s, coordinates, builder, BrigadierCommandManager.validator(this::parse));
		}
		return Suggestions.empty();
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
