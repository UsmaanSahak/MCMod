package net.fabricmc.example.mixin;

import net.minecraft.client.network.packet.EntitySpawnS2CPacket;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Packet;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.List;

@Mixin(FireworkEntity.class)
public class FireworkEntityMixin extends Entity {

    public FireworkEntityMixin(EntityType<?> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    @Shadow
    protected void initDataTracker() {}
    @Shadow
    public void readCustomDataFromTag(CompoundTag compoundTag_1) {}
    @Shadow
    public void writeCustomDataToTag(CompoundTag compoundTag_1) {}
    @Shadow
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }
    @Shadow @Final
    private static  TrackedData<ItemStack> ITEM;
    @Shadow
    private LivingEntity shooter;

    @Overwrite
    private void explode() {
        System.out.println("explody");
        this.world.createExplosion(this, this.x, this.y + (double)(this.getHeight() / 16.0F), this.z, 4.0F, Explosion.DestructionType.BREAK);
        /*
        float float_1 = 0.0F;
        ItemStack itemStack_1 = (ItemStack)this.dataTracker.get(ITEM);


        CompoundTag compoundTag_1 = itemStack_1.isEmpty() ? null : itemStack_1.getSubCompoundTag("Fireworks");
        ListTag listTag_1 = compoundTag_1 != null ? compoundTag_1.getList("Explosions", 10) : null;
        if (listTag_1 != null && !listTag_1.isEmpty()) {
            float_1 = 5.0F + (float)(listTag_1.size() * 30);
        }

        if (float_1 > 0.0F) { //If has damage, because has explosion
            if (this.shooter != null) {
                this.shooter.damage(DamageSource.FIREWORKS, 5.0F + (float)(listTag_1.size() * 2));
            }

            double double_1 = 5.0D;
            Vec3d vec3d_1 = new Vec3d(this.x, this.y, this.z);
            World world_1 = this.world;

            PrimedTntEntity primedTntEntity_1 = new PrimedTntEntity(world_1, (double)(this.x + 0.5F), (double)this.y, (double)(this.z + 0.5F), null);
            world_1.spawnEntity(primedTntEntity_1);
            world_1.playSound((PlayerEntity)null, primedTntEntity_1.x, primedTntEntity_1.y, primedTntEntity_1.z, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);


            List<LivingEntity> list_1 = this.world.getEntities(LivingEntity.class, this.getBoundingBox().expand(2.0D));
            Iterator var9 = list_1.iterator();

            while(true) {
                LivingEntity livingEntity_1;

                do {
                    do {
                        if (!var9.hasNext()) {
                            return;
                        }

                        livingEntity_1 = (LivingEntity)var9.next();
                    } while(livingEntity_1 == this.shooter);
                } while(this.squaredDistanceTo(livingEntity_1) > 25.0D);

                boolean boolean_1 = false;

                for(int int_1 = 0; int_1 < 2; ++int_1) {
                    Vec3d vec3d_2 = new Vec3d(livingEntity_1.x, livingEntity_1.y + (double)livingEntity_1.getHeight() * 0.5D * (double)int_1, livingEntity_1.z);
                    HitResult hitResult_1 = this.world.rayTrace(new RayTraceContext(vec3d_1, vec3d_2, RayTraceContext.ShapeType.COLLIDER, RayTraceContext.FluidHandling.NONE, this));
                    if (hitResult_1.getType() == HitResult.Type.MISS) {
                        boolean_1 = true;
                        break;
                    }
                }

                if (boolean_1) {
                    float float_2 = float_1 * (float)Math.sqrt((5.0D - (double)this.distanceTo(livingEntity_1)) / 5.0D);
                    livingEntity_1.damage(DamageSource.FIREWORKS, float_2);
                }
            }

        }

         */
    }

}
