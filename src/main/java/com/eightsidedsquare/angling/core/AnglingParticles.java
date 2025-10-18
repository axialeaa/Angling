package com.eightsidedsquare.angling.core;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class AnglingParticles {

    public static SimpleParticleType ALGAE = Registry.register(Registries.PARTICLE_TYPE, Identifier.of(MOD_ID, "algae"), FabricParticleTypes.simple(true));
    public static SimpleParticleType WORM = Registry.register(Registries.PARTICLE_TYPE, Identifier.of(MOD_ID, "worm"), FabricParticleTypes.simple(true));

    public static void init() {

    }

}
