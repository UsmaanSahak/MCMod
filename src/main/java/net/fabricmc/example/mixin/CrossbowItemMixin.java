package net.fabricmc.example.mixin;

import net.minecraft.client.util.math.Quaternion;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.Projectile;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

import static net.minecraft.item.CrossbowItem.isCharged;
import static net.minecraft.item.CrossbowItem.setCharged;

/*
@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {
    @Inject(method = "isCharged", at = @At("HEAD"), cancellable = true)
    private static void isCharged(CallbackInfoReturnable<Boolean> info) {
        info.setReturnValue(true);
    }
}
*/
@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {

    @Shadow
    private boolean field_7937 = false;
    @Shadow
    private boolean field_7936 = false;
    @Shadow
    public static void shootAllProjectiles(World world_1, LivingEntity livingEntity_1, Hand hand_1, ItemStack itemStack_1, float float_1, float float_2) {}

    @Shadow
    private static float method_20309(ItemStack itemStack_1) {return -1;}

    @Shadow
    private static void storeChargedProjectile(ItemStack itemStack_1, ItemStack itemStack_2) {}
    @Shadow
    private static ProjectileEntity method_18814(World world_1, LivingEntity livingEntity_1, ItemStack itemStack_1, ItemStack itemStack_2) {return null;}


    public ItemStack ROCKET;

    @Inject(at = @At("RETURN"), method="<init>*")
    private void constructor(Item.Settings item$Settings_1, CallbackInfo info) {
        ListTag detailsListTag = new ListTag();
        CompoundTag Type = new CompoundTag();
        CompoundTag Flicker = new CompoundTag();
        CompoundTag Trail = new CompoundTag();


        Type.putInt("Type",0);
        Flicker.putInt("Flicker",0);
        Trail.putInt("Trail",0);
        detailsListTag.add(Type);
        detailsListTag.add(Flicker);
        detailsListTag.add(Trail);
        CompoundTag explosionsTag = new CompoundTag();
        explosionsTag.put("Explosions",detailsListTag);
        explosionsTag.putInt("Flight",3);
        CompoundTag fireworksTag = new CompoundTag();
        fireworksTag.put("Fireworks",explosionsTag);
        ROCKET =  new ItemStack(Items.FIREWORK_ROCKET);
        ROCKET.setTag(fireworksTag);
    }

///*

    @Overwrite
    public static boolean isCharged(ItemStack itemStack_1) {
        CompoundTag compoundTag_1 = itemStack_1.getTag();
        return compoundTag_1 != null && compoundTag_1.getBoolean("Charged");
    }

/*
    public TypedActionResult<ItemStack> use(World world_1, PlayerEntity playerEntity_1, Hand hand_1) {
        System.out.println("is using reg!");
        ItemStack itemStack_1 = playerEntity_1.getStackInHand(hand_1);
        if (isCharged(itemStack_1)) {
            shootAllProjectiles(world_1, playerEntity_1, hand_1, itemStack_1, method_20309(itemStack_1), 1.0F);

            storeChargedProjectile(itemStack_1,ROCKET);
            ///*
            //storeChargedProjectile(itemStack_1, new ItemStack(Items.FIREWORK_ROCKET));
            return new TypedActionResult(ActionResult.SUCCESS, itemStack_1);
        } else if (!playerEntity_1.getArrowType(itemStack_1).isEmpty()) {
            if (!isCharged(itemStack_1)) {
                this.field_7937 = false;
                this.field_7936 = false;
                playerEntity_1.setCurrentHand(hand_1);
            }

            return new TypedActionResult(ActionResult.SUCCESS, itemStack_1);
        } else {
            return new TypedActionResult(ActionResult.FAIL, itemStack_1);
        }
    }


    private static void shoot(World world_1, LivingEntity livingEntity_1, Hand hand_1, ItemStack itemStack_1, ItemStack itemStack_2, float float_1, boolean boolean_1, float float_2, float float_3, float float_4) {
        if (!world_1.isClient) {
            boolean boolean_2 = itemStack_2.getItem() == Items.FIREWORK_ROCKET;
            Object projectile_2;
            if (boolean_2) {
                projectile_2 = new FireworkEntity(world_1, itemStack_2, livingEntity_1.x, livingEntity_1.y + (double)livingEntity_1.getStandingEyeHeight() - 0.15000000596046448D, livingEntity_1.z, true);
            } else {
                projectile_2 = method_18814(world_1, livingEntity_1, itemStack_1, itemStack_2);
                if (boolean_1 || float_4 != 0.0F) {
                    ((ProjectileEntity)projectile_2).pickupType = ProjectileEntity.PickupPermission.CREATIVE_ONLY;
                }
            }

            if (livingEntity_1 instanceof CrossbowUser) {
                CrossbowUser crossbowUser_1 = (CrossbowUser)livingEntity_1;
                crossbowUser_1.shoot(crossbowUser_1.getTarget(), itemStack_1, (Projectile)projectile_2, float_4);
            } else {
                Vec3d vec3d_1 = livingEntity_1.getOppositeRotationVector(1.0F);
                Quaternion quaternion_1 = new Quaternion(new Vector3f(vec3d_1), float_4, true);
                Vec3d vec3d_2 = livingEntity_1.getRotationVec(1.0F);
                Vector3f vector3f_1 = new Vector3f(vec3d_2);
                vector3f_1.method_19262(quaternion_1);
                ((Projectile)projectile_2).setVelocity((double)vector3f_1.x(), (double)vector3f_1.y(), (double)vector3f_1.z(), float_2, float_3);
            }

            itemStack_1.applyDamage(boolean_2 ? 3 : 1, livingEntity_1, (livingEntity_1x) -> {
                livingEntity_1x.sendToolBreakStatus(hand_1);
            });

            world_1.spawnEntity((Entity)projectile_2);
            world_1.playSound((PlayerEntity)null, livingEntity_1.x, livingEntity_1.y, livingEntity_1.z, SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.PLAYERS, 1.0F, float_1);
        }
    }
    */
}