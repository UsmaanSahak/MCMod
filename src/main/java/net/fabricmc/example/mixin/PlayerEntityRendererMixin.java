package net.fabricmc.example.mixin;

import net.fabricmc.example.ABC;
import net.fabricmc.example.UseActionM;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;


@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {




    private BipedEntityModel.ArmPose method_4210(AbstractClientPlayerEntity abstractClientPlayerEntity_1, ItemStack itemStack_1, ItemStack itemStack_2, Hand hand_1) {

        //System.out.println("We called method_4210");
        BipedEntityModel.ArmPose bipedEntityModel$ArmPose_1 = BipedEntityModel.ArmPose.EMPTY;
        ItemStack itemStack_3 = hand_1 == Hand.MAIN_HAND ? itemStack_1 : itemStack_2;
        if (!itemStack_3.isEmpty()) {
            //System.out.println("not empty");
            bipedEntityModel$ArmPose_1 = BipedEntityModel.ArmPose.ITEM;
            if (abstractClientPlayerEntity_1.getItemUseTimeLeft() > 0) {
                //System.out.println("not empty2");
                UseAction useAction_1 = itemStack_3.getUseAction();
                //UseActionM useActionM;
                ///*
                System.out.println("class is 2 " + itemStack_3.getClass());

                if (useAction_1 == null) {
                    System.out.println("class is " + itemStack_3.getClass());
                }

                //*/
                if (useAction_1 == UseAction.BLOCK) {
                    bipedEntityModel$ArmPose_1 = BipedEntityModel.ArmPose.BLOCK;
                } else if (useAction_1 == UseAction.BOW) {
                    bipedEntityModel$ArmPose_1 = BipedEntityModel.ArmPose.BOW_AND_ARROW;
                } else if (useAction_1 == UseAction.SPEAR) {
                    bipedEntityModel$ArmPose_1 = BipedEntityModel.ArmPose.THROW_SPEAR;
                } else if (useAction_1 == UseAction.CROSSBOW && hand_1 == abstractClientPlayerEntity_1.getActiveHand()) {
                    bipedEntityModel$ArmPose_1 = BipedEntityModel.ArmPose.CROSSBOW_CHARGE;
                }
            } else {
                //System.out.println("not empty3");
                boolean boolean_1 = itemStack_1.getItem() == Items.CROSSBOW;
                boolean boolean_2 = CrossbowItem.isCharged(itemStack_1);
                boolean boolean_3 = itemStack_2.getItem() == Items.CROSSBOW;
                boolean boolean_4 = CrossbowItem.isCharged(itemStack_2);
                if (boolean_1 && boolean_2) {
                    bipedEntityModel$ArmPose_1 = BipedEntityModel.ArmPose.CROSSBOW_HOLD;
                }

                if (boolean_3 && boolean_4 && itemStack_1.getItem().getUseAction(itemStack_1) == UseAction.NONE) {
                    bipedEntityModel$ArmPose_1 = BipedEntityModel.ArmPose.CROSSBOW_HOLD;
                }
            }
        }
        //System.out.println("returning from PlayerEntityRenderer");
        return bipedEntityModel$ArmPose_1;
    }
}
