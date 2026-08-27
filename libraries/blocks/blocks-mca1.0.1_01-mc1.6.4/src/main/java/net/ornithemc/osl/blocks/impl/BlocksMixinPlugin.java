package net.ornithemc.osl.blocks.impl;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;

import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import net.ornithemc.osl.core.impl.util.MinecraftVersion;

public class BlocksMixinPlugin implements IMixinConfigPlugin {

	public static final boolean CLIENT_SIDE = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
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
		if ("net.ornithemc.osl.blocks.impl.mixin.common.BlockMixin_1_3_1_13w01a".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("1.3.1") >= 0 && MinecraftVersion.resolve().compareTo("13w01a") <= 0;
		}
		if ("net.ornithemc.osl.blocks.impl.mixin.common.BlockMixin_12w06a".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("12w06a") <= 0;
		}
		if ("net.ornithemc.osl.blocks.impl.mixin.common.BlockMixin_12w40a".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("12w40a") >= 0;
		}
		if ("net.ornithemc.osl.blocks.impl.mixin.client.BlockMixin_13w02a".equals(mixinClassName)
			|| "net.ornithemc.osl.blocks.impl.mixin.common.BlockMixin_13w02a".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("13w02a") >= 0;
		}
		if ("net.ornithemc.osl.blocks.impl.mixin.common.BlockMixin_a1_1_0_12w06a".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo(CLIENT_SIDE ? "a1.1.0" : "a0.2.0") >= 0 && MinecraftVersion.resolve().compareTo("12w06a") <= 0;
		}
		if ("net.ornithemc.osl.blocks.impl.mixin.common.BlockMixin_b1_0_1_3".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("b1.0") >= 0 && MinecraftVersion.resolve().compareTo("1.3") <= 0;
		}
		if ("net.ornithemc.osl.blocks.impl.mixin.common.BlockMixin_b1_6_tb3_1_4_7".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("b1.6-tb3") >= 0 && MinecraftVersion.resolve().compareTo("1.4.7") <= 0;
		}
		if ("net.ornithemc.osl.blocks.impl.mixin.common.BlockMixin_b1_9_pre5_12w38b".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("b1.9-pre5") >= 0 && MinecraftVersion.resolve().compareTo("12w38b") <= 0;
		}
		if ("net.ornithemc.osl.blocks.impl.mixin.common.WorldMixin".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("12w03a") <= 0;
		}
		if ("net.ornithemc.osl.blocks.impl.mixin.common.EndermanEntityMixin".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("b1.8") >= 0;
		}
		if ("net.ornithemc.osl.blocks.impl.mixin.common.StatsMixin".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("b1.4") >= 0;
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
