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
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;

public class CuredZombieVillagerTrigger implements ICriterionTrigger<CuredZombieVillagerTrigger.Instance> {
   private static final ResourceLocation ID = new ResourceLocation("cured_zombie_villager");
   private final Map<PlayerAdvancements, CuredZombieVillagerTrigger.Listeners> listeners = Maps.newHashMap();

   public ResourceLocation getId() {
      return ID;
   }

   public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<CuredZombieVillagerTrigger.Instance> listener) {
      CuredZombieVillagerTrigger.Listeners curedzombievillagertrigger$listeners = this.listeners.get(playerAdvancementsIn);
      if (curedzombievillagertrigger$listeners == null) {
         curedzombievillagertrigger$listeners = new CuredZombieVillagerTrigger.Listeners(playerAdvancementsIn);
         this.listeners.put(playerAdvancementsIn, curedzombievillagertrigger$listeners);
      }

      curedzombievillagertrigger$listeners.add(listener);
   }

   public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<CuredZombieVillagerTrigger.Instance> listener) {
      CuredZombieVillagerTrigger.Listeners curedzombievillagertrigger$listeners = this.listeners.get(playerAdvancementsIn);
      if (curedzombievillagertrigger$listeners != null) {
         curedzombievillagertrigger$listeners.remove(listener);
         if (curedzombievillagertrigger$listeners.isEmpty()) {
            this.listeners.remove(playerAdvancementsIn);
         }
      }

   }

   public void removeAllListeners(PlayerAdvancements playerAdvancementsIn) {
      this.listeners.remove(playerAdvancementsIn);
   }

   public CuredZombieVillagerTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
      EntityPredicate entitypredicate = EntityPredicate.deserialize(json.get("zombie"));
      EntityPredicate entitypredicate1 = EntityPredicate.deserialize(json.get("villager"));
      return new CuredZombieVillagerTrigger.Instance(entitypredicate, entitypredicate1);
   }

   public void trigger(ServerPlayerEntity player, ZombieEntity zombie, VillagerEntity villager) {
      CuredZombieVillagerTrigger.Listeners curedzombievillagertrigger$listeners = this.listeners.get(player.getAdvancements());
      if (curedzombievillagertrigger$listeners != null) {
         curedzombievillagertrigger$listeners.trigger(player, zombie, villager);
      }

   }

   public static class Instance extends CriterionInstance {
      private final EntityPredicate zombie;
      private final EntityPredicate villager;

      public Instance(EntityPredicate zombie, EntityPredicate villager) {
         super(CuredZombieVillagerTrigger.ID);
         this.zombie = zombie;
         this.villager = villager;
      }

      public static CuredZombieVillagerTrigger.Instance any() {
         return new CuredZombieVillagerTrigger.Instance(EntityPredicate.ANY, EntityPredicate.ANY);
      }

      public boolean test(ServerPlayerEntity player, ZombieEntity zombie, VillagerEntity villager) {
         if (!this.zombie.test(player, zombie)) {
            return false;
         } else {
            return this.villager.test(player, villager);
         }
      }

      public JsonElement serialize() {
         JsonObject jsonobject = new JsonObject();
         jsonobject.add("zombie", this.zombie.serialize());
         jsonobject.add("villager", this.villager.serialize());
         return jsonobject;
      }
   }

   static class Listeners {
      private final PlayerAdvancements playerAdvancements;
      private final Set<ICriterionTrigger.Listener<CuredZombieVillagerTrigger.Instance>> listeners = Sets.newHashSet();

      public Listeners(PlayerAdvancements playerAdvancementsIn) {
         this.playerAdvancements = playerAdvancementsIn;
      }

      public boolean isEmpty() {
         return this.listeners.isEmpty();
      }

      public void add(ICriterionTrigger.Listener<CuredZombieVillagerTrigger.Instance> listener) {
         this.listeners.add(listener);
      }

      public void remove(ICriterionTrigger.Listener<CuredZombieVillagerTrigger.Instance> listener) {
         this.listeners.remove(listener);
      }

      public void trigger(ServerPlayerEntity player, ZombieEntity zombie, VillagerEntity villager) {
         List<ICriterionTrigger.Listener<CuredZombieVillagerTrigger.Instance>> list = null;

         for(ICriterionTrigger.Listener<CuredZombieVillagerTrigger.Instance> listener : this.listeners) {
            if (listener.getCriterionInstance().test(player, zombie, villager)) {
               if (list == null) {
                  list = Lists.newArrayList();
               }

               list.add(listener);
            }
         }

         if (list != null) {
            for(ICriterionTrigger.Listener<CuredZombieVillagerTrigger.Instance> listener1 : list) {
               listener1.grantCriterion(this.playerAdvancements);
            }
         }

      }
   }
}