package net.ornithemc.osl.resource.loader.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.pack.ResourcePacks;
import net.minecraft.resource.pack.BuiltInPack;
import net.minecraft.resource.pack.metadata.PackMetadataSection;

import net.ornithemc.osl.resource.loader.api.ModPack;

public class ResourceLoader {

	private static final List<ModPack> DEFAULT_MOD_PACKS = new ArrayList<>();

	private static int packFormat = -1;

	public static boolean addDefaultModPack(ModPack pack) {
		return DEFAULT_MOD_PACKS.add(pack);
	}

	public static List<ModPack> getDefaultModPacks() {
		return Collections.unmodifiableList(DEFAULT_MOD_PACKS);
	}

	public static int getPackFormat() {
		if (packFormat < 0) {
			try {
				Minecraft minecraft = Minecraft.getInstance();
				ResourcePacks resourcePacks = minecraft.getResourcePacks();

				BuiltInPack defaultPack = resourcePacks.m_8018497();
				PackMetadataSection metadata = defaultPack.getMetadataSection(PackMetadataSection.SERIALIZER);

				packFormat = metadata.getFormat();
			} catch (IOException e) {
				packFormat = 0;
				System.out.println("unable to parse resource pack format from default resource pack; using default value of " + packFormat);
			}
		}

		return packFormat;
	}
}
