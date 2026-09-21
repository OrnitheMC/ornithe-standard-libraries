package net.ornithemc.osl.datagen.impl.mixin;

import com.google.gson.JsonObject;
import net.minecraft.unmapped.C_23159014;
import net.ornithemc.osl.datagen.impl.access.RecipeBuilderAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(C_23159014.class)
public interface C_23159014OldMixin extends RecipeBuilderAccess {
    @Shadow
    JsonObject m_13901626();

    @Override
    default JsonObject getRecipe() {
        return this.m_13901626();
    }
}
