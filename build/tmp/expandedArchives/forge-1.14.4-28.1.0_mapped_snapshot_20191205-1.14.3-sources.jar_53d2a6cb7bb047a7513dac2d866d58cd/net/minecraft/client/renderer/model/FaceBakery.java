package net.minecraft.client.renderer.model;

import javax.annotation.Nullable;
import net.minecraft.client.renderer.FaceDirection;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.Vector4f;
import net.minecraft.client.renderer.texture.ISprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FaceBakery {
   private static final float SCALE_ROTATION_22_5 = 1.0F / (float)Math.cos((double)((float)Math.PI / 8F)) - 1.0F;
   private static final float SCALE_ROTATION_GENERAL = 1.0F / (float)Math.cos((double)((float)Math.PI / 4F)) - 1.0F;
   private static final FaceBakery.Rotation[] UV_ROTATIONS = new FaceBakery.Rotation[ModelRotation.values().length * Direction.values().length];
   private static final FaceBakery.Rotation UV_ROTATION_0 = new FaceBakery.Rotation() {
      BlockFaceUV makeRotatedUV(float u1, float v1, float u2, float v2) {
         return new BlockFaceUV(new float[]{u1, v1, u2, v2}, 0);
      }
   };
   private static final FaceBakery.Rotation UV_ROTATION_270 = new FaceBakery.Rotation() {
      BlockFaceUV makeRotatedUV(float u1, float v1, float u2, float v2) {
         return new BlockFaceUV(new float[]{v2, 16.0F - u1, v1, 16.0F - u2}, 270);
      }
   };
   private static final FaceBakery.Rotation UV_ROTATION_INVERSE = new FaceBakery.Rotation() {
      BlockFaceUV makeRotatedUV(float u1, float v1, float u2, float v2) {
         return new BlockFaceUV(new float[]{16.0F - u1, 16.0F - v1, 16.0F - u2, 16.0F - v2}, 0);
      }
   };
   private static final FaceBakery.Rotation UV_ROTATION_90 = new FaceBakery.Rotation() {
      BlockFaceUV makeRotatedUV(float u1, float v1, float u2, float v2) {
         return new BlockFaceUV(new float[]{16.0F - v1, u2, 16.0F - v2, u1}, 90);
      }
   };

   public BakedQuad func_217648_a(Vector3f p_217648_1_, Vector3f p_217648_2_, BlockPartFace p_217648_3_, TextureAtlasSprite p_217648_4_, Direction p_217648_5_, ISprite p_217648_6_, @Nullable BlockPartRotation p_217648_7_, boolean p_217648_8_) {
       return makeBakedQuad(p_217648_1_, p_217648_2_, p_217648_3_, p_217648_4_, p_217648_5_, p_217648_6_, p_217648_7_, p_217648_8_);
   }

   public BakedQuad makeBakedQuad(Vector3f p_217648_1_, Vector3f p_217648_2_, BlockPartFace p_217648_3_, TextureAtlasSprite p_217648_4_, Direction p_217648_5_, ISprite p_217648_6_, @Nullable BlockPartRotation p_217648_7_, boolean p_217648_8_) {
      BlockFaceUV blockfaceuv = p_217648_3_.blockFaceUV;
      net.minecraftforge.common.model.TRSRTransformation transform = p_217648_6_.getState().apply(java.util.Optional.empty())
              .orElse(net.minecraftforge.common.model.TRSRTransformation.identity());
      if (p_217648_6_.isUvLock()) {
         blockfaceuv = net.minecraftforge.client.ForgeHooksClient.applyUVLock(p_217648_3_.blockFaceUV, p_217648_5_, transform);
      }

      float[] afloat = new float[blockfaceuv.uvs.length];
      System.arraycopy(blockfaceuv.uvs, 0, afloat, 0, afloat.length);
      float f = (float)p_217648_4_.getWidth() / (p_217648_4_.getMaxU() - p_217648_4_.getMinU());
      float f1 = (float)p_217648_4_.getHeight() / (p_217648_4_.getMaxV() - p_217648_4_.getMinV());
      float f2 = 4.0F / Math.max(f1, f);
      float f3 = (blockfaceuv.uvs[0] + blockfaceuv.uvs[0] + blockfaceuv.uvs[2] + blockfaceuv.uvs[2]) / 4.0F;
      float f4 = (blockfaceuv.uvs[1] + blockfaceuv.uvs[1] + blockfaceuv.uvs[3] + blockfaceuv.uvs[3]) / 4.0F;
      blockfaceuv.uvs[0] = MathHelper.lerp(f2, blockfaceuv.uvs[0], f3);
      blockfaceuv.uvs[2] = MathHelper.lerp(f2, blockfaceuv.uvs[2], f3);
      blockfaceuv.uvs[1] = MathHelper.lerp(f2, blockfaceuv.uvs[1], f4);
      blockfaceuv.uvs[3] = MathHelper.lerp(f2, blockfaceuv.uvs[3], f4);
      int[] aint = this.makeQuadVertexData(blockfaceuv, p_217648_4_, p_217648_5_, this.getPositionsDiv16(p_217648_1_, p_217648_2_), transform, p_217648_7_, false); // FORGE: *Must* pass false here, shade value is applied at render time
      Direction direction = getFacingFromVertexData(aint);
      System.arraycopy(afloat, 0, blockfaceuv.uvs, 0, afloat.length);
      if (p_217648_7_ == null) {
         this.applyFacing(aint, direction);
      }

      net.minecraftforge.client.ForgeHooksClient.fillNormal(aint, direction);
      return new BakedQuad(aint, p_217648_3_.tintIndex, direction, p_217648_4_, p_217648_8_, net.minecraft.client.renderer.vertex.DefaultVertexFormats.ITEM);
   }

   private BlockFaceUV applyUVLock(BlockFaceUV blockFaceUVIn, Direction facing, ModelRotation modelRotationIn) {
      return UV_ROTATIONS[getIndex(modelRotationIn, facing)].rotateUV(blockFaceUVIn);
   }

   private int[] makeQuadVertexData(BlockFaceUV uvs, TextureAtlasSprite sprite, Direction orientation, float[] posDiv16, ModelRotation rotationIn, @Nullable BlockPartRotation partRotation, boolean shade) {
      return makeQuadVertexData(uvs, sprite, orientation, posDiv16, (net.minecraftforge.common.model.ITransformation)rotationIn, partRotation, shade);
   }

   private int[] makeQuadVertexData(BlockFaceUV uvs, TextureAtlasSprite sprite, Direction orientation, float[] posDiv16, net.minecraftforge.common.model.ITransformation rotationIn, BlockPartRotation partRotation, boolean shade) {
      int[] aint = new int[28];

      for(int i = 0; i < 4; ++i) {
         this.fillVertexData(aint, i, orientation, uvs, posDiv16, sprite, rotationIn, partRotation, shade);
      }

      return aint;
   }

   private int getFaceShadeColor(Direction facing) {
      float f = this.getFaceBrightness(facing);
      int i = MathHelper.clamp((int)(f * 255.0F), 0, 255);
      return -16777216 | i << 16 | i << 8 | i;
   }

   private float getFaceBrightness(Direction facing) {
      switch(facing) {
      case DOWN:
         return 0.5F;
      case UP:
         return 1.0F;
      case NORTH:
      case SOUTH:
         return 0.8F;
      case WEST:
      case EAST:
         return 0.6F;
      default:
         return 1.0F;
      }
   }

   private float[] getPositionsDiv16(Vector3f pos1, Vector3f pos2) {
      float[] afloat = new float[Direction.values().length];
      afloat[FaceDirection.Constants.WEST_INDEX] = pos1.getX() / 16.0F;
      afloat[FaceDirection.Constants.DOWN_INDEX] = pos1.getY() / 16.0F;
      afloat[FaceDirection.Constants.NORTH_INDEX] = pos1.getZ() / 16.0F;
      afloat[FaceDirection.Constants.EAST_INDEX] = pos2.getX() / 16.0F;
      afloat[FaceDirection.Constants.UP_INDEX] = pos2.getY() / 16.0F;
      afloat[FaceDirection.Constants.SOUTH_INDEX] = pos2.getZ() / 16.0F;
      return afloat;
   }

   private void fillVertexData(int[] vertexData, int vertexIndex, Direction facing, BlockFaceUV blockFaceUVIn, float[] posDiv16, TextureAtlasSprite sprite, ModelRotation rotationIn, @Nullable BlockPartRotation partRotation, boolean shade) {
      fillVertexData(vertexData, vertexIndex, facing, blockFaceUVIn, posDiv16, sprite, (net.minecraftforge.common.model.ITransformation)rotationIn, partRotation, shade);
   }

   private void fillVertexData(int[] vertexData, int vertexIndex, Direction facing, BlockFaceUV blockFaceUVIn, float[] posDiv16, TextureAtlasSprite sprite, net.minecraftforge.common.model.ITransformation rotationIn, @Nullable BlockPartRotation partRotation, boolean shade) {
      Direction direction = rotationIn.rotateTransform(facing);
      int i = shade ? this.getFaceShadeColor(direction) : -1;
      FaceDirection.VertexInformation facedirection$vertexinformation = FaceDirection.getFacing(facing).getVertexInformation(vertexIndex);
      Vector3f vector3f = new Vector3f(posDiv16[facedirection$vertexinformation.xIndex], posDiv16[facedirection$vertexinformation.yIndex], posDiv16[facedirection$vertexinformation.zIndex]);
      this.rotatePart(vector3f, partRotation);
      int j = this.rotateVertex(vector3f, facing, vertexIndex, rotationIn);
      this.storeVertexData(vertexData, j, vertexIndex, vector3f, i, sprite, blockFaceUVIn);
   }

   private void storeVertexData(int[] faceData, int storeIndex, int vertexIndex, Vector3f position, int shadeColor, TextureAtlasSprite sprite, BlockFaceUV faceUV) {
      int i = storeIndex * 7;
      faceData[i] = Float.floatToRawIntBits(position.getX());
      faceData[i + 1] = Float.floatToRawIntBits(position.getY());
      faceData[i + 2] = Float.floatToRawIntBits(position.getZ());
      faceData[i + 3] = shadeColor;
      faceData[i + 4] = Float.floatToRawIntBits(sprite.getInterpolatedU((double)faceUV.getVertexU(vertexIndex) * .999 + faceUV.getVertexU((vertexIndex + 2) % 4) * .001));
      faceData[i + 4 + 1] = Float.floatToRawIntBits(sprite.getInterpolatedV((double)faceUV.getVertexV(vertexIndex) * .999 + faceUV.getVertexV((vertexIndex + 2) % 4) * .001));
   }

   private void rotatePart(Vector3f vec, @Nullable BlockPartRotation partRotation) {
      if (partRotation != null) {
         Vector3f vector3f;
         Vector3f vector3f1;
         switch(partRotation.axis) {
         case X:
            vector3f = new Vector3f(1.0F, 0.0F, 0.0F);
            vector3f1 = new Vector3f(0.0F, 1.0F, 1.0F);
            break;
         case Y:
            vector3f = new Vector3f(0.0F, 1.0F, 0.0F);
            vector3f1 = new Vector3f(1.0F, 0.0F, 1.0F);
            break;
         case Z:
            vector3f = new Vector3f(0.0F, 0.0F, 1.0F);
            vector3f1 = new Vector3f(1.0F, 1.0F, 0.0F);
            break;
         default:
            throw new IllegalArgumentException("There are only 3 axes");
         }

         Quaternion quaternion = new Quaternion(vector3f, partRotation.angle, true);
         if (partRotation.rescale) {
            if (Math.abs(partRotation.angle) == 22.5F) {
               vector3f1.mul(SCALE_ROTATION_22_5);
            } else {
               vector3f1.mul(SCALE_ROTATION_GENERAL);
            }

            vector3f1.add(1.0F, 1.0F, 1.0F);
         } else {
            vector3f1.set(1.0F, 1.0F, 1.0F);
         }

         this.rotateScale(vec, new Vector3f(partRotation.origin), quaternion, vector3f1);
      }
   }

   public int rotateVertex(Vector3f p_199335_1_, Direction p_199335_2_, int p_199335_3_, ModelRotation p_199335_4_) {
      return rotateVertex(p_199335_1_, p_199335_2_, p_199335_3_, (net.minecraftforge.common.model.ITransformation)p_199335_4_);
   }

   public int rotateVertex(Vector3f p_199335_1_, Direction p_199335_2_, int p_199335_3_, net.minecraftforge.common.model.ITransformation p_199335_4_) {
      if (p_199335_4_ == ModelRotation.X0_Y0) {
         return p_199335_3_;
      } else {
         net.minecraftforge.client.ForgeHooksClient.transform(p_199335_1_, p_199335_4_.getMatrixVec());
         return p_199335_4_.rotate(p_199335_2_, p_199335_3_);
      }
   }

   private void rotateScale(Vector3f p_199334_1_, Vector3f p_199334_2_, Quaternion p_199334_3_, Vector3f p_199334_4_) {
      Vector4f vector4f = new Vector4f(p_199334_1_.getX() - p_199334_2_.getX(), p_199334_1_.getY() - p_199334_2_.getY(), p_199334_1_.getZ() - p_199334_2_.getZ(), 1.0F);
      vector4f.func_195912_a(p_199334_3_);
      vector4f.scale(p_199334_4_);
      p_199334_1_.set(vector4f.getX() + p_199334_2_.getX(), vector4f.getY() + p_199334_2_.getY(), vector4f.getZ() + p_199334_2_.getZ());
   }

   public static Direction getFacingFromVertexData(int[] faceData) {
      Vector3f vector3f = new Vector3f(Float.intBitsToFloat(faceData[0]), Float.intBitsToFloat(faceData[1]), Float.intBitsToFloat(faceData[2]));
      Vector3f vector3f1 = new Vector3f(Float.intBitsToFloat(faceData[7]), Float.intBitsToFloat(faceData[8]), Float.intBitsToFloat(faceData[9]));
      Vector3f vector3f2 = new Vector3f(Float.intBitsToFloat(faceData[14]), Float.intBitsToFloat(faceData[15]), Float.intBitsToFloat(faceData[16]));
      Vector3f vector3f3 = new Vector3f(vector3f);
      vector3f3.sub(vector3f1);
      Vector3f vector3f4 = new Vector3f(vector3f2);
      vector3f4.sub(vector3f1);
      Vector3f vector3f5 = new Vector3f(vector3f4);
      vector3f5.cross(vector3f3);
      vector3f5.normalize();
      Direction direction = null;
      float f = 0.0F;

      for(Direction direction1 : Direction.values()) {
         Vec3i vec3i = direction1.getDirectionVec();
         Vector3f vector3f6 = new Vector3f((float)vec3i.getX(), (float)vec3i.getY(), (float)vec3i.getZ());
         float f1 = vector3f5.dot(vector3f6);
         if (f1 >= 0.0F && f1 > f) {
            f = f1;
            direction = direction1;
         }
      }

      if (direction == null) {
         return Direction.UP;
      } else {
         return direction;
      }
   }

   private void applyFacing(int[] p_178408_1_, Direction p_178408_2_) {
      int[] aint = new int[p_178408_1_.length];
      System.arraycopy(p_178408_1_, 0, aint, 0, p_178408_1_.length);
      float[] afloat = new float[Direction.values().length];
      afloat[FaceDirection.Constants.WEST_INDEX] = 999.0F;
      afloat[FaceDirection.Constants.DOWN_INDEX] = 999.0F;
      afloat[FaceDirection.Constants.NORTH_INDEX] = 999.0F;
      afloat[FaceDirection.Constants.EAST_INDEX] = -999.0F;
      afloat[FaceDirection.Constants.UP_INDEX] = -999.0F;
      afloat[FaceDirection.Constants.SOUTH_INDEX] = -999.0F;

      for(int i = 0; i < 4; ++i) {
         int j = 7 * i;
         float f = Float.intBitsToFloat(aint[j]);
         float f1 = Float.intBitsToFloat(aint[j + 1]);
         float f2 = Float.intBitsToFloat(aint[j + 2]);
         if (f < afloat[FaceDirection.Constants.WEST_INDEX]) {
            afloat[FaceDirection.Constants.WEST_INDEX] = f;
         }

         if (f1 < afloat[FaceDirection.Constants.DOWN_INDEX]) {
            afloat[FaceDirection.Constants.DOWN_INDEX] = f1;
         }

         if (f2 < afloat[FaceDirection.Constants.NORTH_INDEX]) {
            afloat[FaceDirection.Constants.NORTH_INDEX] = f2;
         }

         if (f > afloat[FaceDirection.Constants.EAST_INDEX]) {
            afloat[FaceDirection.Constants.EAST_INDEX] = f;
         }

         if (f1 > afloat[FaceDirection.Constants.UP_INDEX]) {
            afloat[FaceDirection.Constants.UP_INDEX] = f1;
         }

         if (f2 > afloat[FaceDirection.Constants.SOUTH_INDEX]) {
            afloat[FaceDirection.Constants.SOUTH_INDEX] = f2;
         }
      }

      FaceDirection facedirection = FaceDirection.getFacing(p_178408_2_);

      for(int i1 = 0; i1 < 4; ++i1) {
         int j1 = 7 * i1;
         FaceDirection.VertexInformation facedirection$vertexinformation = facedirection.getVertexInformation(i1);
         float f8 = afloat[facedirection$vertexinformation.xIndex];
         float f3 = afloat[facedirection$vertexinformation.yIndex];
         float f4 = afloat[facedirection$vertexinformation.zIndex];
         p_178408_1_[j1] = Float.floatToRawIntBits(f8);
         p_178408_1_[j1 + 1] = Float.floatToRawIntBits(f3);
         p_178408_1_[j1 + 2] = Float.floatToRawIntBits(f4);

         for(int k = 0; k < 4; ++k) {
            int l = 7 * k;
            float f5 = Float.intBitsToFloat(aint[l]);
            float f6 = Float.intBitsToFloat(aint[l + 1]);
            float f7 = Float.intBitsToFloat(aint[l + 2]);
            if (MathHelper.epsilonEquals(f8, f5) && MathHelper.epsilonEquals(f3, f6) && MathHelper.epsilonEquals(f4, f7)) {
               p_178408_1_[j1 + 4] = aint[l + 4];
               p_178408_1_[j1 + 4 + 1] = aint[l + 4 + 1];
            }
         }
      }

   }

   private static void addUvRotation(ModelRotation p_188013_0_, Direction p_188013_1_, FaceBakery.Rotation p_188013_2_) {
      UV_ROTATIONS[getIndex(p_188013_0_, p_188013_1_)] = p_188013_2_;
   }

   private static int getIndex(ModelRotation p_188014_0_, Direction p_188014_1_) {
      return ModelRotation.values().length * p_188014_1_.ordinal() + p_188014_0_.ordinal();
   }

   static {
      addUvRotation(ModelRotation.X0_Y0, Direction.DOWN, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y0, Direction.EAST, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y0, Direction.NORTH, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y0, Direction.SOUTH, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y0, Direction.UP, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y0, Direction.WEST, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y90, Direction.EAST, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y90, Direction.NORTH, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y90, Direction.SOUTH, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y90, Direction.WEST, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y180, Direction.EAST, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y180, Direction.NORTH, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y180, Direction.SOUTH, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y180, Direction.WEST, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y270, Direction.EAST, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y270, Direction.NORTH, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y270, Direction.SOUTH, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y270, Direction.WEST, UV_ROTATION_0);
      addUvRotation(ModelRotation.X90_Y0, Direction.DOWN, UV_ROTATION_0);
      addUvRotation(ModelRotation.X90_Y0, Direction.SOUTH, UV_ROTATION_0);
      addUvRotation(ModelRotation.X90_Y90, Direction.DOWN, UV_ROTATION_0);
      addUvRotation(ModelRotation.X90_Y180, Direction.DOWN, UV_ROTATION_0);
      addUvRotation(ModelRotation.X90_Y180, Direction.NORTH, UV_ROTATION_0);
      addUvRotation(ModelRotation.X90_Y270, Direction.DOWN, UV_ROTATION_0);
      addUvRotation(ModelRotation.X180_Y0, Direction.DOWN, UV_ROTATION_0);
      addUvRotation(ModelRotation.X180_Y0, Direction.UP, UV_ROTATION_0);
      addUvRotation(ModelRotation.X270_Y0, Direction.SOUTH, UV_ROTATION_0);
      addUvRotation(ModelRotation.X270_Y0, Direction.UP, UV_ROTATION_0);
      addUvRotation(ModelRotation.X270_Y90, Direction.UP, UV_ROTATION_0);
      addUvRotation(ModelRotation.X270_Y180, Direction.NORTH, UV_ROTATION_0);
      addUvRotation(ModelRotation.X270_Y180, Direction.UP, UV_ROTATION_0);
      addUvRotation(ModelRotation.X270_Y270, Direction.UP, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y270, Direction.UP, UV_ROTATION_270);
      addUvRotation(ModelRotation.X0_Y90, Direction.DOWN, UV_ROTATION_270);
      addUvRotation(ModelRotation.X90_Y0, Direction.WEST, UV_ROTATION_270);
      addUvRotation(ModelRotation.X90_Y90, Direction.WEST, UV_ROTATION_270);
      addUvRotation(ModelRotation.X90_Y180, Direction.WEST, UV_ROTATION_270);
      addUvRotation(ModelRotation.X90_Y270, Direction.NORTH, UV_ROTATION_270);
      addUvRotation(ModelRotation.X90_Y270, Direction.SOUTH, UV_ROTATION_270);
      addUvRotation(ModelRotation.X90_Y270, Direction.WEST, UV_ROTATION_270);
      addUvRotation(ModelRotation.X180_Y90, Direction.UP, UV_ROTATION_270);
      addUvRotation(ModelRotation.X180_Y270, Direction.DOWN, UV_ROTATION_270);
      addUvRotation(ModelRotation.X270_Y0, Direction.EAST, UV_ROTATION_270);
      addUvRotation(ModelRotation.X270_Y90, Direction.EAST, UV_ROTATION_270);
      addUvRotation(ModelRotation.X270_Y90, Direction.NORTH, UV_ROTATION_270);
      addUvRotation(ModelRotation.X270_Y90, Direction.SOUTH, UV_ROTATION_270);
      addUvRotation(ModelRotation.X270_Y180, Direction.EAST, UV_ROTATION_270);
      addUvRotation(ModelRotation.X270_Y270, Direction.EAST, UV_ROTATION_270);
      addUvRotation(ModelRotation.X0_Y180, Direction.DOWN, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X0_Y180, Direction.UP, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X90_Y0, Direction.NORTH, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X90_Y0, Direction.UP, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X90_Y90, Direction.UP, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X90_Y180, Direction.SOUTH, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X90_Y180, Direction.UP, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X90_Y270, Direction.UP, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y0, Direction.EAST, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y0, Direction.NORTH, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y0, Direction.SOUTH, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y0, Direction.WEST, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y90, Direction.EAST, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y90, Direction.NORTH, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y90, Direction.SOUTH, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y90, Direction.WEST, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y180, Direction.DOWN, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y180, Direction.EAST, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y180, Direction.NORTH, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y180, Direction.SOUTH, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y180, Direction.UP, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y180, Direction.WEST, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y270, Direction.EAST, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y270, Direction.NORTH, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y270, Direction.SOUTH, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y270, Direction.WEST, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X270_Y0, Direction.DOWN, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X270_Y0, Direction.NORTH, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X270_Y90, Direction.DOWN, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X270_Y180, Direction.DOWN, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X270_Y180, Direction.SOUTH, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X270_Y270, Direction.DOWN, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X0_Y90, Direction.UP, UV_ROTATION_90);
      addUvRotation(ModelRotation.X0_Y270, Direction.DOWN, UV_ROTATION_90);
      addUvRotation(ModelRotation.X90_Y0, Direction.EAST, UV_ROTATION_90);
      addUvRotation(ModelRotation.X90_Y90, Direction.EAST, UV_ROTATION_90);
      addUvRotation(ModelRotation.X90_Y90, Direction.NORTH, UV_ROTATION_90);
      addUvRotation(ModelRotation.X90_Y90, Direction.SOUTH, UV_ROTATION_90);
      addUvRotation(ModelRotation.X90_Y180, Direction.EAST, UV_ROTATION_90);
      addUvRotation(ModelRotation.X90_Y270, Direction.EAST, UV_ROTATION_90);
      addUvRotation(ModelRotation.X270_Y0, Direction.WEST, UV_ROTATION_90);
      addUvRotation(ModelRotation.X180_Y90, Direction.DOWN, UV_ROTATION_90);
      addUvRotation(ModelRotation.X180_Y270, Direction.UP, UV_ROTATION_90);
      addUvRotation(ModelRotation.X270_Y90, Direction.WEST, UV_ROTATION_90);
      addUvRotation(ModelRotation.X270_Y180, Direction.WEST, UV_ROTATION_90);
      addUvRotation(ModelRotation.X270_Y270, Direction.NORTH, UV_ROTATION_90);
      addUvRotation(ModelRotation.X270_Y270, Direction.SOUTH, UV_ROTATION_90);
      addUvRotation(ModelRotation.X270_Y270, Direction.WEST, UV_ROTATION_90);
   }

   @OnlyIn(Dist.CLIENT)
   abstract static class Rotation {
      private Rotation() {
      }

      public BlockFaceUV rotateUV(BlockFaceUV blockFaceUVIn) {
         float f = blockFaceUVIn.getVertexU(blockFaceUVIn.getVertexRotatedRev(0));
         float f1 = blockFaceUVIn.getVertexV(blockFaceUVIn.getVertexRotatedRev(0));
         float f2 = blockFaceUVIn.getVertexU(blockFaceUVIn.getVertexRotatedRev(2));
         float f3 = blockFaceUVIn.getVertexV(blockFaceUVIn.getVertexRotatedRev(2));
         return this.makeRotatedUV(f, f1, f2, f3);
      }

      abstract BlockFaceUV makeRotatedUV(float u1, float v1, float u2, float v2);
   }
}