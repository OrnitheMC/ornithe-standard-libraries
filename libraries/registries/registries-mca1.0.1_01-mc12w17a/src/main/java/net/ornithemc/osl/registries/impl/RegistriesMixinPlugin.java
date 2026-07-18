package net.ornithemc.osl.registries.impl;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;

import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import net.ornithemc.osl.core.impl.util.MinecraftVersion;

public class RegistriesMixinPlugin implements IMixinConfigPlugin {

	private static final boolean INTEGRATED_SERVER_EXISTS = MinecraftVersion.resolve().compareTo("12w18a") >= 0;

	@Override
	public void onLoad(String mixinPackage) {
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if ("net.ornithemc.osl.registries.impl.mixin.client.IntegratedServerMixin".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("12w25a") >= 0;
		}
		if ("net.ornithemc.osl.registries.impl.mixin.client.MinecraftMixinOld".equals(mixinClassName)
			|| "net.ornithemc.osl.registries.impl.mixin.server.MinecraftServerOldMixin".equals(mixinClassName)
			|| "net.ornithemc.osl.registries.impl.mixin.server.PlayerManagerMixin".equals(mixinClassName)
			) {
			return !INTEGRATED_SERVER_EXISTS;
		}
		if ("net.ornithemc.osl.registries.impl.mixin.common.MinecraftServerMixin".equals(mixinClassName)
			|| "net.ornithemc.osl.registries.impl.mixin.common.PlayerManagerMixin".equals(mixinClassName)
			) {
			return INTEGRATED_SERVER_EXISTS;
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
