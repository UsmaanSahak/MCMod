package net.fabricmc.example;

import com.google.common.collect.Lists;
import com.sun.istack.internal.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormat;
import net.minecraft.advancement.criterion.Criterions;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.util.math.Quaternion;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FireworkEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.Projectile;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class ABC extends BaseBowItem {

    private boolean field_7937 = false;
    private boolean field_7936 = false;
    public ItemStack ROCKET;

    public ABC(Settings item$Settings_1) {
        super(item$Settings_1);
        this.addProperty(new Identifier("pull"), (itemStack_1, world_1, livingEntity_1) -> {
            if (livingEntity_1 != null && itemStack_1.getItem() == this) {
                //System.out.println((float)(itemStack_1.getMaxUseTime() - livingEntity_1.getItemUseTimeLeft()) / (float)getPullTime(itemStack_1));
                //System.out.println(livingEntity_1.getItemUseTimeLeft());
                return isCharged(itemStack_1) ? 0.0F : (float)(itemStack_1.getMaxUseTime() - livingEntity_1.getItemUseTimeLeft()) / (float)getPullTime(itemStack_1);
            } else {
                return 0.0F;
            }
        });
        this.addProperty(new Identifier("pulling"), (itemStack_1, world_1, livingEntity_1) -> {
            return livingEntity_1 != null && livingEntity_1.isUsingItem() && livingEntity_1.getActiveItem() == itemStack_1 && !isCharged(itemStack_1) ? 1.0F : 0.0F;
        });
        this.addProperty(new Identifier("charged"), (itemStack_1, world_1, livingEntity_1) -> {
            return livingEntity_1 != null && isCharged(itemStack_1) ? 1.0F : 0.0F;
        });
        this.addProperty(new Identifier("firework"), (itemStack_1, world_1, livingEntity_1) -> {
            return livingEntity_1 != null && isCharged(itemStack_1) && hasProjectile(itemStack_1, Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
        });

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

    public Predicate<ItemStack> getHeldProjectilePredicate() {
        return IS_CROSSBOW_PROJECTILE;
    }

    public Predicate<ItemStack> getInventoryProjectilePredicate() {
        return IS_BOW_PROJECTILE;
    }

    public TypedActionResult<ItemStack> use(World world_1, PlayerEntity playerEntity_1, Hand hand_1) {
        /*
        //System.out.println("caller2: " + new Exception().getStackTrace()[2].getClassName());
        //System.out.println("callee: " + new Exception().getStackTrace()[0].getClassName());
        StackTraceElement[] ste = new Exception().getStackTrace();
        for (int i=0; i < ste.length; i++) {
            System.out.println("caller2: " + new Exception().getStackTrace()[i].getClassName());
        }
        */
        ItemStack itemStack_1 = playerEntity_1.getStackInHand(hand_1);
        if (isCharged(itemStack_1)) {
            shootAllProjectiles(world_1, playerEntity_1, hand_1, itemStack_1, method_20309(itemStack_1), 1.0F);
            setCharged(itemStack_1,false);
            //storeChargedProjectile(itemStack_1,ROCKET);
            //storeChargedProjectile(itemStack_1,new ItemStack(Items.ARROW));
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

    public void onItemStopUsing(ItemStack itemStack_1, World world_1, LivingEntity livingEntity_1, int int_1) {

        ///*
        int int_2 = this.getMaxUseTime(itemStack_1) - int_1;
        float float_1 = method_7770(int_2, itemStack_1);
        if (float_1 >= 1.0F && !isCharged(itemStack_1) && method_7767(livingEntity_1, itemStack_1)) {
            setCharged(itemStack_1, true);
            SoundCategory soundCategory_1 = livingEntity_1 instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.HOSTILE;
            world_1.playSound((PlayerEntity)null, livingEntity_1.x, livingEntity_1.y, livingEntity_1.z, SoundEvents.ITEM_CROSSBOW_LOADING_END, soundCategory_1, 1.0F, 1.0F / (random.nextFloat() * 0.5F + 1.0F) + 0.2F);
        }
        //*/


    }

    private static boolean method_7767(LivingEntity livingEntity_1, ItemStack itemStack_1) {
        int int_1 = EnchantmentHelper.getLevel(Enchantments.MULTISHOT, itemStack_1);
        int int_2 = int_1 == 0 ? 1 : 3;
        boolean boolean_1 = livingEntity_1 instanceof PlayerEntity && ((PlayerEntity)livingEntity_1).abilities.creativeMode;
        ItemStack itemStack_2 = livingEntity_1.getArrowType(itemStack_1);
        ItemStack itemStack_3 = itemStack_2.copy();

        for(int int_3 = 0; int_3 < int_2; ++int_3) {
            if (int_3 > 0) {
                itemStack_2 = itemStack_3.copy();
            }

            if (itemStack_2.isEmpty() && boolean_1) {
                itemStack_2 = new ItemStack(Items.ARROW);
                itemStack_3 = itemStack_2.copy();
            }

            if (!method_7765(livingEntity_1, itemStack_1, itemStack_2, int_3 > 0, boolean_1)) {
                return false;
            }
        }

        return true;
    }

    private static boolean method_7765(LivingEntity livingEntity_1, ItemStack itemStack_1, ItemStack itemStack_2, boolean boolean_1, boolean boolean_2) {
        if (itemStack_2.isEmpty()) {
            return false;
        } else {
            boolean boolean_3 = boolean_2 && itemStack_2.getItem() instanceof ArrowItem;
            ItemStack itemStack_4;
            if (!boolean_3 && !boolean_2 && !boolean_1) {
                itemStack_4 = itemStack_2.split(1);
                if (itemStack_2.isEmpty() && livingEntity_1 instanceof PlayerEntity) {
                    ((PlayerEntity)livingEntity_1).inventory.removeOne(itemStack_2);
                }
            } else {
                itemStack_4 = itemStack_2.copy();
            }

            storeChargedProjectile(itemStack_1, itemStack_4);
            return true;
        }
    }

    public static boolean isCharged(ItemStack itemStack_1) {
        CompoundTag compoundTag_1 = itemStack_1.getTag();
        return compoundTag_1 != null && compoundTag_1.getBoolean("Charged");
    }

    public static void setCharged(ItemStack itemStack_1, boolean boolean_1) {
        CompoundTag compoundTag_1 = itemStack_1.getOrCreateTag();
        compoundTag_1.putBoolean("Charged", boolean_1);
    }

    private static void storeChargedProjectile(ItemStack itemStack_1, ItemStack itemStack_2) {
        CompoundTag compoundTag_1 = itemStack_1.getOrCreateTag();
        ListTag listTag_2;
        if (compoundTag_1.containsKey("ChargedProjectiles", 9)) {
            listTag_2 = compoundTag_1.getList("ChargedProjectiles", 10);
        } else {
            listTag_2 = new ListTag();
        }

        CompoundTag compoundTag_2 = new CompoundTag();
        itemStack_2.toTag(compoundTag_2);
        listTag_2.add(compoundTag_2);
        compoundTag_1.put("ChargedProjectiles", listTag_2);
    }

    private static List<ItemStack> getChargedProjectiles(ItemStack itemStack_1) {
        List<ItemStack> list_1 = Lists.newArrayList();
        CompoundTag compoundTag_1 = itemStack_1.getTag();
        if (compoundTag_1 != null && compoundTag_1.containsKey("ChargedProjectiles", 9)) {
            ListTag listTag_1 = compoundTag_1.getList("ChargedProjectiles", 10);
            if (listTag_1 != null) {
                for(int int_1 = 0; int_1 < listTag_1.size(); ++int_1) {
                    CompoundTag compoundTag_2 = listTag_1.getCompoundTag(int_1);
                    list_1.add(ItemStack.fromTag(compoundTag_2));
                }
            }
        }

        return list_1;
    }

    private static void clearProjectiles(ItemStack itemStack_1) {
        CompoundTag compoundTag_1 = itemStack_1.getTag();
        if (compoundTag_1 != null) {
            ListTag listTag_1 = compoundTag_1.getList("ChargedProjectiles", 9);
            listTag_1.clear();
            compoundTag_1.put("ChargedProjectiles", listTag_1);
        }

    }

    private static boolean hasProjectile(ItemStack itemStack_1, Item item_1) {
        return getChargedProjectiles(itemStack_1).stream().anyMatch((itemStack_1x) -> {
            return itemStack_1x.getItem() == item_1;
        });
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
/*
            itemStack_1.applyDamage(boolean_2 ? 3 : 1, livingEntity_1, (livingEntity_1x) -> {
                livingEntity_1x.sendToolBreakStatus(hand_1);
            });
*/
            world_1.spawnEntity((Entity)projectile_2);
            world_1.playSound((PlayerEntity)null, livingEntity_1.x, livingEntity_1.y, livingEntity_1.z, SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.PLAYERS, 1.0F, float_1);
        }
    }

    private static ProjectileEntity method_18814(World world_1, LivingEntity livingEntity_1, ItemStack itemStack_1, ItemStack itemStack_2) {
        ArrowItem arrowItem_1 = (ArrowItem)((ArrowItem)(itemStack_2.getItem() instanceof ArrowItem ? itemStack_2.getItem() : Items.ARROW));
        ProjectileEntity projectileEntity_1 = arrowItem_1.createProjectile(world_1, itemStack_2, livingEntity_1);
        if (livingEntity_1 instanceof PlayerEntity) {
            projectileEntity_1.setCritical(true);
        }

        projectileEntity_1.setSound(SoundEvents.ITEM_CROSSBOW_HIT);
        projectileEntity_1.setShotFromCrossbow(true);
        int int_1 = EnchantmentHelper.getLevel(Enchantments.PIERCING, itemStack_1);
        if (int_1 > 0) {
            projectileEntity_1.setPierceLevel((byte)int_1);
        }

        return projectileEntity_1;
    }

    public static void shootAllProjectiles(World world_1, LivingEntity livingEntity_1, Hand hand_1, ItemStack itemStack_1, float float_1, float float_2) {
        List<ItemStack> list_1 = getChargedProjectiles(itemStack_1);
        float[] floats_1 = method_7780(livingEntity_1.getRand());

        for(int int_1 = 0; int_1 < list_1.size(); ++int_1) {
            ItemStack itemStack_2 = (ItemStack)list_1.get(int_1);
            boolean boolean_1 = livingEntity_1 instanceof PlayerEntity && ((PlayerEntity)livingEntity_1).abilities.creativeMode;
            if (!itemStack_2.isEmpty()) {
                if (int_1 == 0) {
                    shoot(world_1, livingEntity_1, hand_1, itemStack_1, itemStack_2, floats_1[int_1], boolean_1, float_1, float_2, 0.0F);
                } else if (int_1 == 1) {
                    shoot(world_1, livingEntity_1, hand_1, itemStack_1, itemStack_2, floats_1[int_1], boolean_1, float_1, float_2, -10.0F);
                } else if (int_1 == 2) {
                    shoot(world_1, livingEntity_1, hand_1, itemStack_1, itemStack_2, floats_1[int_1], boolean_1, float_1, float_2, 10.0F);
                }
            }
        }

        method_7769(world_1, livingEntity_1, itemStack_1);
    }

    private static float[] method_7780(Random random_1) {
        boolean boolean_1 = random_1.nextBoolean();
        return new float[]{1.0F, method_7784(boolean_1), method_7784(!boolean_1)};
    }

    private static float method_7784(boolean boolean_1) {
        float float_1 = boolean_1 ? 0.63F : 0.43F;
        return 1.0F / (random.nextFloat() * 0.5F + 1.8F) + float_1;
    }

    private static void method_7769(World world_1, LivingEntity livingEntity_1, ItemStack itemStack_1) {
        if (livingEntity_1 instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayerEntity_1 = (ServerPlayerEntity)livingEntity_1;
            if (!world_1.isClient) {
                Criterions.SHOT_CROSSBOW.trigger(serverPlayerEntity_1, itemStack_1);
            }

            serverPlayerEntity_1.incrementStat(Stats.USED.getOrCreateStat(itemStack_1.getItem()));
        }

        clearProjectiles(itemStack_1);
    }


    public void onUsingTick(World world_1, LivingEntity livingEntity_1, ItemStack itemStack_1, int int_1) {
        /*
        if (!world_1.isClient) {
            int int_2 = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, itemStack_1);
            SoundEvent soundEvent_1 = this.getChargeSound(int_2);
            SoundEvent soundEvent_2 = int_2 == 0 ? SoundEvents.ITEM_CROSSBOW_LOADING_MIDDLE : null;
            float float_1 = (float)(itemStack_1.getMaxUseTime() - int_1) / (float)getPullTime(itemStack_1);
            if (float_1 < 0.2F) {
                this.field_7937 = false;
                this.field_7936 = false;
            }

            if (float_1 >= 0.2F && !this.field_7937) {
                this.field_7937 = true;
                world_1.playSound((PlayerEntity)null, livingEntity_1.x, livingEntity_1.y, livingEntity_1.z, soundEvent_1, SoundCategory.PLAYERS, 0.5F, 1.0F);
            }

            if (float_1 >= 0.5F && soundEvent_2 != null && !this.field_7936) {
                this.field_7936 = true;
                world_1.playSound((PlayerEntity)null, livingEntity_1.x, livingEntity_1.y, livingEntity_1.z, soundEvent_2, SoundCategory.PLAYERS, 0.5F, 1.0F);
            }
        }
        */
    }

    public int getMaxUseTime(ItemStack itemStack_1) {
        return getPullTime(itemStack_1) + 3;
    }

    public static int getPullTime(ItemStack itemStack_1) {
        int int_1 = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, itemStack_1);
        return int_1 == 0 ? 25 : 25 - 5 * int_1;
    }

    public UseAction getUseAction(ItemStack itemStack_1) {
        return UseAction.CROSSBOW;
    }

    private SoundEvent getChargeSound(int int_1) {
        switch(int_1) {
            case 1:
                return SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_1;
            case 2:
                return SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_2;
            case 3:
                return SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_3;
            default:
                return SoundEvents.ITEM_CROSSBOW_LOADING_START;
        }
    }

    private static float method_7770(int int_1, ItemStack itemStack_1) {
        float float_1 = (float)int_1 / (float)getPullTime(itemStack_1);
        if (float_1 > 1.0F) {
            float_1 = 1.0F;
        }

        return float_1;
    }

    @Environment(EnvType.CLIENT)
    public void buildTooltip(ItemStack itemStack_1, @Nullable World world_1, List<Component> list_1, TooltipContext tooltipContext_1) {
        List<ItemStack> list_2 = getChargedProjectiles(itemStack_1);
        if (isCharged(itemStack_1) && !list_2.isEmpty()) {
            ItemStack itemStack_2 = (ItemStack)list_2.get(0);
            list_1.add((new TranslatableComponent("item.minecraft.crossbow.projectile", new Object[0])).append(" ").append(itemStack_2.toTextComponent()));
            if (tooltipContext_1.isAdvanced() && itemStack_2.getItem() == Items.FIREWORK_ROCKET) {
                List<Component> list_3 = Lists.newArrayList();
                Items.FIREWORK_ROCKET.buildTooltip(itemStack_2, world_1, list_3, tooltipContext_1);
                if (!list_3.isEmpty()) {
                    for(int int_1 = 0; int_1 < list_3.size(); ++int_1) {
                        list_3.set(int_1, (new TextComponent("  ")).append((Component)list_3.get(int_1)).applyFormat(ChatFormat.GRAY));
                    }

                    list_1.addAll(list_3);
                }
            }

        }
    }

    private static float method_20309(ItemStack itemStack_1) {
        return itemStack_1.getItem() == Items.CROSSBOW && hasProjectile(itemStack_1, Items.FIREWORK_ROCKET) ? 1.6F : 3.15F;
    }
}