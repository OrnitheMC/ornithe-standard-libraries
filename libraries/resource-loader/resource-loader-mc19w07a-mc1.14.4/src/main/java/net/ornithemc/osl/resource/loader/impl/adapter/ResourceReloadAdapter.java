package net.ornithemc.osl.resource.loader.impl.adapter;

import java.util.concurrent.CompletableFuture;

import net.minecraft.unmapped.C_17695012;
import net.minecraft.unmapped.C_75765617;

import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReload;

class ResourceReloadAdapter implements C_17695012 {

	private final ResourceReload resourceReload;

	ResourceReloadAdapter(ResourceReload resourceReload) {
		this.resourceReload = resourceReload;
	}

	@Override
	public boolean m_16882347() {
		return this.resourceReload.isApplying();
	}

	@Override
	public void m_29451477() {
		this.resourceReload.checkExceptions();
	}

	@Override
	public CompletableFuture<C_75765617> m_58614058() {
		return this.resourceReload.result().thenApply(u -> C_75765617.INSTANCE);
	}

	@Override
	public boolean m_77649850() {
		return this.resourceReload.isDone();
	}

	@Override
	public float m_98266169() {
		return this.resourceReload.getProgress();
	}
}
