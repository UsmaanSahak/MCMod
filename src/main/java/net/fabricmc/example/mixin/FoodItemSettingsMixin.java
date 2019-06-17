package net.fabricmc.example.mixin;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodItemSetting;
import net.minecraft.item.FoodItemSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(FoodItemSettings.class)
public class FoodItemSettingsMixin {
    @Shadow @Final
    public static FoodItemSetting ENCHANTED_GOLDEN_APPLE;
    static {
        ENCHANTED_GOLDEN_APPLE = (new FoodItemSetting.Builder()).hunger(4).saturationModifier(1.2F).statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 5000, 4), 1.0F).alwaysEdible().build();
    }
}
