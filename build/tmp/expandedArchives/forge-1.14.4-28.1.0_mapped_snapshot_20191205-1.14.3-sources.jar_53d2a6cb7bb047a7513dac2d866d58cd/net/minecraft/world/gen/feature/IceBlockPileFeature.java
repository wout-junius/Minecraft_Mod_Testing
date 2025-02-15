package net.minecraft.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import java.util.function.Function;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.IWorld;

public class IceBlockPileFeature extends BlockPileFeature {
   public IceBlockPileFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> p_i49862_1_) {
      super(p_i49862_1_);
   }

   protected BlockState getRandomBlock(IWorld worldIn) {
      return worldIn.getRandom().nextInt(7) == 0 ? Blocks.BLUE_ICE.getDefaultState() : Blocks.PACKED_ICE.getDefaultState();
   }
}