package net.ornithemc.osl.networking.impl;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;

import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import net.ornithemc.osl.core.impl.util.MinecraftVersion;

public class NetworkingMixinPlugin implements IMixinConfigPlugin {

	public static final boolean MINECRAFT_DISCONNECT_METHOD_EXISTS = MinecraftVersion.resolve().compareTo("19w06a") >= 0;

	@Override
	public void onLoad(String mixinPackage) {
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if ("net.ornithemc.osl.networking.impl.mixin.client.MinecraftMixinNew".equals(mixinClassName)) {
			return MINECRAFT_DISCONNECT_METHOD_EXISTS;
		}
		if ("net.ornithemc.osl.networking.impl.mixin.client.MinecraftMixinOld".equals(mixinClassName)) {
			return !MINECRAFT_DISCONNECT_METHOD_EXISTS;
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
