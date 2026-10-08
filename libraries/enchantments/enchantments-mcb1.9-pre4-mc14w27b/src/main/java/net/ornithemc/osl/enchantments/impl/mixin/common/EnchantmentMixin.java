package net.ornithemc.osl.enchantments.impl.mixin.common;

import org.objectweb.asm.Opcodes;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.enchantment.Enchantment;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.GameSide;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.core.impl.util.Util;
import net.ornithemc.osl.enchantments.api.enchantment.EnchantmentExtension;
import net.ornithemc.osl.enchantments.impl.EnchantmentRegistryImpl;
import net.ornithemc.osl.enchantments.impl.VanillaEnchantments;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.api.registry.sync.ArrayMapper;
import net.ornithemc.osl.registries.api.registry.sync.DynamicArray;

@Mixin(Enchantment.class)
public class EnchantmentMixin implements EnchantmentExtension {

	@Shadow @Final @Mutable
	private static Enchantment[] BY_ID;

	@Conditional(minecraftVersion = @Version(value = ">=1.4.4", gameSide = GameSide.SERVER))
	@Shadow
	private String key;

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "HEAD"
		)
	)
	private static void osl$enchantments$unlockEnchantmentRegistry(CallbackInfo ci) {
		EnchantmentRegistryImpl.unlock();
	}

	@Conditional(minecraftVersion = @Version("<12w50a"))
	@Inject(
		method = "<clinit>",
		at = @At(
			value = "TAIL"
		)
	)
	private static void osl$enchantments$registerEnchantmentsAndArrayMappers1(CallbackInfo ci) {
		registerEnchantmentsAndArrayMappers();
	}

	@Conditional(minecraftVersion = @Version(">=12w50a"))
	@Inject(
		method = "<clinit>",
		at = @At(
			// target before the for-loop that fills the ALL Enchantment[] array
			value = "FIELD",
			target = "Lnet/minecraft/enchantment/Enchantment;BY_ID:[Lnet/minecraft/enchantment/Enchantment;",
			opcode = Opcodes.GETSTATIC
		)
	)
	private static void osl$enchantments$registerEnchantmentsAndArrayMappers2(CallbackInfo ci) {
		registerEnchantmentsAndArrayMappers();
	}

	@Unique
	private static void registerEnchantmentsAndArrayMappers() {
		EnchantmentRegistryImpl.registerEnchantments();
		EnchantmentRegistryImpl.registerUnknownEnchantments();

		SyncedRegistries.registerMapper(RegistryKeys.ENCHANTMENT, NamespacedIdentifiers.from("enchantment/by_id"), ArrayMapper.of(() -> BY_ID, a -> BY_ID = a));
	}

	@ModifyVariable(
		method = "<init>",
		argsOnly = true,
		ordinal = 0,
		at = @At(
			value = "INVOKE",
			target = "Ljava/lang/Object;<init>()V",
			shift = Shift.AFTER
		)
	)
	private int osl$enchantments$handleAutoAssignId(int id) {
		if (id == AUTO_ASSIGN_ID) {
			// the Enchantment[] array must contain all enchantments so this should
			// give us a valid ID for the Enchantment registry to use.
			id = DynamicArray.length(BY_ID);

			// keep 0-255 free for all Vanilla enchantments
			if (id <= VanillaEnchantments.MAX_ID) {
				id = VanillaEnchantments.MAX_ID + 1;
			}
		}

		return id;
	}

	@Inject(
		method = "<init>",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/enchantment/Enchantment;BY_ID:[Lnet/minecraft/enchantment/Enchantment;",
			opcode = Opcodes.GETSTATIC,
			args = "array=set"
		)
	)
	private void osl$enchantments$growArrays(CallbackInfo ci, @Local(ordinal = 0) int id) {
		int capacity = id + 1;

		BY_ID = DynamicArray.grow(BY_ID, capacity);
	}

	@Conditional(minecraftVersion = @Version(value = ">=1.4.4", gameSide = GameSide.SERVER))
	@Inject(
		method = "getTranslationKey",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$enchantments$autoAssignTranslationKey(CallbackInfoReturnable<String> cir) {
		if (this.key == null) {
			NamespacedIdentifier identifier = EnchantmentRegistryImpl.getIdentifier((Enchantment) (Object) this);

			if (identifier == null) {
				this.key = "unknown";
			} else {
				this.key = Util.makeTranslationKey(identifier);
			}
		}
	}
}
