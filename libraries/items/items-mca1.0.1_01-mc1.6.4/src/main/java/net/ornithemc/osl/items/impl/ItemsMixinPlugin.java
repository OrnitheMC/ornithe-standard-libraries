package net.ornithemc.osl.items.impl;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;

import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import net.ornithemc.osl.core.impl.util.MinecraftVersion;

public class ItemsMixinPlugin implements IMixinConfigPlugin {

	public static final boolean STATS_EXIST = MinecraftVersion.resolve().compareTo("b1.4") >= 0;

	@Override
	public void onLoad(String mixinPackage) {
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if ("net.ornithemc.osl.items.impl.mixin.common.ItemMixinNew".equals(mixinClassName)
			|| "net.ornithemc.osl.items.impl.mixin.common.BlockMixinNew".equals(mixinClassName)
			|| "net.ornithemc.osl.items.impl.mixin.common.StatsMixin".equals(mixinClassName)) {
			return STATS_EXIST;
		}
		if ("net.ornithemc.osl.items.impl.mixin.common.ItemMixinOld".equals(mixinClassName)
			|| "net.ornithemc.osl.items.impl.mixin.common.BlockMixinOld".equals(mixinClassName)) {
			return !STATS_EXIST;
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
