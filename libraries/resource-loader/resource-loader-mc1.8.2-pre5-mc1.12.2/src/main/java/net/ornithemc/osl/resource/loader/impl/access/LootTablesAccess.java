package net.ornithemc.osl.resource.loader.impl.access;

import com.google.gson.Gson;

import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;

public interface LootTablesAccess {

	Gson osl$resource_loader$getGson();

	ResourceManager osl$resource_loader$getResourceManager();

}
