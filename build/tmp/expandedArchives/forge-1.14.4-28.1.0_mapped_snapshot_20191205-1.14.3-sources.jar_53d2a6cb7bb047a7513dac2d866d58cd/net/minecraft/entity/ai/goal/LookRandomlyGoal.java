package net.minecraft.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.entity.MobEntity;

public class LookRandomlyGoal extends Goal {
   private final MobEntity field_75258_a;
   private double lookX;
   private double lookZ;
   private int idleTime;

   public LookRandomlyGoal(MobEntity entitylivingIn) {
      this.field_75258_a = entitylivingIn;
      this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
   }

   /**
    * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
    * method as well.
    */
   public boolean shouldExecute() {
      return this.field_75258_a.getRNG().nextFloat() < 0.02F;
   }

   /**
    * Returns whether an in-progress EntityAIBase should continue executing
    */
   public boolean shouldContinueExecuting() {
      return this.idleTime >= 0;
   }

   /**
    * Execute a one shot task or start executing a continuous task
    */
   public void startExecuting() {
      double d0 = (Math.PI * 2D) * this.field_75258_a.getRNG().nextDouble();
      this.lookX = Math.cos(d0);
      this.lookZ = Math.sin(d0);
      this.idleTime = 20 + this.field_75258_a.getRNG().nextInt(20);
   }

   /**
    * Keep ticking a continuous task that has already been started
    */
   public void tick() {
      --this.idleTime;
      this.field_75258_a.getLookController().setLookPosition(this.field_75258_a.posX + this.lookX, this.field_75258_a.posY + (double)this.field_75258_a.getEyeHeight(), this.field_75258_a.posZ + this.lookZ);
   }
}