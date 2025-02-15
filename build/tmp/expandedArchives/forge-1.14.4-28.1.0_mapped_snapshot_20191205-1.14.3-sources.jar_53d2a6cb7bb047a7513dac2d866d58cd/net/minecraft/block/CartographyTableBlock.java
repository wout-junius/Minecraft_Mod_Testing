package net.minecraft.block;

import javax.annotation.Nullable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.CartographyContainer;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.stats.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class CartographyTableBlock extends Block {
   private static final TranslationTextComponent field_220268_a = new TranslationTextComponent("container.cartography_table");

   protected CartographyTableBlock(Block.Properties p_i49987_1_) {
      super(p_i49987_1_);
   }

   public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
      player.openContainer(state.getContainer(worldIn, pos));
      player.addStat(Stats.INTERACT_WITH_CARTOGRAPHY_TABLE);
      return true;
   }

   @Nullable
   public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos) {
      return new SimpleNamedContainerProvider((p_220267_2_, p_220267_3_, p_220267_4_) -> {
         return new CartographyContainer(p_220267_2_, p_220267_3_, IWorldPosCallable.of(worldIn, pos));
      }, field_220268_a);
   }
}