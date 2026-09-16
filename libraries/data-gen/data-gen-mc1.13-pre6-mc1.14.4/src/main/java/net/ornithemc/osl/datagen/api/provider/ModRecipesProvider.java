package net.ornithemc.osl.datagen.api.provider;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.data.recipe.RecipesProvider;
import net.minecraft.resource.Identifier;
import net.minecraft.unmapped.C_23159014;
import net.ornithemc.osl.datagen.impl.access.RecipeBuilderAccess;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Consumer;

public abstract class ModRecipesProvider extends RecipesProvider {
    public ModRecipesProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    public void run(HashCache cache) throws IOException {
        Path path = this.generator.getOutput();
        Set<Identifier> set = Sets.newHashSet();
        this.buildRecipes((c_23159014) -> {
            if (!set.add(c_23159014.m_80989661())) {
                throw new IllegalStateException("Duplicate recipe " + c_23159014.m_80989661());
            } else {
                this.save(cache, ((RecipeBuilderAccess) c_23159014).getRecipe(), path.resolve("data/" + c_23159014.m_80989661().getNamespace() + "/recipes/" + c_23159014.m_80989661().getPath() + ".json"));
                JsonObject jsonObject = c_23159014.m_38911806();
                if (jsonObject != null) {
                    this.saveAdvancement(cache, jsonObject, path.resolve("data/" + c_23159014.m_80989661().getNamespace() + "/advancements/" + c_23159014.m_66233363().getPath() + ".json"));
                }

            }
        });
    }

    protected abstract void buildRecipes(Consumer<C_23159014> consumer);

    @Override
    public String getName() {
        return "Mod Recipes";
    }
}
