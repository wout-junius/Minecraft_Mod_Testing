package net.minecraft.world.gen.feature.structure;

import com.google.common.collect.Lists;
import com.mojang.datafixers.Dynamic;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class OceanMonumentStructure extends Structure<NoFeatureConfig> {
   private static final List<Biome.SpawnListEntry> MONUMENT_ENEMIES = Lists.newArrayList(new Biome.SpawnListEntry(EntityType.GUARDIAN, 1, 2, 4));

   public OceanMonumentStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> noFeatureConfigIn) {
      super(noFeatureConfigIn);
   }

   protected ChunkPos getStartPositionForPosition(ChunkGenerator<?> chunkGenerator, Random random, int x, int z, int spacingOffsetsX, int spacingOffsetsZ) {
      int i = chunkGenerator.getSettings().getOceanMonumentSpacing();
      int j = chunkGenerator.getSettings().getOceanMonumentSeparation();
      int k = x + i * spacingOffsetsX;
      int l = z + i * spacingOffsetsZ;
      int i1 = k < 0 ? k - i + 1 : k;
      int j1 = l < 0 ? l - i + 1 : l;
      int k1 = i1 / i;
      int l1 = j1 / i;
      ((SharedSeedRandom)random).setLargeFeatureSeedWithSalt(chunkGenerator.getSeed(), k1, l1, 10387313);
      k1 = k1 * i;
      l1 = l1 * i;
      k1 = k1 + (random.nextInt(i - j) + random.nextInt(i - j)) / 2;
      l1 = l1 + (random.nextInt(i - j) + random.nextInt(i - j)) / 2;
      return new ChunkPos(k1, l1);
   }

   public boolean hasStartAt(ChunkGenerator<?> chunkGen, Random rand, int chunkPosX, int chunkPosZ) {
      ChunkPos chunkpos = this.getStartPositionForPosition(chunkGen, rand, chunkPosX, chunkPosZ, 0, 0);
      if (chunkPosX == chunkpos.x && chunkPosZ == chunkpos.z) {
         for(Biome biome : chunkGen.getBiomeProvider().getBiomesInSquare(chunkPosX * 16 + 9, chunkPosZ * 16 + 9, 16)) {
            if (!chunkGen.hasStructure(biome, Feature.OCEAN_MONUMENT)) {
               return false;
            }
         }

         for(Biome biome1 : chunkGen.getBiomeProvider().getBiomesInSquare(chunkPosX * 16 + 9, chunkPosZ * 16 + 9, 29)) {
            if (biome1.getCategory() != Biome.Category.OCEAN && biome1.getCategory() != Biome.Category.RIVER) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public Structure.IStartFactory getStartFactory() {
      return OceanMonumentStructure.Start::new;
   }

   public String getStructureName() {
      return "Monument";
   }

   public int getSize() {
      return 8;
   }

   public List<Biome.SpawnListEntry> getSpawnList() {
      return MONUMENT_ENEMIES;
   }

   public static class Start extends StructureStart {
      private boolean wasCreated;

      public Start(Structure<?> p_i50759_1_, int chunkX, int chunkZ, Biome biomeIn, MutableBoundingBox boundsIn, int referenceIn, long seed) {
         super(p_i50759_1_, chunkX, chunkZ, biomeIn, boundsIn, referenceIn, seed);
      }

      public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn) {
         this.generateStart(chunkX, chunkZ);
      }

      private void generateStart(int chunkX, int chunkZ) {
         int i = chunkX * 16 - 29;
         int j = chunkZ * 16 - 29;
         Direction direction = Direction.Plane.HORIZONTAL.random(this.rand);
         this.components.add(new OceanMonumentPieces.MonumentBuilding(this.rand, i, j, direction));
         this.recalculateStructureSize();
         this.wasCreated = true;
      }

      /**
       * Keeps iterating Structure Pieces and spawning them until the checks tell it to stop
       */
      public void generateStructure(IWorld worldIn, Random rand, MutableBoundingBox structurebb, ChunkPos pos) {
         if (!this.wasCreated) {
            this.components.clear();
            this.generateStart(this.getChunkPosX(), this.getChunkPosZ());
         }

         super.generateStructure(worldIn, rand, structurebb, pos);
      }
   }
}