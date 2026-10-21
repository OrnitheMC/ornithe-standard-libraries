package net.ornithemc.osl.resource.loader.impl;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.pack.TexturePack;

public class DirectoryTexturePack extends TexturePack {

	public final File file;

	private int iconId = -1;
	private BufferedImage icon;

	public DirectoryTexturePack(File file) {
		this.name = file.getName();
		this.file = file;
	}

	private String trimDescriptionLine(String line) {
		if (line != null && line.length() > 34) {
			line = line.substring(0, 34);
		}

		return line;
	}

	@Override
	public void load(Minecraft minecraft) {
		try (InputStream is = this.getResource("/pack.txt")) {
			try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
				this.descriptionLine1 = this.trimDescriptionLine(br.readLine());
				this.descriptionLine2 = this.trimDescriptionLine(br.readLine());
			} catch (Exception e) {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try (InputStream is = this.getResource("/pack.png")) {
			this.icon = ImageIO.read(is);
		} catch (Exception e) {
		}
	}

	@Override
	public void unload(Minecraft minecraft) {
		if (this.icon != null) {
			minecraft.textureManager.release(this.iconId);
		}

		this.close();
	}

	@Override
	public void bindIcon(Minecraft minecraft) {
		if (this.icon != null && this.iconId < 0) {
			this.iconId = minecraft.textureManager.load(this.icon);
		}

		if (this.icon != null) {
			minecraft.textureManager.bind(this.iconId);
		} else {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft.textureManager.load("/gui/unknown_pack.png"));
		}
	}

	@Override
	public void open() {
	}

	@Override
	public void close() {
	}

	@Override
	public InputStream getResource(String path) throws IOException {
		try {
			File file = new File(this.file, path.substring(1));

			if (file.exists()) {
				return new BufferedInputStream(new FileInputStream(file));
			}
		} catch (Exception e) {
		}

		return null;
	}
}
