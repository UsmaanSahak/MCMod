package net.fabricmc.example.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HungerManager.class)
public class HungerManagerMixin {
    public void update(PlayerEntity playerEntity_1) {
    }
}
