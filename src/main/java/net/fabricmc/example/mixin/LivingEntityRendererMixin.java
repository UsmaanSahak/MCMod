package net.fabricmc.example.mixin;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.GlAllocationUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SystemUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.nio.FloatBuffer;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    @Shadow @Final
    protected FloatBuffer colorOverlayBuffer;
    @Shadow
    protected int getOverlayColor(LivingEntity livingEntity_1, float float_1, float float_2) {
        return 0;
    }
    @Shadow @Final
    private static NativeImageBackedTexture colorOverlayTexture;


        public boolean applyOverlayColor(LivingEntity livingEntity_1, float float_1, boolean boolean_1) {

        float float_2 = livingEntity_1.getBrightnessAtEyes();
        int int_1 = this.getOverlayColor(livingEntity_1, float_2, float_1);
        boolean boolean_2 = (int_1 >> 24 & 255) > 0;
        boolean boolean_3 = livingEntity_1.hurtTime > 0 || livingEntity_1.deathTime > 0;
        if (!boolean_2 && !boolean_3) {
            return false;
        } else if (!boolean_2 && !boolean_1) {
            return false;
        } else {
            /*
            GlStateManager.activeTexture(GLX.GL_TEXTURE0);
            GlStateManager.enableTexture();
            GlStateManager.texEnv(8960, 8704, GLX.GL_COMBINE);
            GlStateManager.texEnv(8960, GLX.GL_COMBINE_RGB, 8448);
            GlStateManager.texEnv(8960, GLX.GL_SOURCE0_RGB, GLX.GL_TEXTURE0);
            GlStateManager.texEnv(8960, GLX.GL_SOURCE1_RGB, GLX.GL_PRIMARY_COLOR);
            GlStateManager.texEnv(8960, GLX.GL_OPERAND0_RGB, 768);
            GlStateManager.texEnv(8960, GLX.GL_OPERAND1_RGB, 768);
            GlStateManager.texEnv(8960, GLX.GL_COMBINE_ALPHA, 7681);
            GlStateManager.texEnv(8960, GLX.GL_SOURCE0_ALPHA, GLX.GL_TEXTURE0);
            GlStateManager.texEnv(8960, GLX.GL_OPERAND0_ALPHA, 770);
            GlStateManager.activeTexture(GLX.GL_TEXTURE1);
            GlStateManager.enableTexture();
            GlStateManager.texEnv(8960, 8704, GLX.GL_COMBINE);
            GlStateManager.texEnv(8960, GLX.GL_COMBINE_RGB, GLX.GL_INTERPOLATE);
            GlStateManager.texEnv(8960, GLX.GL_SOURCE0_RGB, GLX.GL_CONSTANT);
            GlStateManager.texEnv(8960, GLX.GL_SOURCE1_RGB, GLX.GL_PREVIOUS);
            GlStateManager.texEnv(8960, GLX.GL_SOURCE2_RGB, GLX.GL_CONSTANT);
            GlStateManager.texEnv(8960, GLX.GL_OPERAND0_RGB, 768);
            GlStateManager.texEnv(8960, GLX.GL_OPERAND1_RGB, 768);
            GlStateManager.texEnv(8960, GLX.GL_OPERAND2_RGB, 770);
            GlStateManager.texEnv(8960, GLX.GL_COMBINE_ALPHA, 7681);
            GlStateManager.texEnv(8960, GLX.GL_SOURCE0_ALPHA, GLX.GL_PREVIOUS);
            GlStateManager.texEnv(8960, GLX.GL_OPERAND0_ALPHA, 770);
            this.colorOverlayBuffer.position(0);
            if (boolean_3) {
                this.colorOverlayBuffer.put(1.0F);
                this.colorOverlayBuffer.put(0.0F);
                this.colorOverlayBuffer.put(0.0F);
                this.colorOverlayBuffer.put(0.3F);
            } else {
                float float_3 = (float)(int_1 >> 24 & 255) / 255.0F;
                float float_4 = (float)(int_1 >> 16 & 255) / 255.0F;
                float float_5 = (float)(int_1 >> 8 & 255) / 255.0F;
                float float_6 = (float)(int_1 & 255) / 255.0F;
                this.colorOverlayBuffer.put(float_4);
                this.colorOverlayBuffer.put(float_5);
                this.colorOverlayBuffer.put(float_6);
                this.colorOverlayBuffer.put(1.0F - float_3);
            }

            this.colorOverlayBuffer.flip();
            GlStateManager.texEnv(8960, 8705, this.colorOverlayBuffer);
            GlStateManager.activeTexture(GLX.GL_TEXTURE2);
            GlStateManager.enableTexture();
            GlStateManager.bindTexture(colorOverlayTexture.getGlId());
            GlStateManager.texEnv(8960, 8704, GLX.GL_COMBINE);
            GlStateManager.texEnv(8960, GLX.GL_COMBINE_RGB, 8448);
            GlStateManager.texEnv(8960, GLX.GL_SOURCE0_RGB, GLX.GL_PREVIOUS);
            GlStateManager.texEnv(8960, GLX.GL_SOURCE1_RGB, GLX.GL_TEXTURE1);
            GlStateManager.texEnv(8960, GLX.GL_OPERAND0_RGB, 768);
            GlStateManager.texEnv(8960, GLX.GL_OPERAND1_RGB, 768);
            GlStateManager.texEnv(8960, GLX.GL_COMBINE_ALPHA, 7681);
            GlStateManager.texEnv(8960, GLX.GL_SOURCE0_ALPHA, GLX.GL_PREVIOUS);
            GlStateManager.texEnv(8960, GLX.GL_OPERAND0_ALPHA, 770);
            GlStateManager.activeTexture(GLX.GL_TEXTURE0);
            */

            return true;
        }
     }
}
