package net.minecraft.state;

import com.google.common.collect.ImmutableMap;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface IStateHolder<C> {
   Logger field_215672_b = LogManager.getLogger();

   /**
    * Get the value of the given Property for this BlockState
    */
   <T extends Comparable<T>> T get(IProperty<T> property);

   <T extends Comparable<T>, V extends T> C with(IProperty<T> property, V value);

   ImmutableMap<IProperty<?>, Comparable<?>> getValues();

   static <T extends Comparable<T>> String writePropertyValueToString(IProperty<T> propertyIn, Comparable<?> value) {
      return propertyIn.getName((T)value);
   }

   static <S extends IStateHolder<S>, T extends Comparable<T>> S func_215671_a(S state, IProperty<T> p_215671_1_, String p_215671_2_, String p_215671_3_, String p_215671_4_) {
      Optional<T> optional = p_215671_1_.parseValue(p_215671_4_);
      if (optional.isPresent()) {
         return (S)(state.with(p_215671_1_, (T)(optional.get())));
      } else {
         field_215672_b.warn("Unable to read property: {} with value: {} for input: {}", p_215671_2_, p_215671_4_, p_215671_3_);
         return state;
      }
   }
}