package net.ornithemc.osl.commands.api.command.argument;

import java.util.function.Predicate;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import net.ornithemc.osl.text.api.TextComponents;

public class ItemInput implements Predicate<ItemStack> {

	private static final Dynamic2CommandExceptionType OVERSTACKED_EXCEPTION = new Dynamic2CommandExceptionType((itemId, maxStackSize) -> TextComponents.translatable("arguments.item.overstacked", itemId, maxStackSize));

	private final Item item;
	private final NbtCompound nbt;

	public ItemInput(Item item, NbtCompound nbt) {
		this.item = item;
		this.nbt = nbt;
	}

	public Item getItem() {
		return this.item;
	}

	@Override
	public boolean test(ItemStack item) {
		return item.getItem() == this.item && NbtUtils.matches(this.nbt, item.getNbt(), true);
	}

	public ItemStack createItem(int size, boolean validateStackSize) throws CommandSyntaxException {
		ItemStack item = new ItemStack(this.item, size);

		if (this.nbt != null) {
			item.setNbt(this.nbt);
		}

		if (validateStackSize && size > item.getMaxSize()) {
			throw OVERSTACKED_EXCEPTION.create(Item.REGISTRY.getKey(this.item), item.getMaxSize());
		}

		return item;
	}

	public String serialize() {
		StringBuilder sb = new StringBuilder(Item.REGISTRY.getId(this.item));

		if (this.nbt != null) {
			sb.append(this.nbt);
		}

		return sb.toString();
	}
}
