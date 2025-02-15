package net.minecraft.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BellBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.server.ServerWorld;

public class RingBellTask extends Task<LivingEntity> {
   public RingBellTask() {
      super(ImmutableMap.of(MemoryModuleType.MEETING_POINT, MemoryModuleStatus.VALUE_PRESENT));
   }

   protected boolean shouldExecute(ServerWorld worldIn, LivingEntity owner) {
      return worldIn.rand.nextFloat() > 0.95F;
   }

   protected void startExecuting(ServerWorld worldIn, LivingEntity entityIn, long gameTimeIn) {
      Brain<?> brain = entityIn.getBrain();
      BlockPos blockpos = brain.getMemory(MemoryModuleType.MEETING_POINT).get().getPos();
      if (blockpos.withinDistance(new BlockPos(entityIn), 3.0D)) {
         BlockState blockstate = worldIn.getBlockState(blockpos);
         if (blockstate.getBlock() == Blocks.BELL) {
            BellBlock bellblock = (BellBlock)blockstate.getBlock();

            for(Direction direction : Direction.Plane.HORIZONTAL) {
               if (bellblock.ring(worldIn, blockstate, worldIn.getTileEntity(blockpos), new BlockRayTraceResult(new Vec3d(0.5D, 0.5D, 0.5D), direction, blockpos, false), (PlayerEntity)null, false)) {
                  break;
               }
            }
         }
      }

   }
}