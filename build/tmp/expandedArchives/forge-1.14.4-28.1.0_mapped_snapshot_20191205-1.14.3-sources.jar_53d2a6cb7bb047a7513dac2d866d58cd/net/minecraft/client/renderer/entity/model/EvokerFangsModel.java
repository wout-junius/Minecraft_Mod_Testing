package net.minecraft.client.renderer.entity.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EvokerFangsModel<T extends Entity> extends EntityModel<T> {
   private final RendererModel base = new RendererModel(this, 0, 0);
   private final RendererModel upperJaw;
   private final RendererModel lowerJaw;

   public EvokerFangsModel() {
      this.base.setRotationPoint(-5.0F, 22.0F, -5.0F);
      this.base.addBox(0.0F, 0.0F, 0.0F, 10, 12, 10);
      this.upperJaw = new RendererModel(this, 40, 0);
      this.upperJaw.setRotationPoint(1.5F, 22.0F, -4.0F);
      this.upperJaw.addBox(0.0F, 0.0F, 0.0F, 4, 14, 8);
      this.lowerJaw = new RendererModel(this, 40, 0);
      this.lowerJaw.setRotationPoint(-1.5F, 22.0F, 4.0F);
      this.lowerJaw.addBox(0.0F, 0.0F, 0.0F, 4, 14, 8);
   }

   public void render(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
      float f = limbSwing * 2.0F;
      if (f > 1.0F) {
         f = 1.0F;
      }

      f = 1.0F - f * f * f;
      this.upperJaw.rotateAngleZ = (float)Math.PI - f * 0.35F * (float)Math.PI;
      this.lowerJaw.rotateAngleZ = (float)Math.PI + f * 0.35F * (float)Math.PI;
      this.lowerJaw.rotateAngleY = (float)Math.PI;
      float f1 = (limbSwing + MathHelper.sin(limbSwing * 2.7F)) * 0.6F * 12.0F;
      this.upperJaw.rotationPointY = 24.0F - f1;
      this.lowerJaw.rotationPointY = this.upperJaw.rotationPointY;
      this.base.rotationPointY = this.upperJaw.rotationPointY;
      this.base.render(scale);
      this.upperJaw.render(scale);
      this.lowerJaw.render(scale);
   }
}