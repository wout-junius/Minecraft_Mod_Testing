package com.wout.firstmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BeefBlock extends Block {
    public BeefBlock() {
        super(Properties.create(Material.CAKE)
                .sound(SoundType.SLIME)
                .hardnessAndResistance(2.0f)
                .lightValue(14)
        );
        setRegistryName("beefblock");
    }
}
