package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.model.ShulkerBulletModel;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ShulkerBulletRenderer extends EntityRenderer<ShulkerBulletEntity> {
   private static final ResourceLocation SHULKER_SPARK_TEXTURE = new ResourceLocation("textures/entity/shulker/spark.png");
   private final ShulkerBulletModel<ShulkerBulletEntity> model = new ShulkerBulletModel<>();

   public ShulkerBulletRenderer(EntityRendererManager manager) {
      super(manager);
   }

   private float rotLerp(float p_188347_1_, float p_188347_2_, float p_188347_3_) {
      float f;
      for(f = p_188347_2_ - p_188347_1_; f < -180.0F; f += 360.0F) {
         ;
      }

      while(f >= 180.0F) {
         f -= 360.0F;
      }

      return p_188347_1_ + p_188347_3_ * f;
   }

   public void doRender(ShulkerBulletEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
      GlStateManager.pushMatrix();
      float f = this.rotLerp(entity.prevRotationYaw, entity.rotationYaw, partialTicks);
      float f1 = MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch);
      float f2 = (float)entity.ticksExisted + partialTicks;
      GlStateManager.translatef((float)x, (float)y + 0.15F, (float)z);
      GlStateManager.rotatef(MathHelper.sin(f2 * 0.1F) * 180.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotatef(MathHelper.cos(f2 * 0.1F) * 180.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotatef(MathHelper.sin(f2 * 0.15F) * 360.0F, 0.0F, 0.0F, 1.0F);
      float f3 = 0.03125F;
      GlStateManager.enableRescaleNormal();
      GlStateManager.scalef(-1.0F, -1.0F, 1.0F);
      this.bindEntityTexture(entity);
      this.model.render(entity, 0.0F, 0.0F, 0.0F, f, f1, 0.03125F);
      GlStateManager.enableBlend();
      GlStateManager.color4f(1.0F, 1.0F, 1.0F, 0.5F);
      GlStateManager.scalef(1.5F, 1.5F, 1.5F);
      this.model.render(entity, 0.0F, 0.0F, 0.0F, f, f1, 0.03125F);
      GlStateManager.disableBlend();
      GlStateManager.popMatrix();
      super.doRender(entity, x, y, z, entityYaw, partialTicks);
   }

   protected ResourceLocation getEntityTexture(ShulkerBulletEntity entity) {
      return SHULKER_SPARK_TEXTURE;
   }
}