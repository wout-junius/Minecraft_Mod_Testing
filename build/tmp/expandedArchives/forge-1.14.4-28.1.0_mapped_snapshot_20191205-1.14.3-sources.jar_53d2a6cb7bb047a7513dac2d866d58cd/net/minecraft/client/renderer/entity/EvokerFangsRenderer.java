package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.model.EvokerFangsModel;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EvokerFangsRenderer extends EntityRenderer<EvokerFangsEntity> {
   private static final ResourceLocation EVOKER_ILLAGER_FANGS = new ResourceLocation("textures/entity/illager/evoker_fangs.png");
   private final EvokerFangsModel<EvokerFangsEntity> model = new EvokerFangsModel<>();

   public EvokerFangsRenderer(EntityRendererManager renderManagerIn) {
      super(renderManagerIn);
   }

   public void doRender(EvokerFangsEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
      float f = entity.getAnimationProgress(partialTicks);
      if (f != 0.0F) {
         float f1 = 2.0F;
         if (f > 0.9F) {
            f1 = (float)((double)f1 * ((1.0D - (double)f) / (double)0.1F));
         }

         GlStateManager.pushMatrix();
         GlStateManager.disableCull();
         GlStateManager.enableAlphaTest();
         this.bindEntityTexture(entity);
         GlStateManager.translatef((float)x, (float)y, (float)z);
         GlStateManager.rotatef(90.0F - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
         GlStateManager.scalef(-f1, -f1, f1);
         float f2 = 0.03125F;
         GlStateManager.translatef(0.0F, -0.626F, 0.0F);
         this.model.render(entity, f, 0.0F, 0.0F, entity.rotationYaw, entity.rotationPitch, 0.03125F);
         GlStateManager.popMatrix();
         GlStateManager.enableCull();
         super.doRender(entity, x, y, z, entityYaw, partialTicks);
      }
   }

   protected ResourceLocation getEntityTexture(EvokerFangsEntity entity) {
      return EVOKER_ILLAGER_FANGS;
   }
}