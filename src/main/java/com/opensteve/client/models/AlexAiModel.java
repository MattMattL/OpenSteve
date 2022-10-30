package com.opensteve.client.models;

import com.opensteve.OpenSteve;
import com.opensteve.entity.EntityAlexAi;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class AlexAiModel extends HumanoidModel<EntityAlexAi>
{
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(OpenSteve.MODID, "alex_ai"), "main");

    public AlexAiModel(ModelPart model)
    {
        super(model);
    }

    public static LayerDefinition createBodyLayer()
    {
        return LayerDefinition.create(createMesh(new CubeDeformation(0f), 0f), 64, 64);
    }

    public static MeshDefinition createMesh(CubeDeformation deform, float p_170683_)
    {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, deform), PartPose.offset(0.0F, 0.0F + p_170683_, 0.0F));
        partdefinition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, deform.extend(0.5F)), PartPose.offset(0.0F, 0.0F + p_170683_, 0.0F));
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, deform), PartPose.offset(0.0F, 0.0F + p_170683_, 0.0F));

        // small arms for alex
        partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, deform), PartPose.offset(-5.0F, 2.0F + p_170683_, 0.0F));
        partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, deform), PartPose.offset(5.0F, 2.0F + p_170683_, 0.0F));

        partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deform), PartPose.offset(-1.9F, 12.0F + p_170683_, 0.0F));
        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deform), PartPose.offset(1.9F, 12.0F + p_170683_, 0.0F));

        return meshdefinition;
   }
}