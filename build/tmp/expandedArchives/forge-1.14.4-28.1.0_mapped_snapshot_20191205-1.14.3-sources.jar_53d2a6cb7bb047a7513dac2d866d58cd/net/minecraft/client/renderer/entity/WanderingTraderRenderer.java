package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.VillagerHeldItemLayer;
import net.minecraft.client.renderer.entity.model.VillagerModel;
import net.minecraft.entity.merchant.villager.WanderingTraderEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WanderingTraderRenderer extends MobRenderer<WanderingTraderEntity, VillagerModel<WanderingTraderEntity>> {
   private static final ResourceLocation field_217780_a = new ResourceLocation("textures/entity/wandering_trader.png");

   public WanderingTraderRenderer(EntityRendererManager renderManagerIn) {
      super(renderManagerIn, new VillagerModel<>(0.0F), 0.5F);
      this.addLayer(new HeadLayer<>(this));
      this.addLayer(new VillagerHeldItemLayer<>(this));
   }

   protected ResourceLocation getEntityTexture(WanderingTraderEntity entity) {
      return field_217780_a;
   }

   protected void preRenderCallback(WanderingTraderEntity entitylivingbaseIn, float partialTickTime) {
      float f = 0.9375F;
      GlStateManager.scalef(0.9375F, 0.9375F, 0.9375F);
   }
}