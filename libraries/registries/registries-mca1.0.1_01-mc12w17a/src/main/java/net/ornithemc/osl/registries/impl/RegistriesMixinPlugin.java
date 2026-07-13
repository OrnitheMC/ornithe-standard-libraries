package net.ornithemc.osl.registries.impl;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;

import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import net.ornithemc.osl.core.impl.util.MinecraftVersion;

public class RegistriesMixinPlugin implements IMixinConfigPlugin {

	private static final boolean OLD_WORLD_FORMAT = MinecraftVersion.resolve().compareTo("b1.3") < 0;
	private static final boolean DIMENSIONS_EXIST = MinecraftVersion.resolve().compareTo(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? "a1.2.0" : "a0.2.2") >= 0;

	@Override
	public void onLoad(String mixinPackage) {
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if ("net.ornithemc.osl.registries.impl.mixin.client.MinecraftMixinOld".equals(mixinClassName)
			|| "net.ornithemc.osl.registries.impl.mixin.client.WorldAccessOld".equals(mixinClassName)
			|| "net.ornithemc.osl.registries.impl.mixin.server.MinecraftServerMixinOld".equals(mixinClassName)) {
			return OLD_WORLD_FORMAT;
		}
		if ("net.ornithemc.osl.registries.impl.mixin.common.AlphaWorldStorageMixin".equals(mixinClassName)
			|| "net.ornithemc.osl.registries.impl.mixin.client.WorldAccessNew".equals(mixinClassName)
			|| "net.ornithemc.osl.registries.impl.mixin.client.MinecraftMixinNew".equals(mixinClassName)
			|| "net.ornithemc.osl.registries.impl.mixin.server.MinecraftServerMixinNew".equals(mixinClassName)) {
			return !OLD_WORLD_FORMAT;
		}
		if ("net.ornithemc.osl.registries.impl.mixin.common.WorldMixinNew".equals(mixinClassName)) {
			return OLD_WORLD_FORMAT && DIMENSIONS_EXIST;
		}
		if ("net.ornithemc.osl.registries.impl.mixin.common.WorldMixinOld".equals(mixinClassName)) {
			return OLD_WORLD_FORMAT && !DIMENSIONS_EXIST;
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
