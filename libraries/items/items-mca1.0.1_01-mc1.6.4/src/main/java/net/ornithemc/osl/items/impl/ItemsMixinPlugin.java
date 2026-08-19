package net.ornithemc.osl.items.impl;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;

import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import net.ornithemc.osl.core.impl.util.MinecraftVersion;

public class ItemsMixinPlugin implements IMixinConfigPlugin {

	public static final boolean STATS_EXIST = MinecraftVersion.resolve().compareTo("b1.4") >= 0;
	public static final boolean RECIPES_OVERHAULED = MinecraftVersion.resolve().compareTo("b1.2") >= 0;
	public static final boolean LOOT_ENTRIES_EXIST = MinecraftVersion.resolve().compareTo("b1.8-pre1") >= 0;
	public static final boolean LOOT_ENTRY_USES_ITEM_STACK = MinecraftVersion.resolve().compareTo("12w50a") >= 0;

	@Override
	public void onLoad(String mixinPackage) {
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if ("net.ornithemc.osl.items.impl.mixin.client.CustomizeFlatWorldScreen_LayerListWidgetMixin".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("12w37a") >= 0;
		}
		if ("net.ornithemc.osl.items.impl.mixin.client.MinecraftMixinNewNew".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("12w34a") >= 0;
		}
		if ("net.ornithemc.osl.items.impl.mixin.client.MinecraftMixinNew".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("12w15a") >= 0 && MinecraftVersion.resolve().compareTo("12w34a") < 0;
		}
		if ("net.ornithemc.osl.items.impl.mixin.client.MinecraftMixinMid".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("1.2.5") == 0;
		}
		if ("net.ornithemc.osl.items.impl.mixin.client.MinecraftMixinOld".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("b1.8-pre2") >= 0 && MinecraftVersion.resolve().compareTo("1.2.5") < 0;
		}
		if ("net.ornithemc.osl.items.impl.mixin.client.ItemRendererMixin_b1_4_01_12w34a_13w01a".equals(mixinClassName)) {
			return (MinecraftVersion.resolve().compareTo("b1.5") < 0) || (MinecraftVersion.resolve().compareTo("12w34a") >= 0 && MinecraftVersion.resolve().compareTo("13w01a") <= 0);
		}
		if ("net.ornithemc.osl.items.impl.mixin.client.ItemRendererMixin_b1_5_12w32a".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("b1.5") >= 0 && MinecraftVersion.resolve().compareTo("12w32a") <= 0;
		}
		if ("net.ornithemc.osl.items.impl.mixin.client.ItemRendererMixin_12w50a".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("12w50a") >= 0;
		}
		if ("net.ornithemc.osl.items.impl.mixin.client.ItemRendererMixin_13w02a".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("13w02a") >= 0;
		}
		if ("net.ornithemc.osl.items.impl.mixin.client.ItemRendererMixin_1_6".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("1.6") >= 0;
		}
		if ("net.ornithemc.osl.items.impl.mixin.client.PlayerRendererMixin".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("a1.0.6") >= 0;
		}
		if ("net.ornithemc.osl.items.impl.mixin.client.PlayerRendererMixinNew".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("a1.2.0") >= 0;
		}
		if ("net.ornithemc.osl.items.impl.mixin.client.WitchRendererMixin".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("12w38a") >= 0;
		}
		if ("net.ornithemc.osl.items.impl.mixin.common.ItemMixinNew".equals(mixinClassName)
			|| "net.ornithemc.osl.items.impl.mixin.common.ItemStatAccess".equals(mixinClassName)
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
		if ("net.ornithemc.osl.items.impl.mixin.common.ItemMixin_1_3_1_13w01a".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("1.3.1") >= 0 && MinecraftVersion.resolve().compareTo("13w02a") < 0;
		}
		if ("net.ornithemc.osl.items.impl.mixin.common.ItemMixin_13w02a".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("13w02a") >= 0;
		}
		if ("net.ornithemc.osl.items.impl.mixin.common.ItemMixin_b1_0_1_3".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("b1.0") >= 0 && MinecraftVersion.resolve().compareTo("1.3") <= 0;
		}
		if ("net.ornithemc.osl.items.impl.mixin.common.FurnaceBlockEntityMixinNew".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("b1.5") >= 0;
		}
		if ("net.ornithemc.osl.items.impl.mixin.common.SmeltingManagerMixinNew".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("12w30d") >= 0;
		}
		if ("net.ornithemc.osl.items.impl.mixin.common.BlockMixin_SilkTouchDropItem".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("b1.9-pre6") >= 0;
		}
		if ("net.ornithemc.osl.items.impl.mixin.client.BlockMixin_PickItem".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("12w15a") >= 0;
		}
		if ("net.ornithemc.osl.items.impl.mixin.common.AxisBlockMixin".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("13w16a") >= 0;
		}
		if ("net.ornithemc.osl.items.impl.mixin.common.LeavesBlockMixin".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("12w49a") >= 0;
		}
		if ("net.ornithemc.osl.items.impl.mixin.common.PaneBlockMixin".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("12w17a") >= 0;
		}
		if ("net.ornithemc.osl.items.impl.mixin.client.SlabBlockMixin".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("12w50a") >= 0;
		}
		if ("net.ornithemc.osl.items.impl.mixin.common.QuartzBlockMixin".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("13w02a") >= 0;
		}
		if ("net.ornithemc.osl.items.impl.mixin.common.LootEntryMixinNew".equals(mixinClassName)) {
			return LOOT_ENTRY_USES_ITEM_STACK;
		}
		if ("net.ornithemc.osl.items.impl.mixin.common.LootEntryMixinOld".equals(mixinClassName)) {
			return LOOT_ENTRIES_EXIST && !LOOT_ENTRY_USES_ITEM_STACK;
		}
		if ("net.ornithemc.osl.items.impl.mixin.common.BlockAccess".equals(mixinClassName)
			|| "net.ornithemc.osl.items.impl.mixin.common.BlockItemMixinOld".equals(mixinClassName)
			|| "net.ornithemc.osl.items.impl.mixin.common.ItemAccess".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("13w02a") < 0;
		}
		if ("net.ornithemc.osl.items.impl.mixin.common.BlockItemMixinNew".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("13w02a") >= 0;
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
