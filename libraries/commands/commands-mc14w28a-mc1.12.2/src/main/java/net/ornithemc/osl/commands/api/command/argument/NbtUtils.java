package net.ornithemc.osl.commands.api.command.argument;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

public final class NbtUtils {

	public static boolean matches(NbtElement expected, NbtElement actual, boolean partialListMatches) {
		if (expected == actual || expected == null || actual == null) {
			return true;
		}
		if (!expected.getClass().equals(actual.getClass())) {
			return false;
		}

		if (expected instanceof NbtCompound) {
			NbtCompound nbt1 = (NbtCompound) expected;
			NbtCompound nbt2 = (NbtCompound) actual;

			for (String key : nbt1.getKeys()) {
				NbtElement child1 = nbt1.get(key);
				NbtElement child2 = nbt2.get(key);

				if (!matches(child1, child2, partialListMatches)) {
					return false;
				}
			}

			return true;
		} else if (expected instanceof NbtList && partialListMatches) {
			NbtList expectedList = (NbtList) expected;
			NbtList actualList = (NbtList) actual;

			if (expectedList.size() == 0) {
				return actualList.size() == 0;
			}
			if (expectedList.size() > actualList.size()) {
				return false;
			}

			for (int i = 0; i < expectedList.size(); i++) {
				NbtElement expectedChild = expectedList.getElement(i);
				boolean found = false;

				for (int j = 0; j < actualList.size(); j++) {
					NbtElement actualChild = actualList.getElement(j);

					if (matches(expectedChild, actualChild, partialListMatches)) {
						found = true;
						break;
					}
				}

				if (!found) {
					return false;
				}
			}

			return true;
		} else {
			return expected.equals(actual);
		}
	}
}
