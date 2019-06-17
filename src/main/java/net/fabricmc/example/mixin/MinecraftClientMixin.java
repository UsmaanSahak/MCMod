package net.fabricmc.example.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;

import static net.minecraft.world.biome.Biome.LOGGER;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    public ClientPlayerInteractionManager interactionManager;
    private int itemUseCooldown;
    public ClientPlayerEntity player;
    public HitResult hitResult;
    public ClientWorld world;
    public GameRenderer gameRenderer;

    private void doItemUse() {
        if (!this.interactionManager.isBreakingBlock()) {
            this.itemUseCooldown = 4;
            if (!this.player.isRiding()) {
                if (this.hitResult == null) {
                    LOGGER.warn("Null returned as 'hitResult', this shouldn't happen!");
                }

                Hand[] var1 = Hand.values();
                int var2 = var1.length;

                for(int var3 = 0; var3 < var2; ++var3) {
                    Hand hand_1 = var1[var3];
                    ItemStack itemStack_1 = this.player.getStackInHand(hand_1);
                    if (this.hitResult != null) {
                        switch(this.hitResult.getType()) {
                            case ENTITY:
                                EntityHitResult entityHitResult_1 = (EntityHitResult)this.hitResult;
                                Entity entity_1 = entityHitResult_1.getEntity();
                                if (this.interactionManager.interactEntityAtLocation(this.player, entity_1, entityHitResult_1, hand_1) == ActionResult.SUCCESS) {
                                    return;
                                }

                                if (this.interactionManager.interactEntity(this.player, entity_1, hand_1) == ActionResult.SUCCESS) {
                                    return;
                                }
                                break;
                            case BLOCK:
                                BlockHitResult blockHitResult_1 = (BlockHitResult)this.hitResult;
                                int int_1 = itemStack_1.getAmount();
                                ActionResult actionResult_1 = this.interactionManager.interactBlock(this.player, this.world, hand_1, blockHitResult_1);
                                if (actionResult_1 == ActionResult.SUCCESS) {
                                    this.player.swingHand(hand_1);
                                    if (!itemStack_1.isEmpty() && (itemStack_1.getAmount() != int_1 || this.interactionManager.hasCreativeInventory())) {
                                        this.gameRenderer.firstPersonRenderer.resetEquipProgress(hand_1);
                                    }

                                    return;
                                }

                                if (actionResult_1 == ActionResult.FAIL) {
                                    return;
                                }
                        }
                    }

                    if (!itemStack_1.isEmpty() && this.interactionManager.interactItem(this.player, this.world, hand_1) == ActionResult.SUCCESS) {
                        this.gameRenderer.firstPersonRenderer.resetEquipProgress(hand_1);
                        return;
                    }
                }

            }
        }
    }
}
