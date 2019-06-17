package net.fabricmc.example;

import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class CookieCreeperEntity extends CreeperEntity {
    public CookieCreeperEntity(EntityType<? extends CreeperEntity> type, World world1) {
        super(type,world1);

    }

}
