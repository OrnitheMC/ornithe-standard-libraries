package net.ornithemc.osl.items.impl;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;

import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import net.ornithemc.osl.core.impl.util.MinecraftVersion;

public class ItemsMixinPlugin implements IMixinConfigPlugin {

	public static final boolean SPECIAL_BLOCK_ITEM_HANDLING = MinecraftVersion.resolve().compareTo("13w37a") >= 0;
	public static final boolean BLOCK_ITEMS_MAP_EXISTS = MinecraftVersion.resolve().compareTo("14w25a") >= 0;

	@Override
	public void onLoad(String mixinPackage) {
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if ("net.ornithemc.osl.items.impl.mixin.common.ItemMixinNew".equals(mixinClassName)) {
			return SPECIAL_BLOCK_ITEM_HANDLING;
		}
		if ("net.ornithemc.osl.items.impl.mixin.common.ItemMixinOld".equals(mixinClassName)) {
			return !SPECIAL_BLOCK_ITEM_HANDLING;
		}
		if ("net.ornithemc.osl.items.impl.mixin.common.ItemMixin_14w21b".equals(mixinClassName)) {
			return !BLOCK_ITEMS_MAP_EXISTS;
		}
		if ("net.ornithemc.osl.items.impl.mixin.common.ItemMixin_14w25a".equals(mixinClassName)) {
			return BLOCK_ITEMS_MAP_EXISTS;
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
