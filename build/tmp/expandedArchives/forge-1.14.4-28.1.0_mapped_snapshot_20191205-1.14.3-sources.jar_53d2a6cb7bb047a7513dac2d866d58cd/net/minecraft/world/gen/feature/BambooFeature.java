package net.minecraft.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import java.util.Random;
import java.util.function.Function;
import net.minecraft.block.BambooBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.properties.BambooLeaves;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;

public class BambooFeature extends Feature<ProbabilityConfig> {
   private static final BlockState BAMBOO_BASE = Blocks.BAMBOO.getDefaultState().with(BambooBlock.PROPERTY_AGE, Integer.valueOf(1)).with(BambooBlock.PROPERTY_BAMBOO_LEAVES, BambooLeaves.NONE).with(BambooBlock.PROPERTY_STAGE, Integer.valueOf(0));
   private static final BlockState BAMBOO_LARGE_LEAVES_GROWN = BAMBOO_BASE.with(BambooBlock.PROPERTY_BAMBOO_LEAVES, BambooLeaves.LARGE).with(BambooBlock.PROPERTY_STAGE, Integer.valueOf(1));
   private static final BlockState BAMBOO_LARGE_LEAVES = BAMBOO_BASE.with(BambooBlock.PROPERTY_BAMBOO_LEAVES, BambooLeaves.LARGE);
   private static final BlockState BAMBOO_SMALL_LEAVES = BAMBOO_BASE.with(BambooBlock.PROPERTY_BAMBOO_LEAVES, BambooLeaves.SMALL);

   public BambooFeature(Function<Dynamic<?>, ? extends ProbabilityConfig> p_i49919_1_) {
      super(p_i49919_1_);
   }

   public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, ProbabilityConfig config) {
      int i = 0;
      BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(pos);
      BlockPos.MutableBlockPos blockpos$mutableblockpos1 = new BlockPos.MutableBlockPos(pos);
      if (worldIn.isAirBlock(blockpos$mutableblockpos)) {
         if (Blocks.BAMBOO.getDefaultState().isValidPosition(worldIn, blockpos$mutableblockpos)) {
            int j = rand.nextInt(12) + 5;
            if (rand.nextFloat() < config.probability) {
               int k = rand.nextInt(4) + 1;

               for(int l = pos.getX() - k; l <= pos.getX() + k; ++l) {
                  for(int i1 = pos.getZ() - k; i1 <= pos.getZ() + k; ++i1) {
                     int j1 = l - pos.getX();
                     int k1 = i1 - pos.getZ();
                     if (j1 * j1 + k1 * k1 <= k * k) {
                        blockpos$mutableblockpos1.setPos(l, worldIn.getHeight(Heightmap.Type.WORLD_SURFACE, l, i1) - 1, i1);
                        if (worldIn.getBlockState(blockpos$mutableblockpos1).getBlock().isIn(BlockTags.DIRT_LIKE)) {
                           worldIn.setBlockState(blockpos$mutableblockpos1, Blocks.PODZOL.getDefaultState(), 2);
                        }
                     }
                  }
               }
            }

            for(int l1 = 0; l1 < j && worldIn.isAirBlock(blockpos$mutableblockpos); ++l1) {
               worldIn.setBlockState(blockpos$mutableblockpos, BAMBOO_BASE, 2);
               blockpos$mutableblockpos.move(Direction.UP, 1);
            }

            if (blockpos$mutableblockpos.getY() - pos.getY() >= 3) {
               worldIn.setBlockState(blockpos$mutableblockpos, BAMBOO_LARGE_LEAVES_GROWN, 2);
               worldIn.setBlockState(blockpos$mutableblockpos.move(Direction.DOWN, 1), BAMBOO_LARGE_LEAVES, 2);
               worldIn.setBlockState(blockpos$mutableblockpos.move(Direction.DOWN, 1), BAMBOO_SMALL_LEAVES, 2);
            }
         }

         ++i;
      }

      return i > 0;
   }
}