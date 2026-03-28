package net.ornithemc.osl.resource.loader.api.resource.pack;

import java.util.List;

import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackSummary;

/**
 * Represents a resource pack's preferred ordering position.
 */
public enum PackPosition {

	TOP, BOTTOM;

	public PackPosition opposite() {
		return this == TOP ? BOTTOM : TOP;
	}

	public void insert(List<ResourcePackSummary> resourcePacks, ResourcePackSummary resourcePack, boolean reverse) {
		PackPosition pos = reverse ? this.opposite() : this;

		if (pos == BOTTOM) {
			int index;

			for (index = 0; index < resourcePacks.size(); index++) {
				ResourcePackSummary pack = resourcePacks.get(index);

				if (!pack.isFixedPosition() || pack.getDefaultPosition() != this) {
					break;
				}
			}

			resourcePacks.add(index, resourcePack);
		} else {
			int index;

			for (index = resourcePacks.size() - 1; index >= 0; index--) {
				ResourcePackSummary pack = resourcePacks.get(index);

				if (!pack.isFixedPosition() || pack.getDefaultPosition() != this) {
					break;
				}
			}

			resourcePacks.add(index + 1, resourcePack);
		}
	}
}
