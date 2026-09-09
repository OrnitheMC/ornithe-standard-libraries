package net.ornithemc.osl.entities.impl;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;

import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import net.ornithemc.osl.core.impl.util.MinecraftVersion;

public class EntitiesMixinPlugin implements IMixinConfigPlugin {

	public static final boolean VANILLA_ENTITY_TYPES_HAVE_IDS = (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
		? MinecraftVersion.resolve().compareTo("a1.0.17") >= 0
		: MinecraftVersion.resolve().compareTo("a0.1.4") >= 0;

	@Override
	public void onLoad(String mixinPackage) {
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if ("net.ornithemc.osl.entities.impl.mixin.common.EntitiesMixinNew".equals(mixinClassName)) {
			return VANILLA_ENTITY_TYPES_HAVE_IDS;
		}
		if ("net.ornithemc.osl.entities.impl.mixin.common.EntitiesMixinOld".equals(mixinClassName)) {
			return !VANILLA_ENTITY_TYPES_HAVE_IDS;
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
