package net.fabricmc.example.mixin;

import com.google.common.base.Objects;
import com.sun.istack.internal.Nullable;
import net.fabricmc.example.ABC;
import net.fabricmc.example.ExampleMod;
import net.minecraft.advancement.criterion.Criterions;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.packet.MobSpawnS2CPacket;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.FrostWalkerEnchantment;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    protected void initDataTracker() {}
    @Shadow
    public void readCustomDataFromTag(CompoundTag compoundTag_1) {}
    @Shadow
    public void writeCustomDataToTag(CompoundTag compoundTag_1) {}
    @Shadow
    public Packet<?> createSpawnPacket() {
        return null;
    }
    @Shadow
    public EntityAttributeInstance getAttributeInstance(EntityAttribute entityAttribute_1) { return null; }

    public LivingEntityMixin(EntityType<?> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }



    @Shadow
    public float getHealth() { return 1.0f; }
    @Shadow
    public boolean hasStatusEffect(StatusEffect statusEffect_1) { return false; }
    @Shadow
    public boolean isSleeping() { return false; }
    @Shadow
    public void wakeUp() {}
    @Shadow
    protected int despawnCounter;
    @Shadow
    public void sendEquipmentBreakStatus(EquipmentSlot equipmentSlot_1) {}


    @Shadow
    private boolean method_6061(DamageSource damageSource_1) { return false; }
    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot var1);
    @Shadow
    protected void damageShield(float float_1) {}

    @Shadow
    protected void takeShieldHit(LivingEntity livingEntity_1) {}
    @Shadow
    public float limbDistance;
    @Shadow
    protected float field_6253;
    @Shadow
    protected void applyDamage(DamageSource damageSource_1, float float_1) {}
    @Shadow
    public int field_6254;
    @Shadow
    public int hurtTime;
    @Shadow
    public float field_6271;
    @Shadow
    public void takeKnockback(Entity entity_1, float float_1, double double_1, double double_2) {}
    @Shadow
    public void setAttacker(@Nullable LivingEntity livingEntity_1) {}
    @Shadow
    protected int playerHitTimer;
    @Shadow
    protected PlayerEntity attackingPlayer;
    @Shadow
    private boolean method_6095(DamageSource damageSource_1) { return false; }
    @Shadow
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_GENERIC_DEATH;
    }
    @Shadow
    protected float getSoundVolume() {
        return 1.0F;
    }
    @Shadow
    protected float getSoundPitch() { return 1.0f; }
    @Shadow
    public void onDeath(DamageSource damageSource_1) {}
    @Shadow
    private DamageSource field_6276;
    @Shadow
    protected void playHurtSound(DamageSource damageSource_1) {}
    @Shadow
    private long field_6226;
    @Shadow
    public float lastHandSwingProgress;
    @Shadow
    public float handSwingProgress;
    @Shadow
    public boolean canBreatheInWater() { return true; }
    @Shadow
    protected int getNextBreathInWater(int int_1) { return 0; }
    @Shadow
    protected int getNextBreathInAir(int int_1) { return 0; }
    @Shadow
    private BlockPos lastBlockPos;
    @Shadow
    protected void updatePostDeath() {}
    @Shadow
    private LivingEntity attacking;
    @Shadow
    private LivingEntity attacker;
    @Shadow
    private int lastAttackedTime;
    @Shadow
    protected float field_6275;
    @Shadow
    protected void spawnPotionParticles() {}
    @Shadow
    protected float field_6255;
    @Shadow
    public float field_6220;
    @Shadow
    public float field_6283;
    @Shadow
    public float headYaw;
    @Shadow
    public float prevHeadYaw;
    @Shadow
    protected ItemStack activeItemStack;
    @Shadow
    public int getItemUseTimeLeft() { return 1;}
    @Shadow
    protected void method_6076() {}
    @Shadow
    public void clearActiveItem() {}




        public boolean damage(DamageSource damageSource_1, float float_1) {
        if (this.isInvulnerableTo(damageSource_1)) {
            return false;
        } else if (this.world.isClient) {
            return false;
        } else if (this.getHealth() <= 0.0F) {
            return false;
        } else if (damageSource_1.isFire() && this.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {
            return false;
        } else {
            if (this.isSleeping() && !this.world.isClient) {
                this.wakeUp();
            }

            this.despawnCounter = 0;
            float float_2 = float_1;
            if ((damageSource_1 == DamageSource.ANVIL || damageSource_1 == DamageSource.FALLING_BLOCK) && !this.getEquippedStack(EquipmentSlot.HEAD).isEmpty()) {
                this.getEquippedStack(EquipmentSlot.HEAD).applyDamage((int)(float_1 * 4.0F + this.random.nextFloat() * float_1 * 2.0F), (LivingEntity)(Object)this, (livingEntity_1x) -> {
                    livingEntity_1x.sendEquipmentBreakStatus(EquipmentSlot.HEAD);
                });
                float_1 *= 0.75F;
            }

            boolean boolean_1 = false;
            float float_3 = 0.0F;
            if (float_1 > 0.0F && this.method_6061(damageSource_1)) {
                this.damageShield(float_1);
                float_3 = float_1;
                float_1 = 0.0F;
                if (!damageSource_1.isProjectile()) {
                    Entity entity_1 = damageSource_1.getSource();
                    if (entity_1 instanceof LivingEntity) {
                        this.takeShieldHit((LivingEntity)entity_1);
                    }
                }

                boolean_1 = true;
            }

            this.limbDistance = 1.5F;
            boolean boolean_2 = true;
            //Field_6008 is some sort of damage countdown.
            //If field_6008 is higher than 10, damage no count
            if ((float)this.field_6008 > 10.0F) {
                if (float_1 <= this.field_6253) {
                    return false;
                }

                this.applyDamage(damageSource_1, float_1 - this.field_6253);
                this.field_6253 = float_1;
                boolean_2 = false;
            } else {
                this.field_6253 = float_1;
                this.field_6008 = 0; //20 -> 0
                this.applyDamage(damageSource_1, float_1);
                //System.out.println(field_6254 + " is 6254");
                this.field_6254 = 0; //10 -> 0
                this.hurtTime = this.field_6254;
            }

            this.field_6271 = 0.0F;
            Entity entity_2 = damageSource_1.getAttacker();
            if (entity_2 != null) {
                if (entity_2 instanceof LivingEntity) {
                    this.setAttacker((LivingEntity)entity_2);
                }

                if (entity_2 instanceof PlayerEntity) {
                    this.playerHitTimer = 100;
                    this.attackingPlayer = (PlayerEntity)entity_2;
                } else if (entity_2 instanceof WolfEntity) {
                    WolfEntity wolfEntity_1 = (WolfEntity)entity_2;
                    if (wolfEntity_1.isTamed()) {
                        this.playerHitTimer = 100;
                        LivingEntity livingEntity_1 = wolfEntity_1.getOwner();
                        if (livingEntity_1 != null && livingEntity_1.getType() == EntityType.PLAYER) {
                            this.attackingPlayer = (PlayerEntity)livingEntity_1;
                        } else {
                            this.attackingPlayer = null;
                        }
                    }
                }
            }

            if (boolean_2) {
                if (boolean_1) {
                    this.world.sendEntityStatus(this, (byte)29);
                } else if (damageSource_1 instanceof EntityDamageSource && ((EntityDamageSource)damageSource_1).method_5549()) {
                    this.world.sendEntityStatus(this, (byte)33);
                } else {
                    byte byte_4;
                    if (damageSource_1 == DamageSource.DROWN) {
                        byte_4 = 36;
                    } else if (damageSource_1.isFire()) {
                        byte_4 = 37;
                    } else if (damageSource_1 == DamageSource.SWEET_BERRY_BUSH) {
                        byte_4 = 44;
                    } else {
                        byte_4 = 2;
                    }

                    this.world.sendEntityStatus(this, byte_4);
                }

                if (damageSource_1 != DamageSource.DROWN && (!boolean_1 || float_1 > 0.0F)) {
                    this.scheduleVelocityUpdate();
                }

                if (entity_2 != null) {
                    double double_1 = entity_2.x - this.x;

                    double double_2;
                    for(double_2 = entity_2.z - this.z; double_1 * double_1 + double_2 * double_2 < 1.0E-4D; double_2 = (Math.random() - Math.random()) * 0.01D) {
                        double_1 = (Math.random() - Math.random()) * 0.01D;
                    }

                    this.field_6271 = (float)(MathHelper.atan2(double_2, double_1) * 57.2957763671875D - (double)this.yaw);
                    String source = damageSource_1.getName();
                    if (damageSource_1.isExplosive()) {
                        this.takeKnockback(entity_2, 0.4F, double_1, double_2);
                    }
                } else {
                    this.field_6271 = (float)((int)(Math.random() * 2.0D) * 180);
                    if (damageSource_1.isExplosive()) {
                        this.takeKnockback(entity_2, 0.4F, this.x, this.z);
                    }
                }
            }

            if (this.getHealth() <= 0.0F) {
                if (!this.method_6095(damageSource_1)) {
                    SoundEvent soundEvent_1 = this.getDeathSound();
                    if (boolean_2 && soundEvent_1 != null) {
                        this.playSound(soundEvent_1, this.getSoundVolume(), this.getSoundPitch());
                    }

                    this.onDeath(damageSource_1);
                }
            } else if (boolean_2) {
                this.playHurtSound(damageSource_1);
            }

            boolean boolean_3 = !boolean_1 || float_1 > 0.0F;
            if (boolean_3) {
                this.field_6276 = damageSource_1;
                this.field_6226 = this.world.getTime();
            }

            if ((LivingEntity)(Object)this instanceof ServerPlayerEntity) {
                Criterions.ENTITY_HURT_PLAYER.handle((ServerPlayerEntity)(Object)this, damageSource_1, float_2, float_1, boolean_1);
                if (float_3 > 0.0F && float_3 < 3.4028235E37F) {
                    ((ServerPlayerEntity)(Object)this).increaseStat(Stats.DAMAGE_BLOCKED_BY_SHIELD, Math.round(float_3 * 10.0F));
                }
            }

            if (entity_2 instanceof ServerPlayerEntity) {
                Criterions.PLAYER_HURT_ENTITY.handle((ServerPlayerEntity)entity_2, this, damageSource_1, float_2, float_1, boolean_1);
            }

            return boolean_3;
        }
    }





    public void baseTick() {
        this.lastHandSwingProgress = this.handSwingProgress;
        super.baseTick();
        this.world.getProfiler().push("livingEntityBaseTick");
        boolean boolean_1 = (LivingEntity)(Object)this instanceof PlayerEntity;
        if (this.isAlive()) {
            if (this.isInsideWall()) {
                this.damage(DamageSource.IN_WALL, 1.0F);
            } else if (boolean_1 && !this.world.getWorldBorder().contains(this.getBoundingBox())) {
                double double_1 = this.world.getWorldBorder().contains(this) + this.world.getWorldBorder().getBuffer();
                if (double_1 < 0.0D) {
                    double double_2 = this.world.getWorldBorder().getDamagePerBlock();
                    if (double_2 > 0.0D) {
                        this.damage(DamageSource.IN_WALL, (float)Math.max(1, MathHelper.floor(-double_1 * double_2)));
                    }
                }
            }
        }

        if (this.isFireImmune() || this.world.isClient) {
            this.extinguish();
        }

        boolean boolean_2 = boolean_1 && ((PlayerEntity)(Object)this).abilities.invulnerable;
        if (this.isAlive()) {
            if (this.isInFluid(FluidTags.WATER) && this.world.getBlockState(new BlockPos(this.x, this.y + (double)this.getStandingEyeHeight(), this.z)).getBlock() != Blocks.BUBBLE_COLUMN) {
                if (!this.canBreatheInWater() && !StatusEffectUtil.hasWaterBreathing((LivingEntity)(Object)this) && !boolean_2) {
                    this.setBreath(this.getNextBreathInWater(this.getBreath()));
                    if (this.getBreath() == -20) {
                        this.setBreath(0);
                        Vec3d vec3d_1 = this.getVelocity();

                        for(int int_1 = 0; int_1 < 8; ++int_1) {
                            float float_1 = this.random.nextFloat() - this.random.nextFloat();
                            float float_2 = this.random.nextFloat() - this.random.nextFloat();
                            float float_3 = this.random.nextFloat() - this.random.nextFloat();
                            this.world.addParticle(ParticleTypes.BUBBLE, this.x + (double)float_1, this.y + (double)float_2, this.z + (double)float_3, vec3d_1.x, vec3d_1.y, vec3d_1.z);
                        }

                        this.damage(DamageSource.DROWN, 2.0F);
                    }
                }

                if (!this.world.isClient && this.hasVehicle() && this.getVehicle() != null && !this.getVehicle().canBeRiddenInWater()) {
                    this.stopRiding();
                }
            } else if (this.getBreath() < this.getMaxBreath()) {
                this.setBreath(this.getNextBreathInAir(this.getBreath()));
            }

            if (!this.world.isClient) {
                BlockPos blockPos_1 = new BlockPos(this);
                if (!Objects.equal(this.lastBlockPos, blockPos_1)) {
                    this.lastBlockPos = blockPos_1;
                    this.applyFrostWalker(blockPos_1);
                }
            }
        }

        if (this.isAlive() && this.isTouchingWater()) {
            this.extinguish();
        }
///*
        if (this.hurtTime > 0) {
            --this.hurtTime;
        }

        if (this.field_6008 > 0 && !((LivingEntity)(Object)this instanceof ServerPlayerEntity)) {
            --this.field_6008;
        }
//*/
        if (this.getHealth() <= 0.0F) {
            this.updatePostDeath();
        }

        if (this.playerHitTimer > 0) {
            --this.playerHitTimer;
        } else {
            this.attackingPlayer = null;
        }

        if (this.attacking != null && !this.attacking.isAlive()) {
            this.attacking = null;
        }

        if (this.attacker != null) {
            if (!this.attacker.isAlive()) {
                this.setAttacker((LivingEntity)null);
            } else if (this.age - this.lastAttackedTime > 100) {
                this.setAttacker((LivingEntity)null);
            }
        }

        this.spawnPotionParticles();
        this.field_6275 = this.field_6255;
        this.field_6220 = this.field_6283;
        this.prevHeadYaw = this.headYaw;
        this.prevYaw = this.yaw;
        this.prevPitch = this.pitch;
        this.world.getProfiler().pop();
    }

    public void applyFrostWalker(BlockPos blockPos_1) {
        int int_1 = EnchantmentHelper.getEquipmentLevel(Enchantments.FROST_WALKER, (LivingEntity)(Object)this);
        if (int_1 > 0) {
            FrostWalkerEnchantment.freezeWater((LivingEntity)(Object)this, this.world, blockPos_1, int_1);
        }

    }


    public void stopUsingItem() {
        //Item activeItem = this.activeItemStack.getItem();
        if (!this.activeItemStack.isEmpty()) {
            /*
            if ((activeItem == ExampleMod.ABC)) {
                CompoundTag compoundTag_1 = activeItemStack.getTag();
                if (compoundTag_1 != null && !compoundTag_1.getBoolean("Charged")) {
                    return;
                }
            }
            */

            this.activeItemStack.onItemStopUsing(this.world, (LivingEntity)(Object)this, this.getItemUseTimeLeft());
            if (this.activeItemStack.method_7967()) {
                this.method_6076();
            }
        }

        this.clearActiveItem();
    }



    /*
    public void takeKnockback(Entity entity_1, float float_1, double double_1, double double_2) {
        System.out.println("KNOOCK");
        if (this.random.nextDouble() >= this.getAttributeInstance(EntityAttributes.KNOCKBACK_RESISTANCE).getValue()) {
            this.velocityDirty = true;
            Vec3d vec3d_1 = this.getVelocity();
            Vec3d vec3d_2 = (new Vec3d(double_1, 0.0D, double_2)).normalize().multiply((double)float_1);
            this.setVelocity(vec3d_1.x / 2.0D - vec3d_2.x, this.onGround ? Math.min(0.4D, vec3d_1.y / 2.0D + (double)float_1) : vec3d_1.y, vec3d_1.z / 2.0D - vec3d_2.z);
        }


    }
    */
/*
    @Overwrite
    public void knockback(LivingEntity livingEntity_1) {
        System.out.println(this.x + " is x !!!");
        livingEntity_1.takeKnockback((LivingEntity)(Object)this, 100.0F, livingEntity_1.x - this.x, livingEntity_1.z - this.z);
    }
    */
}

//@Inject(method = "knockback", at = @At("HEAD"), cancellable = true)

/*
*/