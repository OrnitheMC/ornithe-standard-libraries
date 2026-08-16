package net.ornithemc.osl.networking.impl;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;

import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import net.ornithemc.osl.core.impl.util.MinecraftVersion;

public class NetworkingMixinPlugin implements IMixinConfigPlugin {

	public static final boolean PACKET_READ_QUEUE_IS_LIST = MinecraftVersion.resolve().compareTo("1.6.1") <= 0;

	@Override
	public void onLoad(String mixinPackage) {
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if ("net.ornithemc.osl.networking.impl.mixin.common.RemoteConnectionMixinNew".equals(mixinClassName)) {
			return !PACKET_READ_QUEUE_IS_LIST;
		}
		if ("net.ornithemc.osl.networking.impl.mixin.common.RemoteConnectionMixinOld".equals(mixinClassName)) {
			return PACKET_READ_QUEUE_IS_LIST;
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
