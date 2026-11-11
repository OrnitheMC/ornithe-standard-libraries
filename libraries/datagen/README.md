# Data Generator API

The Data Generator API lets mods generate their resource and data files (block states, models, translations, recipes, tags, advancements and loot tables) from code, instead of writing JSON files by hand. It is built on top of Minecraft's own data generator.

To configure and setup Data generation in your workspace, follow this guide: https://docs.fabricmc.net/develop/loom/fabric-api#data-gen

## Registering Data Generators

Data generators are registered through the `datagen` entrypoint in your `fabric.mod.json`. The entrypoint class must implement `DataGeneratorInitializer`.

`fabric.mod.json`
```json
{
	...
	"entrypoints": {
		...
		"datagen": [
			"com.example.ExampleDataGenerator"
		]
	},
	...
}
```

```java
package com.example;

import net.ornithemc.osl.datagen.api.DataGeneratorInitializer;
import net.ornithemc.osl.datagen.api.ModDataGenerator;
import net.ornithemc.osl.datagen.api.PackGenerator;

public class ExampleDataGenerator implements DataGeneratorInitializer {

	@Override
	public void onDatagenInit(ModDataGenerator dataGenerator) {
		PackGenerator pack = dataGenerator.createPack();

		pack.addProvider(ExampleModelsProvider::new);
		pack.addProvider(ExampleLanguageProvider::new);
	}
}
```

## Packs

Generated files are grouped into packs. A `ModDataGenerator` can create two kinds of packs:

- `createPack()` creates a pack that is written directly to the output directory. This is the pack that ends up in your mod's resources.
- `createBundledResourcePack(NamespacedIdentifier id)` creates a pack that is written to `resourcepacks/<namespace>/<path>/` inside the output directory. Use this to generate [bundled mod resource packs](../resource-loader/README.md#bundled-mod-resource-packs).

```java
PackGenerator pack = dataGenerator.createPack();
PackGenerator cookiePack = dataGenerator.createBundledResourcePack(NamespacedIdentifiers.from("example", "cookies"));
```

A `pack.mcmeta` file is generated for each pack automatically. The main pack uses your mod's description, bundled packs use their identifier.

Providers are added to a pack with `PackGenerator.addProvider`. It accepts both providers whose constructor takes only a `PackGenerator`, and providers whose constructor takes a `PackGenerator` and a `ModContainer`, so a constructor reference can be passed directly.

## Builtin Providers

The API provides the following data providers. Extend them and implement their abstract method to generate data.

| Provider                 | Generates                          | Package                                   | Availability    |
|--------------------------|------------------------------------|-------------------------------------------|-----------------|
| `ModModelsProvider`      | Block states, block and item models | `net.ornithemc.osl.datagen.api.provider`  | All versions    |
| `ModLanguageProvider`    | Translations                       | `net.ornithemc.osl.datagen.api.provider`  | All versions    |
| `ModRecipesProvider`     | Recipes and recipe advancements    | `net.ornithemc.osl.datagen.api.provider`  | All versions    |
| `ModAdvancementsProvider`| Advancements                       | `net.ornithemc.osl.datagen.api.provider`  | All versions    |
| `ModLootTablesProvider`  | Loot tables                        | `net.ornithemc.osl.datagen.api.provider`  | 18w43a - 1.14.4 |
| `ModBlockTagsProvider`   | Block tags                         | `net.ornithemc.osl.datagen.api.provider.tag` | All versions |
| `ModItemTagsProvider`    | Item tags                          | `net.ornithemc.osl.datagen.api.provider.tag` | All versions |
| `ModFluidTagsProvider`   | Fluid tags                         | `net.ornithemc.osl.datagen.api.provider.tag` | All versions |
| `ModEntityTypeTagsProvider` | Entity type tags                | `net.ornithemc.osl.datagen.api.provider.tag` | 18w43a - 1.14.4 |

### Models

`ModModelsProvider` generates block states, block models and item models. Models are generated in `generateModels` through the given `ModelGenerator`.

The provider has `block(...)` and `item(...)` helper methods that prefix an identifier with `block/` or `item/`, which is useful for referencing textures and models.

For common block shapes, `ModelGenerator.blockTemplates()` returns a set of templates that generate the block state, the block models and the item model in one go. Templates are available for simple blocks, slabs, stairs, walls, fences, fence gates, buttons, doors, trapdoors, pressure plates and leaves.

```java
package com.example;

import net.fabricmc.loader.api.ModContainer;

import net.ornithemc.osl.datagen.api.PackGenerator;
import net.ornithemc.osl.datagen.api.model.ModelGenerator;
import net.ornithemc.osl.datagen.api.model.block.BlockModelTemplates;
import net.ornithemc.osl.datagen.api.provider.ModModelsProvider;

public class ExampleModelsProvider extends ModModelsProvider {

	public ExampleModelsProvider(PackGenerator generator, ModContainer mod) {
		super(generator, mod);
	}

	@Override
	public void generateModels(ModelGenerator generator) {
		BlockModelTemplates templates = generator.blockTemplates();

		templates
			.simpleBlock(ExampleBlocks.COOKIE_BLOCK, block("example:cookie_block"))
			.simpleSlab(ExampleBlocks.COOKIE_SLAB, block("example:cookie_block"), block("example:cookie_block"))
			.simpleStairs(ExampleBlocks.COOKIE_STAIRS, block("example:cookie_block"))
			.fence(ExampleBlocks.COOKIE_FENCE, block("example:cookie_block"));
	}
}
```

For blocks that do not fit a template, models and block states can be built manually:

- `ModelBuilder` builds a block or item model from a parent model and textures. `BlockModelTemplates` has static methods such as `cubeAll`, `cubeColumn` and `cross` that create `ModelBuilder`s for vanilla parent models, and `ItemModelTemplates.basic` creates a generated item model.
- `ModelGenerator.blockModel` writes a block model and returns its identifier, so it can be referenced from a block state.
- `ModelGenerator.itemModel` writes an item model.
- `ModelGenerator.blockstate` writes a block state from a `BlockModelDefinitionBuilder`. Block states can use `variant`s (matched with `BlockModelDefinitionBuilder.predicate`) or multipart `selector`s (matched with `ConditionsBuilder`). The models for each variant or selector are described with `VariantsBuilder`, which supports rotations, uv lock and weighted random models.

```java
@Override
public void generateModels(ModelGenerator generator) {
	// a column block with a different texture for the ends
	NamespacedIdentifier model = generator.blockModel(ExampleBlocks.COOKIE_JAR, BlockModelTemplates.cubeColumn(
		block("example:cookie_jar_side"),
		block("example:cookie_jar_top")
	));

	generator.blockstate(ExampleBlocks.COOKIE_JAR, BlockModelDefinitionBuilder.createSimple(model));
	generator.itemModel(ExampleBlocks.COOKIE_JAR, ModelBuilder.create(model));

	// a block with randomly rotated models
	NamespacedIdentifier crumbs = generator.blockModel(ExampleBlocks.CRUMBS, BlockModelTemplates.cubeAll(block("example:crumbs")));

	generator.blockstate(ExampleBlocks.CRUMBS, BlockModelDefinitionBuilder.create()
		.selector(VariantsBuilder.multi(
			VariantsBuilder.of(crumbs),
			VariantsBuilder.of(crumbs).y(90),
			VariantsBuilder.of(crumbs).y(180),
			VariantsBuilder.of(crumbs).y(270)
		)));

	// a simple generated item model
	generator.itemModel(ExampleItems.COOKIE, ItemModelTemplates.basic(item("example:cookie")));
}
```

### Translations

`ModLanguageProvider` generates a language file. By default it generates `en_us`, but a different language code can be passed to the constructor. Translations are added in `generateTranslations`, either by translation key, or directly for blocks, items, entity types and biomes. Existing `.lang` or `.json` language files can be included with `add(Path)`.

Translations are written in the order they are added.

```java
package com.example;

import net.fabricmc.loader.api.ModContainer;

import net.ornithemc.osl.datagen.api.PackGenerator;
import net.ornithemc.osl.datagen.api.provider.ModLanguageProvider;

public class ExampleLanguageProvider extends ModLanguageProvider {

	public ExampleLanguageProvider(PackGenerator generator, ModContainer mod) {
		super(generator, mod);
	}

	@Override
	protected void generateTranslations(TranslationBuilder builder) {
		builder.add(ExampleBlocks.COOKIE_BLOCK, "Block of Cookie");
		builder.add(ExampleItems.COOKIE, "Cookie");
		builder.add("itemGroup.example.cookies", "Cookies");
	}
}
```

```java
package com.example;

import net.fabricmc.loader.api.ModContainer;

import net.ornithemc.osl.datagen.api.PackGenerator;
import net.ornithemc.osl.datagen.api.provider.ModLanguageProvider;

public class ExampleFrenchLanguageProvider extends ModLanguageProvider {

	public ExampleFrenchLanguageProvider(PackGenerator generator, ModContainer mod) {
		super(generator, mod, "fr_fr");
	}

	@Override
	protected void generateTranslations(TranslationBuilder builder) {
		builder.add(ExampleItems.COOKIE, "Biscuit");
	}
}
```

### Recipes, Tags, Advancements and Loot Tables

These providers wrap Minecraft's own data providers, so they are used with the vanilla builders for each data type:

- `ModRecipesProvider`: implement `generateRecipes` and pass recipes built with the vanilla recipe builders to the consumer. Recipe advancements are generated along with the recipes. Use the `id(String)` helper to prefix recipe ids with your mod id. See vanilla's `RecipesProvider` for usage examples.
- `ModAdvancementsProvider`: implement `generateAdvancements` and pass advancements built with `Advancement.builder()` to the consumer.
- `ModBlockTagsProvider`, `ModItemTagsProvider`, `ModFluidTagsProvider` and `ModEntityTypeTagsProvider`: implement `addTags` and add tag entries the same way vanilla's tag providers do. Use `ModTagsProvider` for tags of other registries.
- `ModLootTablesProvider`: implement `generateLootTables` and register sub-providers with `addSubProvider`. Extend `ModBlockLootTablesSubProvider` for block loot tables, `ModEntityTypeLootTablesSubProvider` for entity loot tables, and `ModLootTablesSubProvider` for any other loot tables.

Tag providers only take a `PackGenerator` in their constructor, which `addProvider` supports as well:

```java
pack.addProvider(ExampleBlockTagsProvider::new);
```

## Custom Providers

Custom data providers can be made by implementing `ModDataProvider`. Files should be written through the `PackCache` given to `provide`, so that unchanged files are not written again.

For JSON files, the `GsonDataProvider` class provides a base that takes care of serialization. Only the generation of the JSON elements and the resolution of their output paths need to be implemented.

```java
package com.example;

import java.nio.file.Path;
import java.util.function.BiConsumer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.fabricmc.loader.api.ModContainer;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.datagen.api.PackGenerator;
import net.ornithemc.osl.datagen.api.provider.GsonDataProvider;

public class CookiesProvider extends GsonDataProvider {

	public CookiesProvider(PackGenerator generator, ModContainer mod) {
		super(generator, mod);
	}

	@Override
	protected void generate(BiConsumer<NamespacedIdentifier, JsonElement> consumer) {
		JsonObject chocolateChip = new JsonObject();
		chocolateChip.addProperty("crunchiness", 5);

		consumer.accept(NamespacedIdentifiers.from("example", "chocolate_chip"), chocolateChip);
	}

	@Override
	protected Path getPath(NamespacedIdentifier id) {
		return this.generator.getOutputPath().resolve("data/" + id.namespace() + "/cookies/" + id.identifier() + ".json");
	}

	@Override
	public String getProviderName() {
		return "Cookies";
	}
}
```
