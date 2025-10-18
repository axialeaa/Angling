package com.eightsidedsquare.angling.client.model;

import com.eightsidedsquare.angling.common.entity.AnemoneBlockEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.model.DefaultedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class AnemoneBlockEntityModel extends GeoModel<AnemoneBlockEntity> {

    @Override
    public Identifier getModelResource(GeoRenderState geoRenderState) {
        return Identifier.of(MOD_ID, "geo/anemone.geo.json");
    }

    @Override
    public Identifier getTextureResource(GeoRenderState geoRenderState) {
        return Identifier.of(MOD_ID, "textures/entity/anemone/anemone.png");
    }

    @Override
    public Identifier getAnimationResource(AnemoneBlockEntity animatable) {
        return Identifier.of(MOD_ID, "animations/anemone.animation.json");
    }

    @Override
    public @Nullable Animation getAnimation(AnemoneBlockEntity animatable, String name) throws RuntimeException {
        return super.getAnimation(animatable, name);
    }
}
