package net.minecraft.client.renderer.vertex;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@OnlyIn(Dist.CLIENT)
public class VertexFormatElement {
   private static final Logger LOGGER = LogManager.getLogger();
   private final VertexFormatElement.Type type;
   private final VertexFormatElement.Usage usage;
   private final int index;
   private final int elementCount;

   public VertexFormatElement(int indexIn, VertexFormatElement.Type typeIn, VertexFormatElement.Usage usageIn, int count) {
      if (this.isFirstOrUV(indexIn, usageIn)) {
         this.usage = usageIn;
      } else {
         LOGGER.warn("Multiple vertex elements of the same type other than UVs are not supported. Forcing type to UV.");
         this.usage = VertexFormatElement.Usage.UV;
      }

      this.type = typeIn;
      this.index = indexIn;
      this.elementCount = count;
   }

   private final boolean isFirstOrUV(int p_177372_1_, VertexFormatElement.Usage p_177372_2_) {
      return p_177372_1_ == 0 || p_177372_2_ == VertexFormatElement.Usage.UV;
   }

   public final VertexFormatElement.Type getType() {
      return this.type;
   }

   public final VertexFormatElement.Usage getUsage() {
      return this.usage;
   }

   public final int getElementCount() {
      return this.elementCount;
   }

   public final int getIndex() {
      return this.index;
   }

   public String toString() {
      return this.elementCount + "," + this.usage.getDisplayName() + "," + this.type.getDisplayName();
   }

   public final int getSize() {
      return this.type.getSize() * this.elementCount;
   }

   public final boolean isPositionElement() {
      return this.usage == VertexFormatElement.Usage.POSITION;
   }

   public boolean equals(Object p_equals_1_) {
      if (this == p_equals_1_) {
         return true;
      } else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
         VertexFormatElement vertexformatelement = (VertexFormatElement)p_equals_1_;
         if (this.elementCount != vertexformatelement.elementCount) {
            return false;
         } else if (this.index != vertexformatelement.index) {
            return false;
         } else if (this.type != vertexformatelement.type) {
            return false;
         } else {
            return this.usage == vertexformatelement.usage;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int i = this.type.hashCode();
      i = 31 * i + this.usage.hashCode();
      i = 31 * i + this.index;
      i = 31 * i + this.elementCount;
      return i;
   }

   @OnlyIn(Dist.CLIENT)
   public static enum Type {
      FLOAT(4, "Float", 5126),
      UBYTE(1, "Unsigned Byte", 5121),
      BYTE(1, "Byte", 5120),
      USHORT(2, "Unsigned Short", 5123),
      SHORT(2, "Short", 5122),
      UINT(4, "Unsigned Int", 5125),
      INT(4, "Int", 5124);

      private final int size;
      private final String displayName;
      private final int glConstant;

      private Type(int sizeIn, String displayNameIn, int glConstantIn) {
         this.size = sizeIn;
         this.displayName = displayNameIn;
         this.glConstant = glConstantIn;
      }

      public int getSize() {
         return this.size;
      }

      public String getDisplayName() {
         return this.displayName;
      }

      public int getGlConstant() {
         return this.glConstant;
      }
   }

   @OnlyIn(Dist.CLIENT)
   public static enum Usage {
      POSITION("Position"),
      NORMAL("Normal"),
      COLOR("Vertex Color"),
      UV("UV"),
      // As of 1.8.8 - unused in vanilla; use GENERIC for now
      @Deprecated
      MATRIX("Bone Matrix"),
      @Deprecated
      BLEND_WEIGHT("Blend Weight"),
      PADDING("Padding"),
      GENERIC("Generic");

      public void preDraw(VertexFormat format, int element, int stride, java.nio.ByteBuffer buffer) { net.minecraftforge.client.ForgeHooksClient.preDraw(this, format, element, stride, buffer); }
      public void postDraw(VertexFormat format, int element, int stride, java.nio.ByteBuffer buffer) { net.minecraftforge.client.ForgeHooksClient.postDraw(this, format, element, stride, buffer); }

      private final String displayName;

      private Usage(String displayNameIn) {
         this.displayName = displayNameIn;
      }

      public String getDisplayName() {
         return this.displayName;
      }
   }
}