package com.wout.firstmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class CookedBeefBlock extends Block {
    public CookedBeefBlock() {
        super(Properties.create(Material.CLAY)
                .sound(SoundType.CLOTH)
                .hardnessAndResistance(2.0f)
                .lightValue(14)
        );
        setRegistryName("cookedbeefblock");
    }
}
