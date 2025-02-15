package net.minecraft.util;

import net.minecraft.client.GameSettings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MovementInputFromOptions extends MovementInput {
   private final GameSettings gameSettings;

   public MovementInputFromOptions(GameSettings gameSettingsIn) {
      this.gameSettings = gameSettingsIn;
   }

   public void tick(boolean slow, boolean noDampening) {
      this.forwardKeyDown = this.gameSettings.keyBindForward.isKeyDown();
      this.backKeyDown = this.gameSettings.keyBindBack.isKeyDown();
      this.leftKeyDown = this.gameSettings.keyBindLeft.isKeyDown();
      this.rightKeyDown = this.gameSettings.keyBindRight.isKeyDown();
      this.moveForward = this.forwardKeyDown == this.backKeyDown ? 0.0F : (float)(this.forwardKeyDown ? 1 : -1);
      this.moveStrafe = this.leftKeyDown == this.rightKeyDown ? 0.0F : (float)(this.leftKeyDown ? 1 : -1);
      this.jump = this.gameSettings.keyBindJump.isKeyDown();
      this.sneak = this.gameSettings.keyBindSneak.isKeyDown();
      if (!noDampening && (this.sneak || slow)) {
         this.moveStrafe = (float)((double)this.moveStrafe * 0.3D);
         this.moveForward = (float)((double)this.moveForward * 0.3D);
      }

   }
}