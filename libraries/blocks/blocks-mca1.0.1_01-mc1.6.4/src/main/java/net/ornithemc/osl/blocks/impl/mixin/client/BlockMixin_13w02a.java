package net.ornithemc.osl.blocks.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;

import net.ornithemc.osl.blocks.api.BlockRegistry;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.impl.util.Util;

@Mixin(Block.class)
public class BlockMixin_13w02a {

	@Shadow
	private String key;

	@Inject(
		method = "registerSprites",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$blocks$autoAssignTranslationKey(CallbackInfo ci) {
		if (this.key == null) {
			NamespacedIdentifier identifier = BlockRegistry.getIdentifier((Block) (Object) this);

			if (identifier == null) {
				this.key = "unknown";
			} else {
				this.key = Util.makeTranslationKey(identifier);
			}
		}
	}
}
