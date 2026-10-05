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

public class ExampleDataGenerator implements DataGeneratorInitializer {
    @Override
    public void onDatagenInit(ModDataGenerator dataGenerator) {
        PackGenerator pack = dataGenerator.createPack();
        pack.addProvider(ExampleModelProvider::new);
    }
}
```

### Pack Providers

Pack providers are used to provide data to the pack generator.
There are a few builtin providers, such as the model provider, and language provider.

An example for the model provider is shown below.

```java
package com.example;

public class ExampleModelProvider extends ModModelProvider {
    public ExampleModelProvider(PackGenerator generator, ModContainer mod) {
        super(generator, mod);
    }
    
    public void generateModels(ModelGenerator generator) {
        BlockModelTemplates templates = generator.blockTemplates();
        templates.simpleBlock(Blocks.OAK_PLANKS, block("oak_planks"));
    }
}
```

Vanilla DataProviders can also be extended to provide data such as tags.
In the extended constructor, replace DataGenerator with PackGenerator and upcast it back to DataGenerator.
