package net.ornithemc.osl.datagen.impl.mixin;

import net.minecraft.data.DataProvider;
import net.ornithemc.osl.datagen.impl.PackProviderExtensionImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DataProvider.class)
public interface DataProviderMixin extends PackProviderExtensionImpl {
    @Shadow
    String getName();

    @Override
    default String getProviderName() {
        return this.getName();
    }
}
