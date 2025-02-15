package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HuskRenderer extends ZombieRenderer {
   private static final ResourceLocation HUSK_ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/husk.png");

   public HuskRenderer(EntityRendererManager renderManagerIn) {
      super(renderManagerIn);
   }

   protected void preRenderCallback(ZombieEntity entitylivingbaseIn, float partialTickTime) {
      float f = 1.0625F;
      GlStateManager.scalef(1.0625F, 1.0625F, 1.0625F);
      super.preRenderCallback(entitylivingbaseIn, partialTickTime);
   }

   protected ResourceLocation getEntityTexture(ZombieEntity entity) {
      return HUSK_ZOMBIE_TEXTURES;
   }
}