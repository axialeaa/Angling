package com.eightsidedsquare.angling.core.tags;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class AnglingBlockTags {

    public static final TagKey<Block> FILTER_FEEDERS = create("filter_feeders");
    public static final TagKey<Block> STARFISH_FOOD = create("starfish_food");
    public static final TagKey<Block> CRAB_SPAWNABLE_ON = create("crab_spawnable_on");

    private static TagKey<Block> create(String id) {
        return TagKey.of(RegistryKeys.BLOCK, Identifier.of(MOD_ID, id));
    }

}
