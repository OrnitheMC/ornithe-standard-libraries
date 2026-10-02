package net.ornithemc.osl.blockentities.impl.mixin.common;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.util.datafix.fixes.BlockEntityIdFix;

import net.ornithemc.osl.blockentities.api.BlockEntityTypeRegistry;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.api.registry.LegacyStringIds;

@Mixin(BlockEntityIdFix.class)
public class BlockEntityIdFixMixin {

	@Shadow
	private static Map<String, String> IDS;

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "TAIL"
		)
	)
	private static void osl$blockentities$addBlockEntityIdFixes(CallbackInfo ci) {
		Set<String> alreadyFixedIds = new HashSet<>(IDS.values());

		for (NamespacedIdentifier identifier : BlockEntityTypeRegistry.REGISTRY.identifierSet()) {
			String id = identifier.toString();

			if (!alreadyFixedIds.contains(id)) {
				IDS.put(LegacyStringIds.fromIdentifier(identifier), id);
			}
		}
	}
}
