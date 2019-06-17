package net.fabricmc.example.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.sun.istack.internal.Nullable;
import net.minecraft.client.render.Camera;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class ThirdMixin {
    @Shadow
    private boolean ready;
    @Shadow
    private BlockView area;
    @Shadow
    private Entity focusedEntity;
    @Shadow
    private Vec3d pos;
    @Shadow @Final
    private BlockPos.Mutable blockPos;
    @Shadow
    private Vec3d field_18714;
    @Shadow
    private Vec3d field_18715;
    @Shadow
    private Vec3d field_18716;
    @Shadow
    private float pitch;
    @Shadow
    private float yaw;
    @Shadow
    private boolean thirdPerson;
    @Shadow
    private boolean inverseView;
    @Shadow
    private float field_18721;
    @Shadow
    private float field_18722;

    @Shadow
    protected void setPos(double x, double y, double z) {}
    @Shadow
    protected void setRotation(float yaw, float pitch) {}
    @Shadow
    protected void updateRotation() {}
    @Shadow
    protected void moveBy(double x, double y, double z) {}
    @Shadow
    private double method_19318(double double_1) {return 0.0;}



    @Overwrite
    public void update(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta) {
        this.ready = true;
        this.area = area;
        this.focusedEntity = focusedEntity;
        this.thirdPerson = thirdPerson;
        this.inverseView = inverseView;
        this.setRotation(focusedEntity.getYaw(tickDelta), focusedEntity.getPitch(tickDelta));
        this.setPos(MathHelper.lerp(tickDelta, focusedEntity.prevX, focusedEntity.x), MathHelper.lerp(tickDelta, focusedEntity.prevY, focusedEntity.y) + MathHelper.lerp(tickDelta, this.field_18722, this.field_18721), MathHelper.lerp(tickDelta, focusedEntity.prevZ, focusedEntity.z));
        if (thirdPerson) {
            if (inverseView) {
                this.yaw += 180.0f;
                this.pitch += -this.pitch * 2.0f;
                this.updateRotation();
            }
            this.moveBy(-this.method_19318(4.0), 0.0, 0.0);
        }
        else if (focusedEntity instanceof LivingEntity && ((LivingEntity)focusedEntity).isSleeping()) {
            Direction direction7 = ((LivingEntity)focusedEntity).getSleepingDirection();
            this.setRotation((direction7 != null) ? (direction7.asRotation() - 180.0f) : 0.0f, 0.0f);
            this.moveBy(0.0, 0.3, 0.0);
        }
        else {
            this.moveBy(-0.05000000074505806, 0.0, 0.0);
        }
        GlStateManager.rotatef(this.pitch, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotatef(this.yaw + 180.0f, 0.0f, 1.0f, 0.0f);

    }
}
