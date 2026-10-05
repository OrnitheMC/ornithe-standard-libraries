package net.ornithemc.osl.datagen.impl.mixin;

import com.google.gson.JsonObject;
import net.minecraft.unmapped.C_23159014;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import net.ornithemc.osl.datagen.impl.access.RecipeBuilderAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Conditional(minecraftVersion = @Version(">=19w03a"))
@Mixin(C_23159014.class)
public interface C_23159014NewMixin extends RecipeBuilderAccess {
    @Shadow
    JsonObject m_77277228();

    @Override
    default JsonObject getRecipe() {
        return this.m_77277228();
    }
}
