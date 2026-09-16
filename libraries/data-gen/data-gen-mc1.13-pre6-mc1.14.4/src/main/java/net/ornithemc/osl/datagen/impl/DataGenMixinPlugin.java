package net.ornithemc.osl.datagen.impl;

import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class DataGenMixinPlugin implements IMixinConfigPlugin {

	@Override
	public void onLoad(String mixinPackage) {
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if ("net.ornithemc.osl.datagen.impl.mixin.C_23159014NewMixin".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("19w03a") >= 0;
		}

		if ("net.ornithemc.osl.datagen.impl.mixin.C_23159014OldMixin".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("19w03a") < 0;
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
