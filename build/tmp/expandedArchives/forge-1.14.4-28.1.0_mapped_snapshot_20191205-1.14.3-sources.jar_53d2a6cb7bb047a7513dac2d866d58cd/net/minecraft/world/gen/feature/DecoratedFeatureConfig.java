package net.minecraft.world.gen.feature;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

public class DecoratedFeatureConfig implements IFeatureConfig {
   public final ConfiguredFeature<?> feature;
   public final ConfiguredPlacement<?> decorator;

   public DecoratedFeatureConfig(ConfiguredFeature<?> feature, ConfiguredPlacement<?> decorator) {
      this.feature = feature;
      this.decorator = decorator;
   }

   public <F extends IFeatureConfig, D extends IPlacementConfig> DecoratedFeatureConfig(Feature<F> p_i49892_1_, F p_i49892_2_, Placement<D> p_i49892_3_, D p_i49892_4_) {
      this(new ConfiguredFeature<>(p_i49892_1_, p_i49892_2_), new ConfiguredPlacement<>(p_i49892_3_, p_i49892_4_));
   }

   public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
      return new Dynamic<>(ops, ops.createMap(ImmutableMap.of(ops.createString("feature"), this.feature.serialize(ops).getValue(), ops.createString("decorator"), this.decorator.serialize(ops).getValue())));
   }

   public String toString() {
      return String.format("< %s [%s | %s] >", this.getClass().getSimpleName(), Registry.FEATURE.getKey(this.feature.feature), Registry.DECORATOR.getKey(this.decorator.decorator));
   }

   public static <T> DecoratedFeatureConfig deserialize(Dynamic<T> p_214688_0_) {
      ConfiguredFeature<?> configuredfeature = ConfiguredFeature.deserialize(p_214688_0_.get("feature").orElseEmptyMap());
      ConfiguredPlacement<?> configuredplacement = ConfiguredPlacement.deserialize(p_214688_0_.get("decorator").orElseEmptyMap());
      return new DecoratedFeatureConfig(configuredfeature, configuredplacement);
   }
}