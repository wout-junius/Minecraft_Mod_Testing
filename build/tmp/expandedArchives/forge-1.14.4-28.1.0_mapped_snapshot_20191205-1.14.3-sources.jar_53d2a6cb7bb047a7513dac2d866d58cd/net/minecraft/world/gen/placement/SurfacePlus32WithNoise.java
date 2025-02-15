package net.minecraft.world.gen.placement;

import com.mojang.datafixers.Dynamic;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;

public class SurfacePlus32WithNoise extends Placement<NoiseDependant> {
   public SurfacePlus32WithNoise(Function<Dynamic<?>, ? extends NoiseDependant> p_i51365_1_) {
      super(p_i51365_1_);
   }

   public Stream<BlockPos> getPositions(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generatorIn, Random random, NoiseDependant configIn, BlockPos pos) {
      double d0 = Biome.INFO_NOISE.getValue((double)pos.getX() / 200.0D, (double)pos.getZ() / 200.0D);
      int i = d0 < configIn.noiseLevel ? configIn.belowNoise : configIn.aboveNoise;
      return IntStream.range(0, i).mapToObj((p_215054_3_) -> {
         int j = random.nextInt(16);
         int k = random.nextInt(16);
         int l = worldIn.getHeight(Heightmap.Type.MOTION_BLOCKING, pos.add(j, 0, k)).getY() + 32;
         if (l <= 0) {
            return null;
         } else {
            int i1 = random.nextInt(l);
            return pos.add(j, i1, k);
         }
      }).filter(Objects::nonNull);
   }
}