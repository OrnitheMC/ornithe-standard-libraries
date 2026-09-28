package net.ornithemc.osl.resource.loader.api.world;

import java.util.Set;

public interface WorldDataExtension {

	Set<String> getDisabledDataPacks();

	Set<String> getEnabledDataPacks();

}
