package net.ornithemc.osl.commands.api.command.argument;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import net.ornithemc.osl.commands.api.command.SharedSuggestionProvider;
import net.ornithemc.osl.commands.api.command.argument.AnchorArgument.Anchor;
import net.ornithemc.osl.commands.api.server.command.CommandSourceStack;
import net.ornithemc.osl.text.api.TextComponents;

public class AnchorArgument implements ArgumentType<Anchor> {

	private static final Collection<String> EXAMPLES = Arrays.asList("eyes", "feet");
	private static final DynamicCommandExceptionType INVALID_EXCEPTION = new DynamicCommandExceptionType(anchor -> TextComponents.translatable("argument.anchor.invalid", anchor));

	public static AnchorArgument anchor() {
		return new AnchorArgument();
	}

	public static Anchor getAnchor(CommandContext<?> ctx, String arg) {
		return ctx.getArgument(arg, Anchor.class);
	}

	@Override
	public Anchor parse(StringReader reader) throws CommandSyntaxException {
		int startCursor = reader.getCursor();

		String name = reader.readUnquotedString();
		Anchor anchor = Anchor.byName(name);

		if (anchor == null) {
			reader.setCursor(startCursor);
			throw INVALID_EXCEPTION.createWithContext(reader, name);
		}

		return anchor;
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
		return SharedSuggestionProvider.suggestMatching(Anchor.BY_NAME.keySet(), builder);
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}

	public enum Anchor {

		FEET("feet", (pos, entity) -> pos),
		EYES("eyes", (pos, entity) -> new Vec3d(pos.x, pos.y + entity.getEyeHeight(), pos.z));

		private static final Map<String, Anchor> BY_NAME;

		private final String name;
		private final BiFunction<Vec3d, Entity, Vec3d> transform;

		private Anchor(String name, BiFunction<Vec3d, Entity, Vec3d> transform) {
			this.name = name;
			this.transform = transform;
		}

		public static Anchor byName(String name) {
			return BY_NAME.get(name);
		}

		public Vec3d apply(Entity entity) {
			return this.transform.apply(new Vec3d(entity.x, entity.y, entity.z), entity);
		}

		public Vec3d apply(CommandSourceStack source) {
			Entity entity = source.getEntity();

			if (entity == null) {
				return source.getPos();
			} else {
				return this.transform.apply(source.getPos(), entity);
			}
		}

		static {
			BY_NAME = new HashMap<>();

			for (Anchor anchor : Anchor.values()) {
				BY_NAME.put(anchor.name, anchor);
			}
		}
	}
}
