package com.eightsidedsquare.angling.cca;

import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistryV3;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class AnglingEntityComponents implements EntityComponentInitializer {

    public static final ComponentKey<FishSpawningComponent> FISH_SPAWNING =
            ComponentRegistryV3.INSTANCE.getOrCreate(Identifier.of(MOD_ID, "fish_spawning"), FishSpawningComponent.class);

    @Override
    public void registerEntityComponentFactories(@NotNull EntityComponentFactoryRegistry registry) {
        registry.registerFor(FishEntity.class, FISH_SPAWNING, FishSpawningComponent::new);
    }
}
