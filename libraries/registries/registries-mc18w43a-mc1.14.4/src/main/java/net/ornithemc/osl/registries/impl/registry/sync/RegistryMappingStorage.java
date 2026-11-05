package net.ornithemc.osl.registries.impl.registry.sync;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;

import net.ornithemc.osl.registries.impl.Constants;
import net.ornithemc.osl.registries.impl.registry.RegistriesImpl;
import net.ornithemc.osl.registries.impl.registry.SyncedRegistriesImpl;

public final class RegistryMappingStorage {

	public static void loadRegistryMappings(File dir) throws IOException {
		File file = new File(dir, Constants.REGISTRY_MAPPINGS_FILE_NAME);

		if (!readRegistryMappings(dir, file, false)) {
			file = new File(dir, Constants.REGISTRY_MAPPINGS_FILE_NAME + "_old");

			if (!readRegistryMappings(dir, file, true)) {
				RegistriesImpl.LOGGER.debug("no registry mappings read for '" + dir.getName() + "'");
			}
		}
	}

	private static boolean readRegistryMappings(File dir, File file, boolean throwOnException) throws IOException {
		if (file.exists()) {
			try (InputStream is = new FileInputStream(file)) {
				NbtCompound nbt = NbtIo.readCompressed(is);

				SyncedRegistriesNbtSerializer.deserialize(nbt, RegistryMappingSource.WORLD_SAVE);
				SyncedRegistriesImpl.applyMappings();

				return true;
			} catch (IOException e) {
				if (throwOnException) {
					throw new IOException("Unable to read registry mappings for '" + dir.getName() + "'", e);
				} else {
					RegistriesImpl.LOGGER.warn("error while reading registry mappings for '" + dir.getName() + "'", e);
				}
			} catch (RegistryMappingException e) {
				if (throwOnException) {
					throw new IOException("Invalid registry mappings for '" + dir.getName() + "'", e);
				} else {
					RegistriesImpl.LOGGER.warn("invalid registry mappings for '" + dir.getName() + "'", e);
				}
			}
		}

		return false;
	}

	public static void saveRegistryMappings(File dir) throws IOException {
		File file = new File(dir, Constants.REGISTRY_MAPPINGS_FILE_NAME);
		File tmp = new File(dir, Constants.REGISTRY_MAPPINGS_FILE_NAME + "_tmp");
		File backup = new File(dir, Constants.REGISTRY_MAPPINGS_FILE_NAME + "_old");

		writeRegistryMappings(dir, file, tmp, backup);
	}

	private static void writeRegistryMappings(File dir, File file, File newFile, File oldFile) throws IOException {
		NbtCompound nbt = new NbtCompound();

		try {
			SyncedRegistriesNbtSerializer.serialize(nbt);

			try (OutputStream os = new FileOutputStream(newFile)) {
				NbtIo.writeCompressed(nbt, os);
			}

			if (oldFile.exists()) {
				oldFile.delete();
			}

			file.renameTo(oldFile);
			if (file.exists()) {
				file.delete();
			}

			newFile.renameTo(file);
			if (newFile.exists()) {
				newFile.delete();
			}
		} catch (IOException e) {
			RegistriesImpl.LOGGER.warn("error while writing registry mappings for '" + dir.getName() + "'", e);
		}
	}
}
