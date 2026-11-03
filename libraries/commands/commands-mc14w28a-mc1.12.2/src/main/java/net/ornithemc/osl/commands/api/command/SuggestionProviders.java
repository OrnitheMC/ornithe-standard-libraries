package net.ornithemc.osl.commands.api.command;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;

public class SuggestionProviders {

	private static final Map<NamespacedIdentifier, SuggestionProvider<SharedSuggestionProvider>> REGISTRY = new HashMap<>();
	private static final NamespacedIdentifier DEFAULT_IDENTIFIER = NamespacedIdentifiers.from("ask_server");

	public static final SuggestionProvider<SharedSuggestionProvider> ASK_SERVER = register(DEFAULT_IDENTIFIER,
			(ctx, builder) -> ctx.getSource().requestSuggestions(ctx, builder));
/*	public static final SuggestionProvider<CommandSourceStack> ALL_RECIPES = register(
			"all_recipes",
			(ctx, builder) -> SuggestionProvider.suggestResource(ctx.getSource().getAvailableRecipes(), builder));
	public static final SuggestionProvider<CommandSourceStack> AVAILABLE_SOUNDS = register(
			"available_sounds",
			(ctx, builder) -> SuggestionProvider.suggestResource(ctx.getSource().getAvailableSounds(), builder));
	public static final SuggestionProvider<CommandSourceStack> SUMMONABLE_ENTITIES = register(
			"summonable_entities",
			(ctx, builder) -> SuggestionProvider.suggestResource(
					Registry.ENTITY_TYPE.stream().filter(EntityType::isSummonable), builder, EntityType::getKey,
					entity -> new TranslatableText(Utils.makeTranslationKey("entity", EntityType.getKey(entity)))));*/

	private static <S extends SharedSuggestionProvider> SuggestionProvider<S> register(String name, SuggestionProvider<SharedSuggestionProvider> provider) {
		return register(NamespacedIdentifiers.from(name), provider);
	}

	public static <S extends SharedSuggestionProvider> SuggestionProvider<S> register(NamespacedIdentifier identifier, SuggestionProvider<SharedSuggestionProvider> provider) {
		if (REGISTRY.containsKey(identifier)) {
			throw new IllegalArgumentException("A command suggestion provider is already registered with the name " + identifier);
		} else {
			REGISTRY.put(identifier, provider);
			return (SuggestionProvider<S>) new IdentifiableSuggestionProvider(identifier, provider);
		}
	}

	public static SuggestionProvider<SharedSuggestionProvider> get(NamespacedIdentifier identifier) {
		return REGISTRY.getOrDefault(identifier, ASK_SERVER);
	}

	public static NamespacedIdentifier getIdentifier(SuggestionProvider<SharedSuggestionProvider> provider) {
		return provider instanceof IdentifiableSuggestionProvider
			? ((IdentifiableSuggestionProvider) provider).identifier
			: DEFAULT_IDENTIFIER;
	}

	public static SuggestionProvider<SharedSuggestionProvider> getIdentifiable(SuggestionProvider<SharedSuggestionProvider> provider) {
		return provider instanceof IdentifiableSuggestionProvider ? provider : ASK_SERVER;
	}

	public static class IdentifiableSuggestionProvider implements SuggestionProvider<SharedSuggestionProvider> {

		private final SuggestionProvider<SharedSuggestionProvider> provider;
		private final NamespacedIdentifier identifier;

		public IdentifiableSuggestionProvider(NamespacedIdentifier identifier, SuggestionProvider<SharedSuggestionProvider> provider) {
			this.provider = provider;
			this.identifier = identifier;
		}

		public CompletableFuture<Suggestions> getSuggestions(CommandContext<SharedSuggestionProvider> ctx, SuggestionsBuilder builder) throws CommandSyntaxException {
			return this.provider.getSuggestions(ctx, builder);
		}
	}
}
