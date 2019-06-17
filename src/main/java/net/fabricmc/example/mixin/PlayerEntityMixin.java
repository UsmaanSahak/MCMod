package net.fabricmc.example.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Shadow @Final
    public PlayerInventory inventory;

    @Shadow @Final
    public PlayerAbilities abilities;

    @Shadow
    protected void dropShoulderEntities() {}


    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    /*
    @Shadow
    public ItemEntity dropSelectedItem(boolean boolean_1) { return null;}
    */

    /*
    @Inject(at = @At("HEAD"), method="damage")
    public void damage(DamageSource damageSource_1, float float_1, CallbackInfoReturnable info) {
        System.out.println(damageSource_1.getName() + " is the damage source from playerEntity");
        if (damageSource_1.name == "fireworks") {
            float_1 = 0;
            System.out.println(float_1 + " is float_1.");

            //System.out.println(this.dropSelectedItem(true));

        }

    }
    */


    public boolean damage(DamageSource damageSource_1, float float_1) {
        if (this.isInvulnerableTo(damageSource_1)) {
            return false;
        } else if (this.abilities.invulnerable && !damageSource_1.doesDamageToCreative()) {
            return false;
        } else {
            this.despawnCounter = 0;
            if (this.getHealth() <= 0.0F) {
                return false;
            } else {
                this.dropShoulderEntities();
                if (damageSource_1.isScaledWithDifficulty()) {
                    if (this.world.getDifficulty() == Difficulty.PEACEFUL) {
                        float_1 = 0.0F;
                    }

                    if (this.world.getDifficulty() == Difficulty.EASY) {
                        float_1 = Math.min(float_1 / 2.0F + 1.0F, float_1);
                    }

                    if (this.world.getDifficulty() == Difficulty.HARD) {
                        float_1 = float_1 * 3.0F / 2.0F;
                    }
                }

                if (damageSource_1.name == "fireworks") {
                    float_1 = float_1 * 0.2F;
                    System.out.println(float_1 + " is float_1.");
                }

                return float_1 == 0.0F ? false : super.damage(damageSource_1, float_1);
            }
        }
    }
}
