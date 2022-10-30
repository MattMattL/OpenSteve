package com.opensteve.client.models;

import com.opensteve.OpenSteve;
import com.opensteve.entity.BaseAiEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BaseAiModel extends HumanoidModel<BaseAiEntity>
{
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(OpenSteve.MODID, "base_ai"), "main");

    public BaseAiModel(ModelPart modelPart)
    {
        super(modelPart);
    }


    public static LayerDefinition createBodyLayer()
    {
//        return createBodyLayer(new CubeDeformation(0.0F));

        MeshDefinition mesh = HumanoidModel.createMesh(new CubeDeformation(0f), 0f);

        return LayerDefinition.create(mesh, 0, 0);
    }
}