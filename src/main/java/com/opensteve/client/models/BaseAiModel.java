package com.opensteve.client.models;

import com.opensteve.OpenSteve;
import com.opensteve.entity.BaseAiEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BaseAiModel extends PigModel<BaseAiEntity>
{
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(OpenSteve.MODID, "base_ai"), "main");

    public BaseAiModel(ModelPart modelPart)
    {
      super(modelPart);
    }


    public static LayerDefinition createBodyLayer()
    {
        return createBodyLayer(new CubeDeformation(0.0F));

//        MeshDefinition meshDefinition = createBodyMesh(CubeDeformation.NONE);
//        PartDefinition partDefinition = meshDefinition.getRoot();
//
//        partDefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 13).addBox(-3.0F, -11.0F, -2.0F, 6.0F, 5.0F, 7.0F, CubeDeformation.NONE), PartPose.ZERO);
//
//        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    public static MeshDefinition createBodyMesh(CubeDeformation p_170670_)
    {
       MeshDefinition meshdefinition = new MeshDefinition();
       PartDefinition partdefinition = meshdefinition.getRoot();

       return meshdefinition;
    }
}