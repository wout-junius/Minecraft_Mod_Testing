package net.minecraft.world.gen.carver;

import com.mojang.datafixers.Dynamic;
import java.util.BitSet;
import java.util.Random;
import java.util.function.Function;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.feature.ProbabilityConfig;

public class CaveWorldCarver extends WorldCarver<ProbabilityConfig> {
   public CaveWorldCarver(Function<Dynamic<?>, ? extends ProbabilityConfig> p_i49929_1_, int p_i49929_2_) {
      super(p_i49929_1_, p_i49929_2_);
   }

   public boolean shouldCarve(Random rand, int chunkX, int chunkZ, ProbabilityConfig config) {
      return rand.nextFloat() <= config.probability;
   }

   public boolean carve(IChunk chunkIn, Random rand, int seaLevel, int chunkX, int chunkZ, int p_212867_6_, int p_212867_7_, BitSet carvingMask, ProbabilityConfig config) {
      int i = (this.func_222704_c() * 2 - 1) * 16;
      int j = rand.nextInt(rand.nextInt(rand.nextInt(this.func_222724_a()) + 1) + 1);

      for(int k = 0; k < j; ++k) {
         double d0 = (double)(chunkX * 16 + rand.nextInt(16));
         double d1 = (double)this.generateCaveStartY(rand);
         double d2 = (double)(chunkZ * 16 + rand.nextInt(16));
         int l = 1;
         if (rand.nextInt(4) == 0) {
            double d3 = 0.5D;
            float f1 = 1.0F + rand.nextFloat() * 6.0F;
            this.func_222723_a(chunkIn, rand.nextLong(), seaLevel, p_212867_6_, p_212867_7_, d0, d1, d2, f1, 0.5D, carvingMask);
            l += rand.nextInt(4);
         }

         for(int k1 = 0; k1 < l; ++k1) {
            float f = rand.nextFloat() * ((float)Math.PI * 2F);
            float f3 = (rand.nextFloat() - 0.5F) / 4.0F;
            float f2 = this.generateCaveRadius(rand);
            int i1 = i - rand.nextInt(i / 4);
            int j1 = 0;
            this.carveTunnel(chunkIn, rand.nextLong(), seaLevel, p_212867_6_, p_212867_7_, d0, d1, d2, f2, f, f3, 0, i1, this.func_222725_b(), carvingMask);
         }
      }

      return true;
   }

   protected int func_222724_a() {
      return 15;
   }

   protected float generateCaveRadius(Random rand) {
      float f = rand.nextFloat() * 2.0F + rand.nextFloat();
      if (rand.nextInt(10) == 0) {
         f *= rand.nextFloat() * rand.nextFloat() * 3.0F + 1.0F;
      }

      return f;
   }

   protected double func_222725_b() {
      return 1.0D;
   }

   protected int generateCaveStartY(Random p_222726_1_) {
      return p_222726_1_.nextInt(p_222726_1_.nextInt(120) + 8);
   }

   protected void func_222723_a(IChunk p_222723_1_, long p_222723_2_, int p_222723_4_, int p_222723_5_, int p_222723_6_, double p_222723_7_, double p_222723_9_, double p_222723_11_, float p_222723_13_, double p_222723_14_, BitSet p_222723_16_) {
      double d0 = 1.5D + (double)(MathHelper.sin(((float)Math.PI / 2F)) * p_222723_13_);
      double d1 = d0 * p_222723_14_;
      this.func_222705_a(p_222723_1_, p_222723_2_, p_222723_4_, p_222723_5_, p_222723_6_, p_222723_7_ + 1.0D, p_222723_9_, p_222723_11_, d0, d1, p_222723_16_);
   }

   protected void carveTunnel(IChunk chunkIn, long p_222727_2_, int p_222727_4_, int p_222727_5_, int p_222727_6_, double p_222727_7_, double p_222727_9_, double p_222727_11_, float radius, float p_222727_14_, float p_222727_15_, int p_222727_16_, int p_222727_17_, double p_222727_18_, BitSet p_222727_20_) {
      Random random = new Random(p_222727_2_);
      int i = random.nextInt(p_222727_17_ / 2) + p_222727_17_ / 4;
      boolean flag = random.nextInt(6) == 0;
      float f = 0.0F;
      float f1 = 0.0F;

      for(int j = p_222727_16_; j < p_222727_17_; ++j) {
         double d0 = 1.5D + (double)(MathHelper.sin((float)Math.PI * (float)j / (float)p_222727_17_) * radius);
         double d1 = d0 * p_222727_18_;
         float f2 = MathHelper.cos(p_222727_15_);
         p_222727_7_ += (double)(MathHelper.cos(p_222727_14_) * f2);
         p_222727_9_ += (double)MathHelper.sin(p_222727_15_);
         p_222727_11_ += (double)(MathHelper.sin(p_222727_14_) * f2);
         p_222727_15_ = p_222727_15_ * (flag ? 0.92F : 0.7F);
         p_222727_15_ = p_222727_15_ + f1 * 0.1F;
         p_222727_14_ += f * 0.1F;
         f1 = f1 * 0.9F;
         f = f * 0.75F;
         f1 = f1 + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
         f = f + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;
         if (j == i && radius > 1.0F) {
            this.carveTunnel(chunkIn, random.nextLong(), p_222727_4_, p_222727_5_, p_222727_6_, p_222727_7_, p_222727_9_, p_222727_11_, random.nextFloat() * 0.5F + 0.5F, p_222727_14_ - ((float)Math.PI / 2F), p_222727_15_ / 3.0F, j, p_222727_17_, 1.0D, p_222727_20_);
            this.carveTunnel(chunkIn, random.nextLong(), p_222727_4_, p_222727_5_, p_222727_6_, p_222727_7_, p_222727_9_, p_222727_11_, random.nextFloat() * 0.5F + 0.5F, p_222727_14_ + ((float)Math.PI / 2F), p_222727_15_ / 3.0F, j, p_222727_17_, 1.0D, p_222727_20_);
            return;
         }

         if (random.nextInt(4) != 0) {
            if (!this.func_222702_a(p_222727_5_, p_222727_6_, p_222727_7_, p_222727_11_, j, p_222727_17_, radius)) {
               return;
            }

            this.func_222705_a(chunkIn, p_222727_2_, p_222727_4_, p_222727_5_, p_222727_6_, p_222727_7_, p_222727_9_, p_222727_11_, d0, d1, p_222727_20_);
         }
      }

   }

   protected boolean func_222708_a(double p_222708_1_, double p_222708_3_, double p_222708_5_, int p_222708_7_) {
      return p_222708_3_ <= -0.7D || p_222708_1_ * p_222708_1_ + p_222708_3_ * p_222708_3_ + p_222708_5_ * p_222708_5_ >= 1.0D;
   }
}