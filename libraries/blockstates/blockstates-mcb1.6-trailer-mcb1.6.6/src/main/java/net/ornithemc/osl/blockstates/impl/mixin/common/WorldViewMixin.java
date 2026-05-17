package net.ornithemc.osl.blockstates.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.WorldView;

import net.ornithemc.osl.blockstates.api.world.WorldViewExtension;

@Mixin(WorldView.class)
public interface WorldViewMixin extends WorldViewExtension {
}
