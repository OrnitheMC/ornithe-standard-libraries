package net.ornithemc.osl.blocks.impl;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;

import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import net.ornithemc.osl.core.impl.util.MinecraftVersion;

public class BlocksMixinPlugin implements IMixinConfigPlugin {

	public static final boolean BLOCK_IS_METHOD_PRESENT = MinecraftVersion.resolve().compareTo("13w01a") >= 0;

	@Override
	public void onLoad(String mixinPackage) {
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if ("net.ornithemc.osl.blocks.impl.mixin.common.BlockMixinNew".equals(mixinClassName)) {
			return BLOCK_IS_METHOD_PRESENT;
		}
		if ("net.ornithemc.osl.blocks.impl.mixin.common.BlockMixinOld".equals(mixinClassName)) {
			return !BLOCK_IS_METHOD_PRESENT;
		}
		if ("net.ornithemc.osl.blocks.impl.mixin.common.RedstoneTorchBlockMixin".equals(mixinClassName)) {
			return !BLOCK_IS_METHOD_PRESENT && MinecraftVersion.resolve().compareTo("a1.0.1") >= 0;
		}
		if ("net.ornithemc.osl.blocks.impl.mixin.common.RepeaterBlockMixin".equals(mixinClassName)) {
			return !BLOCK_IS_METHOD_PRESENT && MinecraftVersion.resolve().compareTo("b1.3") >= 0;
		}
		if ("net.ornithemc.osl.blocks.impl.mixin.common.WorldMixin".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("12w03a") <= 0;
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
