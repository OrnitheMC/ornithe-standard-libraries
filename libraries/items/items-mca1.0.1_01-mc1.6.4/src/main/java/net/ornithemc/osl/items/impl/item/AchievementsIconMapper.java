package net.ornithemc.osl.items.impl.item;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

import net.minecraft.stat.achievement.AchievementStat;

import net.ornithemc.osl.registries.api.registry.sync.IdMapper;
import net.ornithemc.osl.registries.api.registry.sync.RegistryMappings;

public class AchievementsIconMapper implements IdMapper {

	public static AchievementsIconMapper of(List<AchievementStat> achievements) {
		return new AchievementsIconMapper(achievements);
	}

	private final List<AchievementStat> achievements;
	private final Set<AchievementStat> missing;

	private boolean applied;

	private AchievementsIconMapper(List<AchievementStat> achievements) {
		this.achievements = achievements;
		this.missing = Collections.newSetFromMap(new IdentityHashMap<>());
	}

	@Override
	public void apply(RegistryMappings mappings) {
		this.missing.clear();
		this.fixAchievementIcons(mappings::remap, true);

		this.applied = true;
	}

	@Override
	public void undo(RegistryMappings mappings) {
		if (this.applied) {
			this.fixAchievementIcons(mappings::unmap, false);
			this.missing.clear();
		}

		this.applied = false;
	}

	private void fixAchievementIcons(Int2IntFunction mapper, boolean storeMissing) {
		for (AchievementStat achievement : this.achievements) {
			if (this.missing.contains(achievement)) {
				continue;
			}

			int oldId = achievement.icon.id;
			int newId = mapper.applyAsInt(oldId);

			if (newId >= 0) {
				achievement.icon.id = newId;
			} else if (storeMissing) {
				this.missing.add(achievement);
			}
		}
	}
}
