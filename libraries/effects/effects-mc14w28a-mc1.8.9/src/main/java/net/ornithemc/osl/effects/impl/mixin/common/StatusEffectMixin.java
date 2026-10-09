package net.ornithemc.osl.effects.impl.mixin.common;

import java.util.Map;

import org.objectweb.asm.Opcodes;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.entity.living.effect.StatusEffect;
import net.minecraft.resource.Identifier;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.core.impl.util.Util;
import net.ornithemc.osl.effects.api.effect.StatusEffectExtension;
import net.ornithemc.osl.effects.impl.StatusEffectRegistryImpl;
import net.ornithemc.osl.effects.impl.VanillaStatusEffects;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.api.registry.sync.ArrayMapper;
import net.ornithemc.osl.registries.api.registry.sync.DynamicArray;

@Mixin(StatusEffect.class)
public class StatusEffectMixin implements StatusEffectExtension {

	@Shadow @Final @Mutable
	private static StatusEffect[] BY_ID;

	@Shadow
	private String key;

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "HEAD"
		)
	)
	private static void osl$effects$unlockStatusEffectRegistry(CallbackInfo ci) {
		StatusEffectRegistryImpl.unlock();
	}

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "TAIL"
		)
	)
	private static void osl$effects$registerStatusEffectsAndArrayMappers(CallbackInfo ci) {
		StatusEffectRegistryImpl.registerStatusEffects();
		StatusEffectRegistryImpl.registerUnknownStatusEffects();

		SyncedRegistries.registerMapper(RegistryKeys.STATUS_EFFECT, NamespacedIdentifiers.from("mob_effect/by_id"), ArrayMapper.of(() -> BY_ID, a -> BY_ID = a));
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
	private int osl$effects$handleAutoAssignId(int id) {
		if (id == AUTO_ASSIGN_ID) {
			// the StatusEffect[] array must contain all effects so this should
			// give us a valid ID for the StatusEffect registry to use.
			id = DynamicArray.length(BY_ID);

			// keep 0-255 free for all Vanilla effects
			if (id <= VanillaStatusEffects.MAX_ID) {
				id = VanillaStatusEffects.MAX_ID + 1;
			}
		}

		return id;
	}

	@Inject(
		method = "<init>",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/entity/living/effect/StatusEffect;BY_ID:[Lnet/minecraft/entity/living/effect/StatusEffect;",
			opcode = Opcodes.GETSTATIC,
			args = "array=set"
		)
	)
	private void osl$effects$growArrays(CallbackInfo ci, @Local(ordinal = 0) int id) {
		int capacity = id + 1;

		BY_ID = DynamicArray.grow(BY_ID, capacity);
	}

	@WrapOperation(
		method = "<init>",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"
		)
	)
	private Object osl$effects$cancelPlaceholderNamespacedId(Map<Identifier, StatusEffect> REGISTRY, Object key, Object effect, Operation<Object> op) {
		if (key == PLACEHOLDER_NAMESPACED_ID) {
			return null;
		} else {
			return op.call(REGISTRY, key, effect);
		}
	}

	@Inject(
		method = "getTranslationKey",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$effects$autoAssignTranslationKey(CallbackInfoReturnable<String> cir) {
		if (this.key == null) {
			NamespacedIdentifier identifier = StatusEffectRegistryImpl.getIdentifier((StatusEffect) (Object) this);

			if (identifier == null) {
				this.key = "potion.unknown";
			} else {
				this.key = Util.makeTranslationKey("potion", identifier);
			}
		}
	}
}
