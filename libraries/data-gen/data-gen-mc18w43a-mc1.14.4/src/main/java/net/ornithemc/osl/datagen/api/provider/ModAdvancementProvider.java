package net.ornithemc.osl.datagen.api.provider;

import com.google.gson.JsonElement;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.advancement.Advancement;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.datagen.api.PackGenerator;
import net.ornithemc.osl.resource.loader.api.resource.ResourcePath;
import net.ornithemc.osl.resource.loader.api.resource.ResourceType;

import java.nio.file.Path;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class ModAdvancementProvider extends GsonPackProvider {
    protected ModAdvancementProvider(PackGenerator generator, ModContainer mod) {
        super(generator, mod);
    }

    @Override
    protected final void generate(BiConsumer<NamespacedIdentifier, JsonElement> consumer) {
        Consumer<Advancement> advancementConsumer = advancement -> {
            consumer.accept(advancement.getId(), advancement.builder().toJson());
        };

        generateAdvancements(advancementConsumer);
    }

    public abstract void generateAdvancements(Consumer<Advancement> consumer);

    @Override
    protected Path getPath(NamespacedIdentifier id) {
        return generator.getOutputPath().resolve(ResourcePath.nameOf(ResourceType.SERVER_DATA, id.prefixed("advancements/").suffixed(".json")));
    }

    @Override
    public String getProviderName() {
        return "Advancements";
    }
}
