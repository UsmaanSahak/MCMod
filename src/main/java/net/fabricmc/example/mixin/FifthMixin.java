package net.fabricmc.example.mixin;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.gui.MapRenderer;
import net.minecraft.client.render.*;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.SystemUtil;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Locale;
import java.util.Random;

@Mixin(GameRenderer.class)
public class FifthMixin {
    @Shadow @Final
    private static final Logger LOGGER = LogManager.getLogger();
    @Shadow @Final
    private static final Identifier RAIN_LOC = new Identifier("textures/environment/rain.png");
    @Shadow @Final
    private static final Identifier SNOW_LOC = new Identifier("textures/environment/snow.png");
    @Shadow @Final
    private final MinecraftClient client = null;
    @Shadow @Final
    private  ResourceManager resourceContainer;
    @Shadow @Final
    private  Random random = new Random();
    @Shadow
    private float viewDistance;
    @Shadow
    public  FirstPersonRenderer firstPersonRenderer;
    @Shadow
    private  MapRenderer mapRenderer;
    @Shadow
    private int ticks;
    @Shadow
    private float movementFovMultiplier;
    @Shadow
    private float lastMovementFovMultiplier;
    @Shadow
    private float skyDarkness;
    @Shadow
    private float lastSkyDarkness;
    @Shadow
    private boolean renderHand = true;
    @Shadow
    private boolean blockOutlineEnabled = true;
    @Shadow
    private long lastWorldIconUpdate;
    @Shadow
    private long lastWindowFocusedTime = SystemUtil.getMeasuringTimeMs();
    @Shadow @Final
    private LightmapTextureManager lightmapTextureManager;
    @Shadow
    private int field_3995;
    @Shadow @Final
    private  float[] field_3991 = new float[1024];
    @Shadow @Final
    private  float[] field_3989 = new float[1024];
    @Shadow @Final
    private  BackgroundRenderer backgroundRenderer;
    @Shadow
    private boolean field_4001;
    @Shadow
    private double field_4005 = 1.0D;
    @Shadow
    private double field_3988;
    @Shadow
    private double field_4004;
    @Shadow
    private ItemStack floatingItem;
    @Shadow
    private int floatingItemTimeLeft;
    @Shadow
    private float floatingItemWidth;
    @Shadow
    private float floatingItemHeight;
    @Shadow
    private ShaderEffect shader;
    @Shadow
    private static final Identifier[] SHADERS_LOCATIONS = new Identifier[]{new Identifier("shaders/post/notch.json"), new Identifier("shaders/post/fxaa.json"), new Identifier("shaders/post/art.json"), new Identifier("shaders/post/bumpy.json"), new Identifier("shaders/post/blobs2.json"), new Identifier("shaders/post/pencil.json"), new Identifier("shaders/post/color_convolve.json"), new Identifier("shaders/post/deconverge.json"), new Identifier("shaders/post/flip.json"), new Identifier("shaders/post/invert.json"), new Identifier("shaders/post/ntsc.json"), new Identifier("shaders/post/outline.json"), new Identifier("shaders/post/phosphor.json"), new Identifier("shaders/post/scan_pincushion.json"), new Identifier("shaders/post/sobel.json"), new Identifier("shaders/post/bits.json"), new Identifier("shaders/post/desaturate.json"), new Identifier("shaders/post/green.json"), new Identifier("shaders/post/blur.json"), new Identifier("shaders/post/wobble.json"), new Identifier("shaders/post/blobs.json"), new Identifier("shaders/post/antialias.json"), new Identifier("shaders/post/creeper.json"), new Identifier("shaders/post/spider.json")};
    @Shadow @Final
    public static  int SHADER_COUNT;
    @Shadow
    private int forcedShaderIndex;
    @Shadow
    private boolean shadersEnabled;
    @Shadow
    private int field_4021;
    @Shadow @Final
    private  Camera camera;
    @Shadow
    public void renderWorld(float float_1, long long_1) {}
    @Shadow
    private void updateWorldIcon() {}
    @Shadow
    private void renderFloatingItem(int int_1, int int_2, float float_1) {}



        public void render(float float_1, long long_1, boolean boolean_1) {
        if (!this.client.isWindowFocused() && this.client.options.pauseOnLostFocus && (!this.client.options.touchscreen || !this.client.mouse.wasRightButtonClicked())) {
            if (SystemUtil.getMeasuringTimeMs() - this.lastWindowFocusedTime > 500L) {
                this.client.openPauseMenu(false);
            }
        } else {
            this.lastWindowFocusedTime = SystemUtil.getMeasuringTimeMs();
        }

        if (!this.client.skipGameRender) {
            int int_1 = (int)(this.client.mouse.getX() * (double)this.client.window.getScaledWidth() / (double)this.client.window.getWidth());
            int int_2 = (int)(this.client.mouse.getY() * (double)this.client.window.getScaledHeight() / (double)this.client.window.getHeight());
            int int_3 = this.client.options.maxFps;
            if (boolean_1 && this.client.world != null) {
                this.client.getProfiler().push("level");
                int int_4 = Math.min(MinecraftClient.getCurrentFps(), int_3);
                int_4 = Math.max(int_4, 60);
                long long_2 = SystemUtil.getMeasuringTimeNano() - long_1;
                long long_3 = Math.max((long)(1000000000 / int_4 / 4) - long_2, 0L);
                this.renderWorld(float_1, SystemUtil.getMeasuringTimeNano() + long_3);
                if (this.client.isIntegratedServerRunning() && this.lastWorldIconUpdate < SystemUtil.getMeasuringTimeMs() - 1000L) {
                    this.lastWorldIconUpdate = SystemUtil.getMeasuringTimeMs();
                    if (!this.client.getServer().hasIconFile()) {
                        this.updateWorldIcon();
                    }
                }

                if (GLX.usePostProcess) {
                    this.client.worldRenderer.drawEntityOutlinesFramebuffer();
                    if (this.shader != null && this.shadersEnabled) {
                        GlStateManager.matrixMode(5890);
                        GlStateManager.pushMatrix();
                        GlStateManager.loadIdentity();
                        this.shader.render(float_1);
                        GlStateManager.popMatrix();
                    }

                    this.client.getFramebuffer().beginWrite(true);
                }

                this.client.getProfiler().swap("gui");
                if (!this.client.options.hudHidden || this.client.currentScreen != null) {
                    GlStateManager.alphaFunc(516, 0.1F);
                    this.client.window.method_4493(MinecraftClient.IS_SYSTEM_MAC);
                    this.renderFloatingItem(this.client.window.getScaledWidth(), this.client.window.getScaledHeight(), float_1);
                    this.client.inGameHud.draw(float_1);
                }

                this.client.getProfiler().pop();
            } else {
                GlStateManager.viewport(0, 0, this.client.window.getFramebufferWidth(), this.client.window.getFramebufferHeight());
                GlStateManager.matrixMode(5889);
                GlStateManager.loadIdentity();
                GlStateManager.matrixMode(5888);
                GlStateManager.loadIdentity();
                this.client.window.method_4493(MinecraftClient.IS_SYSTEM_MAC);
            }

            CrashReportSection crashReportSection_2;
            CrashReport crashReport_2;
            if (this.client.overlay != null) {
                GlStateManager.clear(256, MinecraftClient.IS_SYSTEM_MAC);

                try {
                    this.client.overlay.render(int_1, int_2, this.client.getLastFrameDuration());
                } catch (Throwable var14) {
                    crashReport_2 = CrashReport.create(var14, "Rendering overlay");
                    crashReportSection_2 = crashReport_2.addElement("Overlay render details");
                    crashReportSection_2.add("Overlay name", () -> {
                        return this.client.overlay.getClass().getCanonicalName();
                    });
                    throw new CrashException(crashReport_2);
                }
            } else if (this.client.currentScreen != null) {
                GlStateManager.clear(256, MinecraftClient.IS_SYSTEM_MAC);

                try {
                    this.client.currentScreen.render(int_1, int_2, this.client.getLastFrameDuration());
                } catch (Throwable var13) {
                    crashReport_2 = CrashReport.create(var13, "Rendering screen");
                    crashReportSection_2 = crashReport_2.addElement("Screen render details");
                    crashReportSection_2.add("Screen name", () -> {
                        return this.client.currentScreen.getClass().getCanonicalName();
                    });
                    crashReportSection_2.add("Mouse location", () -> {
                        return String.format(Locale.ROOT, "Scaled: (%d, %d). Absolute: (%f, %f)", int_1, int_2, this.client.mouse.getX(), this.client.mouse.getY());
                    });
                    crashReportSection_2.add("Screen size", () -> {
                        return String.format(Locale.ROOT, "Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %f", this.client.window.getScaledWidth(), this.client.window.getScaledHeight(), this.client.window.getFramebufferWidth(), this.client.window.getFramebufferHeight(), this.client.window.getScaleFactor());
                    });
                    throw new CrashException(crashReport_2);
                }
            }

        }
    }
}
