package net.minecraft.advancements.criterion;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class PlayerHurtEntityTrigger implements ICriterionTrigger<PlayerHurtEntityTrigger.Instance> {
   private static final ResourceLocation ID = new ResourceLocation("player_hurt_entity");
   private final Map<PlayerAdvancements, PlayerHurtEntityTrigger.Listeners> listeners = Maps.newHashMap();

   public ResourceLocation getId() {
      return ID;
   }

   public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<PlayerHurtEntityTrigger.Instance> listener) {
      PlayerHurtEntityTrigger.Listeners playerhurtentitytrigger$listeners = this.listeners.get(playerAdvancementsIn);
      if (playerhurtentitytrigger$listeners == null) {
         playerhurtentitytrigger$listeners = new PlayerHurtEntityTrigger.Listeners(playerAdvancementsIn);
         this.listeners.put(playerAdvancementsIn, playerhurtentitytrigger$listeners);
      }

      playerhurtentitytrigger$listeners.add(listener);
   }

   public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<PlayerHurtEntityTrigger.Instance> listener) {
      PlayerHurtEntityTrigger.Listeners playerhurtentitytrigger$listeners = this.listeners.get(playerAdvancementsIn);
      if (playerhurtentitytrigger$listeners != null) {
         playerhurtentitytrigger$listeners.remove(listener);
         if (playerhurtentitytrigger$listeners.isEmpty()) {
            this.listeners.remove(playerAdvancementsIn);
         }
      }

   }

   public void removeAllListeners(PlayerAdvancements playerAdvancementsIn) {
      this.listeners.remove(playerAdvancementsIn);
   }

   public PlayerHurtEntityTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
      DamagePredicate damagepredicate = DamagePredicate.deserialize(json.get("damage"));
      EntityPredicate entitypredicate = EntityPredicate.deserialize(json.get("entity"));
      return new PlayerHurtEntityTrigger.Instance(damagepredicate, entitypredicate);
   }

   public void trigger(ServerPlayerEntity player, Entity entityIn, DamageSource source, float amountDealt, float amountTaken, boolean blocked) {
      PlayerHurtEntityTrigger.Listeners playerhurtentitytrigger$listeners = this.listeners.get(player.getAdvancements());
      if (playerhurtentitytrigger$listeners != null) {
         playerhurtentitytrigger$listeners.trigger(player, entityIn, source, amountDealt, amountTaken, blocked);
      }

   }

   public static class Instance extends CriterionInstance {
      private final DamagePredicate damage;
      private final EntityPredicate entity;

      public Instance(DamagePredicate damage, EntityPredicate entity) {
         super(PlayerHurtEntityTrigger.ID);
         this.damage = damage;
         this.entity = entity;
      }

      public static PlayerHurtEntityTrigger.Instance forDamage(DamagePredicate.Builder p_203936_0_) {
         return new PlayerHurtEntityTrigger.Instance(p_203936_0_.build(), EntityPredicate.ANY);
      }

      public boolean test(ServerPlayerEntity player, Entity entity, DamageSource source, float dealt, float taken, boolean blocked) {
         if (!this.damage.test(player, source, dealt, taken, blocked)) {
            return false;
         } else {
            return this.entity.test(player, entity);
         }
      }

      public JsonElement serialize() {
         JsonObject jsonobject = new JsonObject();
         jsonobject.add("damage", this.damage.serialize());
         jsonobject.add("entity", this.entity.serialize());
         return jsonobject;
      }
   }

   static class Listeners {
      private final PlayerAdvancements playerAdvancements;
      private final Set<ICriterionTrigger.Listener<PlayerHurtEntityTrigger.Instance>> listeners = Sets.newHashSet();

      public Listeners(PlayerAdvancements playerAdvancementsIn) {
         this.playerAdvancements = playerAdvancementsIn;
      }

      public boolean isEmpty() {
         return this.listeners.isEmpty();
      }

      public void add(ICriterionTrigger.Listener<PlayerHurtEntityTrigger.Instance> listener) {
         this.listeners.add(listener);
      }

      public void remove(ICriterionTrigger.Listener<PlayerHurtEntityTrigger.Instance> listener) {
         this.listeners.remove(listener);
      }

      public void trigger(ServerPlayerEntity player, Entity entity, DamageSource source, float dealt, float taken, boolean blocked) {
         List<ICriterionTrigger.Listener<PlayerHurtEntityTrigger.Instance>> list = null;

         for(ICriterionTrigger.Listener<PlayerHurtEntityTrigger.Instance> listener : this.listeners) {
            if (listener.getCriterionInstance().test(player, entity, source, dealt, taken, blocked)) {
               if (list == null) {
                  list = Lists.newArrayList();
               }

               list.add(listener);
            }
         }

         if (list != null) {
            for(ICriterionTrigger.Listener<PlayerHurtEntityTrigger.Instance> listener1 : list) {
               listener1.grantCriterion(this.playerAdvancements);
            }
         }

      }
   }
}