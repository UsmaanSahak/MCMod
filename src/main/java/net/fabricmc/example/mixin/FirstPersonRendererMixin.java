package net.fabricmc.example.mixin;

import com.google.common.base.MoreObjects;
import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.example.ExampleMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.FirstPersonRenderer;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.AbsoluteHand;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FirstPersonRenderer.class)
public class FirstPersonRendererMixin {

    //private static final Identifier MAP_BACKGROUND_TEX = new Identifier("textures/map/map_background.png");
    //private static final Identifier UNDERWATER_TEX = new Identifier("textures/misc/underwater.png");
    @Shadow @Final
    private  MinecraftClient client;
    @Shadow
    private ItemStack mainHand;
    @Shadow
    private ItemStack offHand;
    @Shadow
    private float equipProgressMainHand;
    @Shadow
    private float prevEquipProgressMainHand;
    @Shadow
    private float equipProgressOffHand;
    @Shadow
    private float prevEquipProgressOffHand;
    /*
    @Shadow @Final
    //private  EntityRenderDispatcher renderManager;
    @Shadow @Final
    //private  ItemRenderer itemRenderer;
    */

    public void renderFirstPersonItem(float float_1) {

        AbstractClientPlayerEntity abstractClientPlayerEntity_1 = this.client.player;
        float float_2 = abstractClientPlayerEntity_1.getHandSwingProgress(float_1);
        Hand hand_1 = (Hand) MoreObjects.firstNonNull(abstractClientPlayerEntity_1.preferredHand, Hand.MAIN_HAND);
        float float_3 = MathHelper.lerp(float_1, abstractClientPlayerEntity_1.prevPitch, abstractClientPlayerEntity_1.pitch);
        float float_4 = MathHelper.lerp(float_1, abstractClientPlayerEntity_1.prevYaw, abstractClientPlayerEntity_1.yaw);
        boolean boolean_1 = true; //boolean_1 means 'is active hand main hand?' isMainActive
        boolean boolean_2 = true; //isMainInactive
        ItemStack activeItem;
        if (abstractClientPlayerEntity_1.isUsingItem()) {
            activeItem = abstractClientPlayerEntity_1.getActiveItem();
            if (activeItem.getItem() == Items.BOW || activeItem.getItem() == Items.CROSSBOW /*|| activeItem.getItem() == ExampleMod.ABC*/) {
                boolean_1 = abstractClientPlayerEntity_1.getActiveHand() == Hand.MAIN_HAND;
                boolean_2 = !boolean_1;
            }

            Hand hand_2 = abstractClientPlayerEntity_1.getActiveHand();
            if (hand_2 == Hand.MAIN_HAND) {
                ItemStack itemStack_2 = abstractClientPlayerEntity_1.getOffHandStack();
                if (itemStack_2.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(itemStack_2)) {
                    boolean_2 = false; 
                }
            }
        } else {
            activeItem = abstractClientPlayerEntity_1.getMainHandStack();
            ItemStack itemStack_4 = abstractClientPlayerEntity_1.getOffHandStack();
            if (activeItem.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(activeItem)) {
                boolean_2 = !boolean_1;
            }

            if (itemStack_4.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(itemStack_4)) {
                boolean_1 = !activeItem.isEmpty();
                boolean_2 = !boolean_1;
            }
        }

        this.rotate(float_3, float_4);
        this.applyLightmap();
        this.applyCameraAngles(float_1);
        GlStateManager.enableRescaleNormal();
        float float_7;
        float float_8;
        if (boolean_1) { //If have activeItem...
            float_7 = hand_1 == Hand.MAIN_HAND ? float_2 : 0.0F;
            float_8 = 1.0F - MathHelper.lerp(float_1, this.prevEquipProgressMainHand, this.equipProgressMainHand);
            this.renderFirstPersonItem(abstractClientPlayerEntity_1, float_1, float_3, Hand.MAIN_HAND, float_7, this.mainHand, float_8);
        }

        if (boolean_2) {
            float_7 = hand_1 == Hand.OFF_HAND ? float_2 : 0.0F;
            float_8 = 1.0F - MathHelper.lerp(float_1, this.prevEquipProgressOffHand, this.equipProgressOffHand);
            this.renderFirstPersonItem(abstractClientPlayerEntity_1, float_1, float_3, Hand.OFF_HAND, float_7, this.offHand, float_8);
        }

        GlStateManager.disableRescaleNormal();
        GuiLighting.disable();
    }

    public void renderFirstPersonItem(AbstractClientPlayerEntity abstractClientPlayerEntity_1, float float_1, float float_2, Hand hand_1, float float_3, ItemStack itemStack_1, float float_4) {
        boolean boolean_1 = hand_1 == Hand.MAIN_HAND;
        AbsoluteHand absoluteHand_1 = boolean_1 ? abstractClientPlayerEntity_1.getMainHand() : abstractClientPlayerEntity_1.getMainHand().getOpposite();
        GlStateManager.pushMatrix();
        if (itemStack_1.isEmpty()) {
            if (boolean_1 && !abstractClientPlayerEntity_1.isInvisible()) {
                this.renderArmHoldingItem(float_4, float_3, absoluteHand_1);
            }
        } else if (itemStack_1.getItem() == Items.FILLED_MAP) {
            if (boolean_1 && this.offHand.isEmpty()) {
                this.renderMapInBothHands(float_2, float_4, float_3);
            } else {
                this.renderMapInOneHand(float_4, absoluteHand_1, float_3, itemStack_1);
            }
        } else {
            boolean boolean_4;
            float float_19;
            float float_20;
            float float_21;
            float float_22;
            if (itemStack_1.getItem() == Items.CROSSBOW) {
                boolean_4 = CrossbowItem.isCharged(itemStack_1);
                boolean boolean_3 = absoluteHand_1 == AbsoluteHand.RIGHT;
                int int_1 = boolean_3 ? 1 : -1;
                if (abstractClientPlayerEntity_1.isUsingItem() && abstractClientPlayerEntity_1.getItemUseTimeLeft() > 0 && abstractClientPlayerEntity_1.getActiveHand() == hand_1) {
                    this.applyHandOffset(absoluteHand_1, float_4);
                    GlStateManager.translatef((float)int_1 * -0.4785682F, -0.094387F, 0.05731531F);
                    GlStateManager.rotatef(-11.935F, 1.0F, 0.0F, 0.0F);
                    GlStateManager.rotatef((float)int_1 * 65.3F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotatef((float)int_1 * -9.785F, 0.0F, 0.0F, 1.0F);
                    float_19 = (float)itemStack_1.getMaxUseTime() - ((float)this.client.player.getItemUseTimeLeft() - float_1 + 1.0F);
                    float_20 = float_19 / (float)CrossbowItem.getPullTime(itemStack_1);
                    if (float_20 > 1.0F) {
                        float_20 = 1.0F;
                    }

                    if (float_20 > 0.1F) {
                        float_21 = MathHelper.sin((float_19 - 0.1F) * 1.3F);
                        float_22 = float_20 - 0.1F;
                        float float_9 = float_21 * float_22;
                        GlStateManager.translatef(float_9 * 0.0F, float_9 * 0.004F, float_9 * 0.0F);
                    }

                    GlStateManager.translatef(float_20 * 0.0F, float_20 * 0.0F, float_20 * 0.04F);
                    GlStateManager.scalef(1.0F, 1.0F, 1.0F + float_20 * 0.2F);
                    GlStateManager.rotatef((float)int_1 * 45.0F, 0.0F, -1.0F, 0.0F);
                } else {
                    float_19 = -0.4F * MathHelper.sin(MathHelper.sqrt(float_3) * 3.1415927F);
                    float_20 = 0.2F * MathHelper.sin(MathHelper.sqrt(float_3) * 6.2831855F);
                    float_21 = -0.2F * MathHelper.sin(float_3 * 3.1415927F);
                    GlStateManager.translatef((float)int_1 * float_19, float_20, float_21);
                    this.applyHandOffset(absoluteHand_1, float_4);
                    this.method_3217(absoluteHand_1, float_3);
                    if (boolean_4 && float_3 < 0.001F) {
                        GlStateManager.translatef((float)int_1 * -0.641864F, 0.0F, 0.0F);
                        GlStateManager.rotatef((float)int_1 * 10.0F, 0.0F, 1.0F, 0.0F);
                    }
                }

                this.renderItemFromSide(abstractClientPlayerEntity_1, itemStack_1, boolean_3 ? ModelTransformation.Type.FIRST_PERSON_RIGHT_HAND : ModelTransformation.Type.FIRST_PERSON_LEFT_HAND, !boolean_3);
            }//START
            else if (itemStack_1.getItem() == ExampleMod.ABC) { //CrossbowItem.isCharged and CrossbowItem.getPullTime are same as the ABC variants
                boolean_4 = CrossbowItem.isCharged(itemStack_1);
                boolean boolean_3 = absoluteHand_1 == AbsoluteHand.RIGHT;
                int int_1 = boolean_3 ? 1 : -1;
                if (abstractClientPlayerEntity_1.isUsingItem() && abstractClientPlayerEntity_1.getItemUseTimeLeft() > 0 && abstractClientPlayerEntity_1.getActiveHand() == hand_1) {
                    this.applyHandOffset(absoluteHand_1, float_4);
                    //Turns crossbow sideways
                    ///*
                    GlStateManager.translatef((float)int_1 * -0.4785682F, -0.094387F, 0.05731531F);
                    GlStateManager.rotatef(-11.935F, 1.0F, 0.0F, 0.0F);
                    GlStateManager.rotatef((float)int_1 * 65.3F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotatef((float)int_1 * -9.785F, 0.0F, 0.0F, 1.0F);
                    //*/
                    float_19 = (float)itemStack_1.getMaxUseTime() - ((float)this.client.player.getItemUseTimeLeft() - float_1 + 1.0F);
                    float_20 = float_19 / (float)CrossbowItem.getPullTime(itemStack_1);
                    if (float_20 > 1.0F) {
                        float_20 = 1.0F;
                    }

                    if (float_20 > 0.1F) {
                        float_21 = MathHelper.sin((float_19 - 0.1F) * 1.3F);
                        float_22 = float_20 - 0.1F;
                        float float_9 = float_21 * float_22;
                        //Makes crossbow shake slightly while reloading arrow.
                        GlStateManager.translatef(float_9 * 0.0F, float_9 * 0.004F, float_9 * 0.0F);
                    }
                    //Makes the crossbow stretch
                    ///*
                    GlStateManager.translatef(float_20 * 0.0F, float_20 * 0.0F, float_20 * 0.04F);
                    GlStateManager.scalef(1.0F, 1.0F, 1.0F + float_20 * 0.2F);
                    GlStateManager.rotatef((float)int_1 * 45.0F, 0.0F, -1.0F, 0.0F);
                    //*/

                } else {
                    float_19 = -0.4F * MathHelper.sin(MathHelper.sqrt(float_3) * 3.1415927F);
                    float_20 = 0.2F * MathHelper.sin(MathHelper.sqrt(float_3) * 6.2831855F);
                    float_21 = -0.2F * MathHelper.sin(float_3 * 3.1415927F);
                    //idk what this does tbh
                    //GlStateManager.translatef((float)int_1 * float_19, float_20, float_21);
                    this.applyHandOffset(absoluteHand_1, float_4);
                    this.method_3217(absoluteHand_1, float_3);
                    if (boolean_4 && float_3 < 0.001F) {
                        //Determines position of crossbow and abc while charged.
                        /*
                        GlStateManager.translatef((float)int_1 * -0.641864F, 0.0F, 0.0F);
                        GlStateManager.rotatef((float)int_1 * 10.0F, 0.0F, 1.0F, 0.0F);
                        */



                    }
                }

                this.renderItemFromSide(abstractClientPlayerEntity_1, itemStack_1, boolean_3 ? ModelTransformation.Type.FIRST_PERSON_RIGHT_HAND : ModelTransformation.Type.FIRST_PERSON_LEFT_HAND, !boolean_3);
            } else { //END
                boolean_4 = absoluteHand_1 == AbsoluteHand.RIGHT;
                int int_2;
                float float_18;
                if (abstractClientPlayerEntity_1.isUsingItem() && abstractClientPlayerEntity_1.getItemUseTimeLeft() > 0 && abstractClientPlayerEntity_1.getActiveHand() == hand_1) {
                    int_2 = boolean_4 ? 1 : -1;
                    switch(itemStack_1.getUseAction()) {
                        case NONE:
                            this.applyHandOffset(absoluteHand_1, float_4);
                            break;
                        case EAT:
                        case DRINK:
                            this.applyEatOrDrinkTransformation(float_1, absoluteHand_1, itemStack_1);
                            this.applyHandOffset(absoluteHand_1, float_4);
                            break;
                        case BLOCK:
                            this.applyHandOffset(absoluteHand_1, float_4);
                            break;
                        case BOW:
                            this.applyHandOffset(absoluteHand_1, float_4);
                            GlStateManager.translatef((float)int_2 * -0.2785682F, 0.18344387F, 0.15731531F);
                            GlStateManager.rotatef(-13.935F, 1.0F, 0.0F, 0.0F);
                            GlStateManager.rotatef((float)int_2 * 35.3F, 0.0F, 1.0F, 0.0F);
                            GlStateManager.rotatef((float)int_2 * -9.785F, 0.0F, 0.0F, 1.0F);
                            float_18 = (float)itemStack_1.getMaxUseTime() - ((float)this.client.player.getItemUseTimeLeft() - float_1 + 1.0F);
                            float_19 = float_18 / 20.0F;
                            float_19 = (float_19 * float_19 + float_19 * 2.0F) / 3.0F;
                            if (float_19 > 1.0F) {
                                float_19 = 1.0F;
                            }

                            if (float_19 > 0.1F) {
                                float_20 = MathHelper.sin((float_18 - 0.1F) * 1.3F);
                                float_21 = float_19 - 0.1F;
                                float_22 = float_20 * float_21;
                                GlStateManager.translatef(float_22 * 0.0F, float_22 * 0.004F, float_22 * 0.0F);
                            }

                            GlStateManager.translatef(float_19 * 0.0F, float_19 * 0.0F, float_19 * 0.04F);
                            GlStateManager.scalef(1.0F, 1.0F, 1.0F + float_19 * 0.2F);
                            GlStateManager.rotatef((float)int_2 * 45.0F, 0.0F, -1.0F, 0.0F);
                            break;
                        case SPEAR:
                            this.applyHandOffset(absoluteHand_1, float_4);
                            GlStateManager.translatef((float)int_2 * -0.5F, 0.7F, 0.1F);
                            GlStateManager.rotatef(-55.0F, 1.0F, 0.0F, 0.0F);
                            GlStateManager.rotatef((float)int_2 * 35.3F, 0.0F, 1.0F, 0.0F);
                            GlStateManager.rotatef((float)int_2 * -9.785F, 0.0F, 0.0F, 1.0F);
                            float_18 = (float)itemStack_1.getMaxUseTime() - ((float)this.client.player.getItemUseTimeLeft() - float_1 + 1.0F);
                            float_19 = float_18 / 10.0F;
                            if (float_19 > 1.0F) {
                                float_19 = 1.0F;
                            }

                            if (float_19 > 0.1F) {
                                float_20 = MathHelper.sin((float_18 - 0.1F) * 1.3F);
                                float_21 = float_19 - 0.1F;
                                float_22 = float_20 * float_21;
                                GlStateManager.translatef(float_22 * 0.0F, float_22 * 0.004F, float_22 * 0.0F);
                            }

                            GlStateManager.translatef(0.0F, 0.0F, float_19 * 0.2F);
                            GlStateManager.scalef(1.0F, 1.0F, 1.0F + float_19 * 0.2F);
                            GlStateManager.rotatef((float)int_2 * 45.0F, 0.0F, -1.0F, 0.0F);
                    }
                } else if (abstractClientPlayerEntity_1.isUsingRiptide()) {
                    this.applyHandOffset(absoluteHand_1, float_4);
                    int_2 = boolean_4 ? 1 : -1;
                    GlStateManager.translatef((float)int_2 * -0.4F, 0.8F, 0.3F);
                    GlStateManager.rotatef((float)int_2 * 65.0F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotatef((float)int_2 * -85.0F, 0.0F, 0.0F, 1.0F);
                } else {
                    float float_23 = -0.4F * MathHelper.sin(MathHelper.sqrt(float_3) * 3.1415927F);
                    float_18 = 0.2F * MathHelper.sin(MathHelper.sqrt(float_3) * 6.2831855F);
                    float_19 = -0.2F * MathHelper.sin(float_3 * 3.1415927F);
                    int int_4 = boolean_4 ? 1 : -1;
                    GlStateManager.translatef((float)int_4 * float_23, float_18, float_19);
                    this.applyHandOffset(absoluteHand_1, float_4);
                    this.method_3217(absoluteHand_1, float_3);
                }

                this.renderItemFromSide(abstractClientPlayerEntity_1, itemStack_1, boolean_4 ? ModelTransformation.Type.FIRST_PERSON_RIGHT_HAND : ModelTransformation.Type.FIRST_PERSON_LEFT_HAND, !boolean_4);
            }
        }

        GlStateManager.popMatrix();
    }


    //No changes so far
    private void applyHandOffset(AbsoluteHand absoluteHand_1, float float_1) {
        int int_1 = absoluteHand_1 == AbsoluteHand.RIGHT ? 1 : -1;
        GlStateManager.translatef((float)int_1 * 0.56F, -0.52F + float_1 * -0.6F, -0.72F);
    }

    @Shadow
    public void renderItemFromSide(LivingEntity livingEntity_1, ItemStack itemStack_1, ModelTransformation.Type modelTransformation$Type_1, boolean boolean_1) {
    }

    //No changes so far
    private void method_3217(AbsoluteHand absoluteHand_1, float float_1) {
        int int_1 = absoluteHand_1 == AbsoluteHand.RIGHT ? 1 : -1;
        float float_2 = MathHelper.sin(float_1 * float_1 * 3.1415927F);
        GlStateManager.rotatef((float)int_1 * (45.0F + float_2 * -20.0F), 0.0F, 1.0F, 0.0F);
        float float_3 = MathHelper.sin(MathHelper.sqrt(float_1) * 3.1415927F);
        GlStateManager.rotatef((float)int_1 * float_3 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotatef(float_3 * -80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotatef((float)int_1 * -45.0F, 0.0F, 1.0F, 0.0F);
    }
    @Shadow
    private void applyLightmap() {}
    @Shadow
    private void renderMapInOneHand(float float_1, AbsoluteHand absoluteHand_1, float float_2, ItemStack itemStack_1) {}
    @Shadow
    private void applyCameraAngles(float float_1) {}
    @Shadow
    private void renderArmHoldingItem(float float_1, float float_2, AbsoluteHand absoluteHand_1) {}
    @Shadow
    private void renderMapInBothHands(float float_1, float float_2, float float_3) {}
    @Shadow
    private void applyEatOrDrinkTransformation(float float_1, AbsoluteHand absoluteHand_1, ItemStack itemStack_1) {}
    @Shadow
    private void rotate(float float_1, float float_2) {}
}
