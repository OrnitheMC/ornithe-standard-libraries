package net.ornithemc.osl.resource.loader.impl.mixin.common;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.world.WorldData;
import net.minecraft.world.WorldSettings;

import net.ornithemc.feather.constants.NbtTypes;

import net.ornithemc.osl.resource.loader.api.world.WorldDataExtension;

@Mixin(WorldData.class)
public class WorldDataMixin implements WorldDataExtension {

	private static final String DATA_PACKS = "DataPacks";
	private static final String DATA_PACKS_DISABLED = "Disabled";
	private static final String DATA_PACKS_ENABLED = "Enabled";

	@Unique
	private Set<String> disabledDataPacks;
	@Unique
	private Set<String> enabledDataPacks;

	@Inject(
		method = "<init>()V",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$resource_loader$initDataPacks(CallbackInfo ci) {
		this.disabledDataPacks = new HashSet<>();
		this.enabledDataPacks = new LinkedHashSet<>();
	}

	@Inject(
		method = "<init>(Lnet/minecraft/world/WorldSettings;Ljava/lang/String;)V",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$resource_loader$initDataPacks(WorldSettings settings, String name, CallbackInfo ci) {
		this.disabledDataPacks = new HashSet<>();
		this.enabledDataPacks = new LinkedHashSet<>();
	}

	@Inject(
		method = "<init>(Lnet/minecraft/nbt/NbtCompound;)V",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$resource_loader$readDataPacks(NbtCompound nbt, CallbackInfo ci) {
		this.disabledDataPacks = new HashSet<>();
		this.enabledDataPacks = new LinkedHashSet<>();

		if (nbt.contains(DATA_PACKS, NbtTypes.COMPOUND)) {
			NbtCompound dataPacksNbt = nbt.getCompound(DATA_PACKS);
			NbtList disabledNbt = dataPacksNbt.getList(DATA_PACKS_DISABLED, NbtTypes.STRING);
			NbtList enabledNbt = dataPacksNbt.getList(DATA_PACKS_ENABLED, NbtTypes.STRING);

			for (int i = 0; i < disabledNbt.size(); i++) {
				this.disabledDataPacks.add(disabledNbt.getString(i));
			}
			for (int i = 0; i < enabledNbt.size(); i++) {
				this.enabledDataPacks.add(enabledNbt.getString(i));
			}
		}
	}

	@Inject(
		method = "<init>(Lnet/minecraft/world/WorldData;)V",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$resource_loader$copyDataPacks(WorldData data, CallbackInfo ci) {
		this.disabledDataPacks = data.getDisabledDataPacks();
		this.enabledDataPacks = data.getEnabledDataPacks();
	}

	@Inject(
		method = "writeNbt",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$resource_loader$writeDataPacks(NbtCompound nbt, NbtCompound playerNbt, CallbackInfo ci) {
		NbtCompound dataPacksNbt = new NbtCompound();
		NbtList disabledNbt = new NbtList();
		NbtList enabledNbt = new NbtList();

		for (String id : this.enabledDataPacks) {
			enabledNbt.addElement(new NbtString(id));
		}
		for (String id : this.disabledDataPacks) {
			disabledNbt.addElement(new NbtString(id));
		}

		dataPacksNbt.put(DATA_PACKS_ENABLED, enabledNbt);
		dataPacksNbt.put(DATA_PACKS_DISABLED, disabledNbt);
		nbt.put(DATA_PACKS, dataPacksNbt);
	}

	@Override
	public Set<String> getDisabledDataPacks() {
		return this.disabledDataPacks;
	}

	@Override
	public Set<String> getEnabledDataPacks() {
		return this.enabledDataPacks;
	}
}
