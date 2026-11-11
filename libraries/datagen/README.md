# Data Generator API

The Data Generator API is a library that provides a simple and intuitive way to generate data files for Minecraft mods. 
It allows mod developers to easily create block models, item models, and other data files without having to manually write JSON files.

## Registering Data Generators
### Entrypoint

The Data Generator API provides an entrypoint that allows mod developers to register their data generators. 
This entrypoint is used to register the data generators that will be used to generate the data files for the mod.

An example is shown below.

`fabric.mod.json`
```json
{
  ...
  "entrypoints": {
    ...
    "datagen": [
      "com.example.ExampleDataGenerator"
    ],
    ...
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
        pack.addProvider(ExampleModelProvider::new);
    }
}
```

### Mod Data Providers

Mod data providers are used to provide data to the pack generator.
There are a few builtin providers, such as the model provider, and language provider.

An example of the model provider is shown below.

```java
package com.example;

import net.fabricmc.loader.api.ModContainer;
import net.minecraft.block.Blocks;
import net.ornithemc.osl.datagen.api.model.block.BlockModelTemplates;
import net.ornithemc.osl.datagen.api.model.ModelGenerator;
import net.ornithemc.osl.datagen.api.provider.ModModelsProvider;
import net.ornithemc.osl.datagen.api.PackGenerator;

public class ExampleModelProvider extends ModModelsProvider {
    public ExampleModelProvider(PackGenerator generator, ModContainer mod) {
        super(generator, mod);
    }
    
    public void generateModels(ModelGenerator generator) {
        BlockModelTemplates templates = generator.blockTemplates();
        templates.simpleBlock(Blocks.OAK_PLANKS, block("oak_planks"));
    }
}
```

For the full list of builtin providers, see package `net.ornithemc.osl.datagen.api.provider`.
