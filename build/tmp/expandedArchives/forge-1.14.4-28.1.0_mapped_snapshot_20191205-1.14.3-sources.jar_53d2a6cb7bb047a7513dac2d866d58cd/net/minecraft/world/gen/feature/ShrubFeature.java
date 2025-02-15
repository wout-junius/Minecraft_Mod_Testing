package net.minecraft.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.IWorldGenerationReader;

public class ShrubFeature extends AbstractTreeFeature<NoFeatureConfig> {
   private final BlockState leaf;
   private final BlockState trunk;

   public ShrubFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> p_i49868_1_, BlockState trunk, BlockState leaf) {
      super(p_i49868_1_, false);
      this.trunk = trunk;
      this.leaf = leaf;
      setSapling((net.minecraftforge.common.IPlantable)net.minecraft.block.Blocks.JUNGLE_SAPLING);
   }

   public boolean place(Set<BlockPos> changedBlocks, IWorldGenerationReader worldIn, Random rand, BlockPos position, MutableBoundingBox boundsIn) {
      position = worldIn.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, position).down();
      if (isSoil(worldIn, position, getSapling())) {
         position = position.up();
         this.setLogState(changedBlocks, worldIn, position, this.trunk, boundsIn);

         for(int i = position.getY(); i <= position.getY() + 2; ++i) {
            int j = i - position.getY();
            int k = 2 - j;

            for(int l = position.getX() - k; l <= position.getX() + k; ++l) {
               int i1 = l - position.getX();

               for(int j1 = position.getZ() - k; j1 <= position.getZ() + k; ++j1) {
                  int k1 = j1 - position.getZ();
                  if (Math.abs(i1) != k || Math.abs(k1) != k || rand.nextInt(2) != 0) {
                     BlockPos blockpos = new BlockPos(l, i, j1);
                     if (isAirOrLeaves(worldIn, blockpos)) {
                        this.setLogState(changedBlocks, worldIn, blockpos, this.leaf, boundsIn);
                     }
                  }
               }
            }
         }
      }

      return true;
   }
}