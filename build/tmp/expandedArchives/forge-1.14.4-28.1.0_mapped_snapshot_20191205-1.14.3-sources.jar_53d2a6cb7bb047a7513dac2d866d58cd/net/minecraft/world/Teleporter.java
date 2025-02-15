package net.minecraft.world;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ColumnPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Supplier;

public class Teleporter {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final NetherPortalBlock BLOCK_NETHER_PORTAL = (NetherPortalBlock)Blocks.NETHER_PORTAL;
   protected final ServerWorld world;
   protected final Random random;
   protected final Map<ColumnPos, Teleporter.PortalPosition> destinationCoordinateCache = Maps.newHashMapWithExpectedSize(4096);
   private final Object2LongMap<ColumnPos> columnPosMap = new Object2LongOpenHashMap<>();

   public Teleporter(ServerWorld worldIn) {
      this.world = worldIn;
      this.random = new Random(worldIn.getSeed());
   }

   public boolean placeInPortal(Entity p_222268_1_, float p_222268_2_) {
      Vec3d vec3d = p_222268_1_.getLastPortalVec();
      Direction direction = p_222268_1_.getTeleportDirection();
      BlockPattern.PortalInfo blockpattern$portalinfo = this.placeInExistingPortal(new BlockPos(p_222268_1_), p_222268_1_.getMotion(), direction, vec3d.x, vec3d.y, p_222268_1_ instanceof PlayerEntity);
      if (blockpattern$portalinfo == null) {
         return false;
      } else {
         Vec3d vec3d1 = blockpattern$portalinfo.pos;
         Vec3d vec3d2 = blockpattern$portalinfo.motion;
         p_222268_1_.setMotion(vec3d2);
         p_222268_1_.rotationYaw = p_222268_2_ + (float)blockpattern$portalinfo.rotation;
         if (p_222268_1_ instanceof ServerPlayerEntity) {
            ((ServerPlayerEntity)p_222268_1_).connection.setPlayerLocation(vec3d1.x, vec3d1.y, vec3d1.z, p_222268_1_.rotationYaw, p_222268_1_.rotationPitch);
            ((ServerPlayerEntity)p_222268_1_).connection.captureCurrentPosition();
         } else {
            p_222268_1_.setLocationAndAngles(vec3d1.x, vec3d1.y, vec3d1.z, p_222268_1_.rotationYaw, p_222268_1_.rotationPitch);
         }

         return true;
      }
   }

   @Nullable
   public BlockPattern.PortalInfo placeInExistingPortal(BlockPos p_222272_1_, Vec3d p_222272_2_, Direction directionIn, double p_222272_4_, double p_222272_6_, boolean p_222272_8_) {
      int i = 128;
      boolean flag = true;
      BlockPos blockpos = null;
      ColumnPos columnpos = new ColumnPos(p_222272_1_);
      if (!p_222272_8_ && this.columnPosMap.containsKey(columnpos)) {
         return null;
      } else {
         Teleporter.PortalPosition teleporter$portalposition = this.destinationCoordinateCache.get(columnpos);
         if (teleporter$portalposition != null) {
            blockpos = teleporter$portalposition.pos;
            teleporter$portalposition.lastUpdateTime = this.world.getGameTime();
            flag = false;
         } else {
            double d0 = Double.MAX_VALUE;

            for(int j = -128; j <= 128; ++j) {
               BlockPos blockpos2;
               for(int k = -128; k <= 128; ++k) {
                  for(BlockPos blockpos1 = p_222272_1_.add(j, this.world.getActualHeight() - 1 - p_222272_1_.getY(), k); blockpos1.getY() >= 0; blockpos1 = blockpos2) {
                     blockpos2 = blockpos1.down();
                     if (this.world.getBlockState(blockpos1).getBlock() == BLOCK_NETHER_PORTAL) {
                        for(blockpos2 = blockpos1.down(); this.world.getBlockState(blockpos2).getBlock() == BLOCK_NETHER_PORTAL; blockpos2 = blockpos2.down()) {
                           blockpos1 = blockpos2;
                        }

                        double d1 = blockpos1.distanceSq(p_222272_1_);
                        if (d0 < 0.0D || d1 < d0) {
                           d0 = d1;
                           blockpos = blockpos1;
                        }
                     }
                  }
               }
            }
         }

         if (blockpos == null) {
            long l = this.world.getGameTime() + 300L;
            this.columnPosMap.put(columnpos, l);
            return null;
         } else {
            if (flag) {
               this.destinationCoordinateCache.put(columnpos, new Teleporter.PortalPosition(blockpos, this.world.getGameTime()));
               Logger logger = LOGGER;
               Supplier[] asupplier = new Supplier[2];
               Dimension dimension = this.world.getDimension();
               asupplier[0] = dimension::getType;
               asupplier[1] = () -> {
                  return columnpos;
               };
               logger.debug("Adding nether portal ticket for {}:{}", asupplier);
               this.world.getChunkProvider().func_217228_a(TicketType.PORTAL, new ChunkPos(blockpos), 3, columnpos);
            }

            BlockPattern.PatternHelper blockpattern$patternhelper = BLOCK_NETHER_PORTAL.createPatternHelper(this.world, blockpos);
            return blockpattern$patternhelper.getPortalInfo(directionIn, blockpos, p_222272_6_, p_222272_2_, p_222272_4_);
         }
      }
   }

   public boolean makePortal(Entity entityIn) {
      int i = 16;
      double d0 = -1.0D;
      int j = MathHelper.floor(entityIn.posX);
      int k = MathHelper.floor(entityIn.posY);
      int l = MathHelper.floor(entityIn.posZ);
      int i1 = j;
      int j1 = k;
      int k1 = l;
      int l1 = 0;
      int i2 = this.random.nextInt(4);
      BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

      for(int j2 = j - 16; j2 <= j + 16; ++j2) {
         double d1 = (double)j2 + 0.5D - entityIn.posX;

         for(int l2 = l - 16; l2 <= l + 16; ++l2) {
            double d2 = (double)l2 + 0.5D - entityIn.posZ;

            label276:
            for(int j3 = this.world.getActualHeight() - 1; j3 >= 0; --j3) {
               if (this.world.isAirBlock(blockpos$mutableblockpos.setPos(j2, j3, l2))) {
                  while(j3 > 0 && this.world.isAirBlock(blockpos$mutableblockpos.setPos(j2, j3 - 1, l2))) {
                     --j3;
                  }

                  for(int k3 = i2; k3 < i2 + 4; ++k3) {
                     int l3 = k3 % 2;
                     int i4 = 1 - l3;
                     if (k3 % 4 >= 2) {
                        l3 = -l3;
                        i4 = -i4;
                     }

                     for(int j4 = 0; j4 < 3; ++j4) {
                        for(int k4 = 0; k4 < 4; ++k4) {
                           for(int l4 = -1; l4 < 4; ++l4) {
                              int i5 = j2 + (k4 - 1) * l3 + j4 * i4;
                              int j5 = j3 + l4;
                              int k5 = l2 + (k4 - 1) * i4 - j4 * l3;
                              blockpos$mutableblockpos.setPos(i5, j5, k5);
                              if (l4 < 0 && !this.world.getBlockState(blockpos$mutableblockpos).getMaterial().isSolid() || l4 >= 0 && !this.world.isAirBlock(blockpos$mutableblockpos)) {
                                 continue label276;
                              }
                           }
                        }
                     }

                     double d5 = (double)j3 + 0.5D - entityIn.posY;
                     double d7 = d1 * d1 + d5 * d5 + d2 * d2;
                     if (d0 < 0.0D || d7 < d0) {
                        d0 = d7;
                        i1 = j2;
                        j1 = j3;
                        k1 = l2;
                        l1 = k3 % 4;
                     }
                  }
               }
            }
         }
      }

      if (d0 < 0.0D) {
         for(int l5 = j - 16; l5 <= j + 16; ++l5) {
            double d3 = (double)l5 + 0.5D - entityIn.posX;

            for(int j6 = l - 16; j6 <= l + 16; ++j6) {
               double d4 = (double)j6 + 0.5D - entityIn.posZ;

               label214:
               for(int i7 = this.world.getActualHeight() - 1; i7 >= 0; --i7) {
                  if (this.world.isAirBlock(blockpos$mutableblockpos.setPos(l5, i7, j6))) {
                     while(i7 > 0 && this.world.isAirBlock(blockpos$mutableblockpos.setPos(l5, i7 - 1, j6))) {
                        --i7;
                     }

                     for(int l7 = i2; l7 < i2 + 2; ++l7) {
                        int l8 = l7 % 2;
                        int k9 = 1 - l8;

                        for(int i10 = 0; i10 < 4; ++i10) {
                           for(int k10 = -1; k10 < 4; ++k10) {
                              int i11 = l5 + (i10 - 1) * l8;
                              int j11 = i7 + k10;
                              int k11 = j6 + (i10 - 1) * k9;
                              blockpos$mutableblockpos.setPos(i11, j11, k11);
                              if (k10 < 0 && !this.world.getBlockState(blockpos$mutableblockpos).getMaterial().isSolid() || k10 >= 0 && !this.world.isAirBlock(blockpos$mutableblockpos)) {
                                 continue label214;
                              }
                           }
                        }

                        double d6 = (double)i7 + 0.5D - entityIn.posY;
                        double d8 = d3 * d3 + d6 * d6 + d4 * d4;
                        if (d0 < 0.0D || d8 < d0) {
                           d0 = d8;
                           i1 = l5;
                           j1 = i7;
                           k1 = j6;
                           l1 = l7 % 2;
                        }
                     }
                  }
               }
            }
         }
      }

      int i6 = i1;
      int k2 = j1;
      int k6 = k1;
      int l6 = l1 % 2;
      int i3 = 1 - l6;
      if (l1 % 4 >= 2) {
         l6 = -l6;
         i3 = -i3;
      }

      if (d0 < 0.0D) {
         j1 = MathHelper.clamp(j1, 70, this.world.getActualHeight() - 10);
         k2 = j1;

         for(int j7 = -1; j7 <= 1; ++j7) {
            for(int i8 = 1; i8 < 3; ++i8) {
               for(int i9 = -1; i9 < 3; ++i9) {
                  int l9 = i6 + (i8 - 1) * l6 + j7 * i3;
                  int j10 = k2 + i9;
                  int l10 = k6 + (i8 - 1) * i3 - j7 * l6;
                  boolean flag = i9 < 0;
                  blockpos$mutableblockpos.setPos(l9, j10, l10);
                  this.world.setBlockState(blockpos$mutableblockpos, flag ? Blocks.OBSIDIAN.getDefaultState() : Blocks.AIR.getDefaultState());
               }
            }
         }
      }

      for(int k7 = -1; k7 < 3; ++k7) {
         for(int j8 = -1; j8 < 4; ++j8) {
            if (k7 == -1 || k7 == 2 || j8 == -1 || j8 == 3) {
               blockpos$mutableblockpos.setPos(i6 + k7 * l6, k2 + j8, k6 + k7 * i3);
               this.world.setBlockState(blockpos$mutableblockpos, Blocks.OBSIDIAN.getDefaultState(), 3);
            }
         }
      }

      BlockState blockstate = BLOCK_NETHER_PORTAL.getDefaultState().with(NetherPortalBlock.AXIS, l6 == 0 ? Direction.Axis.Z : Direction.Axis.X);

      for(int k8 = 0; k8 < 2; ++k8) {
         for(int j9 = 0; j9 < 3; ++j9) {
            blockpos$mutableblockpos.setPos(i6 + k8 * l6, k2 + j9, k6 + k8 * i3);
            this.world.setBlockState(blockpos$mutableblockpos, blockstate, 18);
         }
      }

      return true;
   }

   /**
    * called periodically to remove out-of-date portal locations from the cache list. Argument par1 is a
    * WorldServer.getTotalWorldTime() value.
    */
   public void tick(long worldTime) {
      if (worldTime % 100L == 0L) {
         this.removeStaleColumnPositions(worldTime);
         this.removeStalePortalLocations(worldTime);
      }

   }

   private void removeStaleColumnPositions(long p_222270_1_) {
      LongIterator longiterator = this.columnPosMap.values().iterator();

      while(longiterator.hasNext()) {
         long i = longiterator.nextLong();
         if (i <= p_222270_1_) {
            longiterator.remove();
         }
      }

   }

   private void removeStalePortalLocations(long p_222269_1_) {
      long i = p_222269_1_ - 300L;
      Iterator<Entry<ColumnPos, Teleporter.PortalPosition>> iterator = this.destinationCoordinateCache.entrySet().iterator();

      while(iterator.hasNext()) {
         Entry<ColumnPos, Teleporter.PortalPosition> entry = iterator.next();
         Teleporter.PortalPosition teleporter$portalposition = entry.getValue();
         if (teleporter$portalposition.lastUpdateTime < i) {
            ColumnPos columnpos = entry.getKey();
            Logger logger = LOGGER;
            Supplier[] asupplier = new Supplier[2];
            Dimension dimension = this.world.getDimension();
            asupplier[0] = dimension::getType;
            asupplier[1] = () -> {
               return columnpos;
            };
            logger.debug("Removing nether portal ticket for {}:{}", asupplier);
            this.world.getChunkProvider().func_217222_b(TicketType.PORTAL, new ChunkPos(teleporter$portalposition.pos), 3, columnpos);
            iterator.remove();
         }
      }

   }

   static class PortalPosition {
      public final BlockPos pos;
      public long lastUpdateTime;

      public PortalPosition(BlockPos p_i50813_1_, long p_i50813_2_) {
         this.pos = p_i50813_1_;
         this.lastUpdateTime = p_i50813_2_;
      }
   }
}