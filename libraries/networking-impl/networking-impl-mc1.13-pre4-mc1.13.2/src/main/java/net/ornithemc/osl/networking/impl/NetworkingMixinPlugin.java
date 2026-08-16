package net.ornithemc.osl.networking.impl;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;

import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import net.ornithemc.osl.core.impl.util.MinecraftVersion;

public class NetworkingMixinPlugin implements IMixinConfigPlugin {

	public static final boolean MINECRAFT_SET_WORLD_HAS_NETWORK_ID_PARAMETER = MinecraftVersion.resolve().compareTo("18w30b") == 0;

	@Override
	public void onLoad(String mixinPackage) {
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if ("net.ornithemc.osl.networking.impl.mixin.client.MinecraftMixin".equals(mixinClassName)) {
			return !MINECRAFT_SET_WORLD_HAS_NETWORK_ID_PARAMETER;
		}
		if ("net.ornithemc.osl.networking.impl.mixin.client.MinecraftMixin18w30b".equals(mixinClassName)) {
			return MINECRAFT_SET_WORLD_HAS_NETWORK_ID_PARAMETER;
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
