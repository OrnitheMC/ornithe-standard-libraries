package net.ornithemc.osl.registries.impl.mixin.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.storage.AlphaWorldStorage;

import net.ornithemc.osl.registries.impl.Constants;
import net.ornithemc.osl.registries.impl.access.WorldStorageAccess;
import net.ornithemc.osl.registries.impl.registry.RegistriesImpl;
import net.ornithemc.osl.registries.impl.registry.SyncedRegistriesImpl;
import net.ornithemc.osl.registries.impl.registry.sync.RegistryMappingException;
import net.ornithemc.osl.registries.impl.registry.sync.RegistryMappingSource;
import net.ornithemc.osl.registries.impl.registry.sync.SyncedRegistriesNbtSerializer;

@Mixin(AlphaWorldStorage.class)
public class AlphaWorldStorageMixin implements WorldStorageAccess {

	@Shadow @Final
	private String name;

	@Shadow
	private File m_52081685() { return null; }

	@Override
	public void osl$registries$loadRegistryMappings() throws IOException {
		File dir = this.m_52081685();
		File file = new File(dir, Constants.REGISTRY_MAPPINGS_FILE_NAME);

		if (!this.readRegistryMappings(file, false)) {
			file = new File(dir, Constants.REGISTRY_MAPPINGS_FILE_NAME + "_old");

			if (!this.readRegistryMappings(file, true)) {
				RegistriesImpl.LOGGER.debug("no registry mappings read for '" + this.name + "'");
			}
		}
	}

	@Unique
	private boolean readRegistryMappings(File file, boolean throwOnException) throws IOException {
		if (file.exists()) {
			try (InputStream is = new FileInputStream(file)) {
				NbtCompound nbt = NbtIo.readCompressed(is);

				SyncedRegistriesNbtSerializer.deserialize(nbt, RegistryMappingSource.WORLD_SAVE);
				SyncedRegistriesImpl.applyMappings();

				return true;
			} catch (IOException e) {
				if (throwOnException) {
					throw new IOException("Unable to read registry mappings for '" + this.name + "'", e);
				} else {
					RegistriesImpl.LOGGER.warn("error while reading registry mappings for '" + this.name + "'", e);
				}
			} catch (RegistryMappingException e) {
				if (throwOnException) {
					throw new IOException("Invalid registry mappings for '" + this.name + "'", e);
				} else {
					RegistriesImpl.LOGGER.warn("invalid registry mappings for '" + this.name + "'", e);
				}
			}
		}

		return false;
	}

	@Override
	public void osl$registries$saveRegistryMappings() throws IOException {
		File dir = this.m_52081685();
		File file = new File(dir, Constants.REGISTRY_MAPPINGS_FILE_NAME);
		File tmp = new File(dir, Constants.REGISTRY_MAPPINGS_FILE_NAME + "_tmp");
		File backup = new File(dir, Constants.REGISTRY_MAPPINGS_FILE_NAME + "_old");

		this.writeRegistryMappings(file, tmp, backup);
	}

	@Unique
	private void writeRegistryMappings(File file, File newFile, File oldFile) throws IOException {
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
			RegistriesImpl.LOGGER.warn("error while writing registry mappings for '" + this.name + "'", e);
		}
	}
}
