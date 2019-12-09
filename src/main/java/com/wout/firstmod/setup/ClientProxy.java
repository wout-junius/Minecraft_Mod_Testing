package com.wout.firstmod.setup;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

public class ClientProxy implements IProxy {

    public World getClientWorld(){
        return Minecraft.getInstance().world;
    }
}
