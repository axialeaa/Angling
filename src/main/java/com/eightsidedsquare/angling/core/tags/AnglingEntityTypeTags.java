package com.eightsidedsquare.angling.core.tags;

import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class AnglingEntityTypeTags {

    public static final TagKey<EntityType<?>> SPAWNING_FISH = create("spawning_fish");
    public static final TagKey<EntityType<?>> COMMON_ENTITIES_IN_PELICAN_BEAK = create("common_entities_in_pelican_beak");
    public static final TagKey<EntityType<?>> UNCOMMON_ENTITIES_IN_PELICAN_BEAK = create("uncommon_entities_in_pelican_beak");
    public static final TagKey<EntityType<?>> HUNTED_BY_PELICAN = create("hunted_by_pelican");
    public static final TagKey<EntityType<?>> HUNTED_BY_PELICAN_WHEN_BABY = create("hunted_by_pelican_when_baby");

    private static TagKey<EntityType<?>> create(String id) {
        return TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(MOD_ID, id));
    }
}
