package net.ornithemc.osl.items.impl;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;

import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import net.ornithemc.osl.core.impl.util.MinecraftVersion;

public class ItemsMixinPlugin implements IMixinConfigPlugin {

	public static final boolean CREATIVE_MODE_TABS_EXIST = MinecraftVersion.resolve().compareTo("12w21b") >= 0;
	public static final boolean STATS_EXIST = MinecraftVersion.resolve().compareTo("b1.4") >= 0;
	public static final boolean RECIPES_OVERHAULED = MinecraftVersion.resolve().compareTo("b1.2") >= 0;

	@Override
	public void onLoad(String mixinPackage) {
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if ("net.ornithemc.osl.items.impl.mixin.client.CreativeModeTabMixin".equals(mixinClassName)
			|| "net.ornithemc.osl.items.impl.mixin.client.CreativeModeTabDecorationsMixin".equals(mixinClassName)) {
			return CREATIVE_MODE_TABS_EXIST;
		}
		if ("net.ornithemc.osl.items.impl.mixin.client.CreativeModeTabInventoryMixinNew".equals(mixinClassName)) {
			return CREATIVE_MODE_TABS_EXIST && MinecraftVersion.resolve().compareTo("13w03a") >= 0;
		}
		if ("net.ornithemc.osl.items.impl.mixin.client.CreativeModeTabInventoryMixinOld".equals(mixinClassName)) {
			return CREATIVE_MODE_TABS_EXIST && MinecraftVersion.resolve().compareTo("13w03a") < 0;
		}
		if ("net.ornithemc.osl.items.impl.mixin.common.ItemMixinNew".equals(mixinClassName)
			|| "net.ornithemc.osl.items.impl.mixin.common.BlockMixinNew".equals(mixinClassName)
			|| "net.ornithemc.osl.items.impl.mixin.common.StatsMixin".equals(mixinClassName)
			|| "net.ornithemc.osl.items.impl.mixin.common.AchievementsMixin".equals(mixinClassName)) {
			return STATS_EXIST;
		}
		if ("net.ornithemc.osl.items.impl.mixin.common.ItemMixinOld".equals(mixinClassName)
			|| "net.ornithemc.osl.items.impl.mixin.common.BlockMixinOld".equals(mixinClassName)) {
			return !STATS_EXIST;
		}
		if ("net.ornithemc.osl.items.impl.mixin.common.ItemStackMixinNew".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("b1.2") >= 0;
		}
		if ("net.ornithemc.osl.items.impl.mixin.common.ShapedRecipeMixinNew".equals(mixinClassName)
			|| "net.ornithemc.osl.items.impl.mixin.common.ShapelessRecipeMixin".equals(mixinClassName)
			|| "net.ornithemc.osl.items.impl.mixin.common.SmeltingManagerMixin".equals(mixinClassName)) {
			return RECIPES_OVERHAULED;
		}
		if ("net.ornithemc.osl.items.impl.mixin.common.ShapedRecipeMixinOld".equals(mixinClassName)
			|| "net.ornithemc.osl.items.impl.mixin.common.FurnaceBlockEntityMixinOld".equals(mixinClassName)) {
			return !RECIPES_OVERHAULED;
		}
		if ("net.ornithemc.osl.items.impl.mixin.common.FurnaceBlockEntityMixinNew".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("b1.5") >= 0;
		}
		if ("net.ornithemc.osl.items.impl.mixin.common.SmelingManagerMixinNew".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("12w30d") >= 0;
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
