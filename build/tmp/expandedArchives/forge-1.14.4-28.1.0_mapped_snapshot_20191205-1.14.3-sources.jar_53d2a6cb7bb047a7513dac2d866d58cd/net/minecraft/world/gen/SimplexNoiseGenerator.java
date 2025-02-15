package net.minecraft.world.gen;

import java.util.Random;
import net.minecraft.util.math.MathHelper;

public class SimplexNoiseGenerator {
   protected static final int[][] GRADS = new int[][]{{1, 1, 0}, {-1, 1, 0}, {1, -1, 0}, {-1, -1, 0}, {1, 0, 1}, {-1, 0, 1}, {1, 0, -1}, {-1, 0, -1}, {0, 1, 1}, {0, -1, 1}, {0, 1, -1}, {0, -1, -1}, {1, 1, 0}, {0, -1, 1}, {-1, 1, 0}, {0, -1, -1}};
   private static final double SQRT_3 = Math.sqrt(3.0D);
   private static final double F2 = 0.5D * (SQRT_3 - 1.0D);
   private static final double G2 = (3.0D - SQRT_3) / 6.0D;
   private final int[] p = new int[512];
   public final double xo;
   public final double yo;
   public final double zo;

   public SimplexNoiseGenerator(Random seed) {
      this.xo = seed.nextDouble() * 256.0D;
      this.yo = seed.nextDouble() * 256.0D;
      this.zo = seed.nextDouble() * 256.0D;

      for(int i = 0; i < 256; this.p[i] = i++) {
         ;
      }

      for(int l = 0; l < 256; ++l) {
         int j = seed.nextInt(256 - l);
         int k = this.p[l];
         this.p[l] = this.p[j + l];
         this.p[j + l] = k;
      }

   }

   private int getPermutValue(int permutIndex) {
      return this.p[permutIndex & 255];
   }

   protected static double processGrad(int[] gradElement, double xFactor, double yFactor, double zFactor) {
      return (double)gradElement[0] * xFactor + (double)gradElement[1] * yFactor + (double)gradElement[2] * zFactor;
   }

   private double getContrib(int gradIndex, double x, double y, double z, double offset) {
      double d1 = offset - x * x - y * y - z * z;
      double d0;
      if (d1 < 0.0D) {
         d0 = 0.0D;
      } else {
         d1 = d1 * d1;
         d0 = d1 * d1 * processGrad(GRADS[gradIndex], x, y, z);
      }

      return d0;
   }

   public double getValue(double x, double y) {
      double d0 = (x + y) * F2;
      int i = MathHelper.floor(x + d0);
      int j = MathHelper.floor(y + d0);
      double d1 = (double)(i + j) * G2;
      double d2 = (double)i - d1;
      double d3 = (double)j - d1;
      double d4 = x - d2;
      double d5 = y - d3;
      int k;
      int l;
      if (d4 > d5) {
         k = 1;
         l = 0;
      } else {
         k = 0;
         l = 1;
      }

      double d6 = d4 - (double)k + G2;
      double d7 = d5 - (double)l + G2;
      double d8 = d4 - 1.0D + 2.0D * G2;
      double d9 = d5 - 1.0D + 2.0D * G2;
      int i1 = i & 255;
      int j1 = j & 255;
      int k1 = this.getPermutValue(i1 + this.getPermutValue(j1)) % 12;
      int l1 = this.getPermutValue(i1 + k + this.getPermutValue(j1 + l)) % 12;
      int i2 = this.getPermutValue(i1 + 1 + this.getPermutValue(j1 + 1)) % 12;
      double d10 = this.getContrib(k1, d4, d5, 0.0D, 0.5D);
      double d11 = this.getContrib(l1, d6, d7, 0.0D, 0.5D);
      double d12 = this.getContrib(i2, d8, d9, 0.0D, 0.5D);
      return 70.0D * (d10 + d11 + d12);
   }
}