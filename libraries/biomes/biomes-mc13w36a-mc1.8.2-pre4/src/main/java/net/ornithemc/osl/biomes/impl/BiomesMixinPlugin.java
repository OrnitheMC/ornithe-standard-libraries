package net.ornithemc.osl.biomes.impl;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;

import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import net.ornithemc.osl.core.impl.util.MinecraftVersion;

public class BiomesMixinPlugin implements IMixinConfigPlugin {

	public static final boolean BIOME_IDS_BEYOND_255_SUPPORTED = false;

	@Override
	public void onLoad(String mixinPackage) {
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if ("net.ornithemc.osl.biomes.impl.mixin.common.BiomeMixinNew".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("14w05a") >= 0;
		}
		if ("net.ornithemc.osl.biomes.impl.mixin.common.BiomeMixinOld".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("14w05a") < 0;
		}

		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}
}
