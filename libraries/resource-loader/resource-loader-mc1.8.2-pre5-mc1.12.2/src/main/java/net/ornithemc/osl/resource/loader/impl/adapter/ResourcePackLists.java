package net.ornithemc.osl.resource.loader.impl.adapter;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ResourcePacksScreen;
import net.minecraft.client.gui.screen.resourcepack.ResourcePackEntry;
import net.minecraft.client.gui.screen.resourcepack.ServerResourcePackEntry;
import net.minecraft.client.resource.pack.ResourcePacks;

import net.ornithemc.osl.resource.loader.api.resource.pack.PackPosition;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackRepository;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackSummary;

public final class ResourcePackLists {

	public static final boolean ADD_SERVER_PACK_TO_GUI;

	static {
		boolean exists;

		try {
			exists = ServerResourcePackEntry.class != null;
		} catch (Throwable t) {
			exists = false;
		}

		ADD_SERVER_PACK_TO_GUI = !exists;
	}

	public static void fixSelection(List<net.minecraft.client.resource.pack.ResourcePack> packs, boolean reversed) {
		fixSelection(
			packs,
			(summary, pack) -> new ResourcePackAdapter(pack),
			reversed
		);
	}

	public static void fixSelection(ResourcePacksScreen screen, List<ResourcePackEntry> entries, boolean reversed) {
		Minecraft minecraft = Minecraft.getInstance();
		ResourcePacks resourcePacks = minecraft.getResourcePacks();

		// add server pack entry manually since vanilla doesn't
		if (ADD_SERVER_PACK_TO_GUI && resourcePacks.getServerPack() != null) {
			String id = resourcePacks.getServerPack().getName();
			ResourcePackSummary summary = ResourcePackRepository.client().getPack(id);

			if (summary != null) {
				entries.add(reversed ? 0 : entries.size(), new ResourcePackSummaryEntry(screen, summary));
			}
		}

		fixSelection(
			entries,
			(summary, pack) -> new ResourcePackSummaryEntry(screen, summary),
			reversed
		);
	}

	/**
	 * insert any missing required packs into the list, preserving their ordering
	 */
	public static <T> void fixSelection(List<T> entries, BiFunction<ResourcePackSummary, ResourcePack, T> entryGetter, boolean reversed) {
		Minecraft minecraft = Minecraft.getInstance();
		ResourcePacks resourcePacks = minecraft.getResourcePacks();
		ResourcePackRepository packRepository = ResourcePackRepository.client();

		Collection<ResourcePackSummary> selection = packRepository.getSelectedPacks();

		boolean serverPackSelected = (resourcePacks.getServerPack() != null);
		boolean defaultPackInserted = false;
		boolean serverPackInserted = false;

		// if you keep inserting at index 0 you invert the ordering
		// so we increment an offset to counteract that effect
		int offset = 0;

		for (ResourcePackSummary summary : selection) {
			ResourcePack pack = summary.open();

			if (summary.isRequired()) {
				if (pack instanceof WrappedResourcePack) {
					net.minecraft.client.resource.pack.ResourcePack resourcePack = ((WrappedResourcePack) pack).pack;

					defaultPackInserted |= (resourcePack == resourcePacks.defaultPack);
					serverPackInserted |= (resourcePack == resourcePacks.getServerPack());
				} else {
					T entry = entryGetter.apply(summary, pack);

					if (summary.getDefaultPosition() == PackPosition.TOP) {
						if (!serverPackSelected || serverPackInserted) {
							entries.add(reversed ? offset++ : entries.size(), entry);
						} else {
							entries.add(reversed ? ++offset : (entries.size() - 1), entry);
						}
					}
					if (summary.getDefaultPosition() == PackPosition.BOTTOM) {
						if (defaultPackInserted) {
							entries.add(reversed ? (entries.size() - 1 ) : ++offset, entry);
						} else {
							entries.add(reversed ? entries.size() : offset++, entry);
						}
					}
				}
			}
		}
	}
}
