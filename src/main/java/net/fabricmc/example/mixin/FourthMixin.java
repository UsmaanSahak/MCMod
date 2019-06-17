package net.fabricmc.example.mixin;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.ClientChatListener;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.*;
import net.minecraft.client.options.AttackIndicator;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.chat.ChatMessageType;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.Team;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.SystemUtil;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static net.minecraft.client.gui.DrawableHelper.GUI_ICONS_LOCATION;
import static net.minecraft.client.gui.DrawableHelper.fill;

@Mixin(InGameHud.class)
public class FourthMixin extends DrawableHelper{
    private static final Identifier VIGNETTE_TEX = new Identifier("textures/misc/vignette.png");
    private static final Identifier WIDGETS_TEX = new Identifier("textures/gui/widgets.png");
    private static final Identifier PUMPKIN_BLUR = new Identifier("textures/misc/pumpkinblur.png");
    private final Random random = new Random();
    @Shadow @Final
    private  MinecraftClient client;
    @Shadow @Final
    private  ItemRenderer itemRenderer;
    @Shadow @Final
    private  ChatHud chatHud;
    @Shadow
    private int ticks;
    @Shadow
    private String overlayMessage = "";
    @Shadow
    private int overlayRemaining;
    @Shadow
    private boolean overlayTinted;
    @Shadow
    public float field_2013 = 1.0F;
    @Shadow
    private int heldItemTooltipFade;
    @Shadow
    private ItemStack currentStack;
    @Shadow @Final
    private  DebugHud debugHud;
    @Shadow @Final
    private  SubtitlesHud subtitlesHud;
    @Shadow @Final
    private  SpectatorHud spectatorHud;
    @Shadow @Final
    private  PlayerListHud playerListHud;
    @Shadow @Final
    private BossBarHud bossBarHud;
    @Shadow
    private int titleTotalTicks;
    @Shadow
    private String title;
    @Shadow
    private String subtitle;
    @Shadow
    private int titleFadeInTicks;
    @Shadow
    private int titleRemainTicks;
    @Shadow
    private int titleFadeOutTicks;
    @Shadow
    private int field_2014;
    @Shadow
    private int field_2033;
    @Shadow
    private long field_2012;
    @Shadow
    private long field_2032;
    @Shadow
    private int scaledWidth;
    @Shadow
    private int scaledHeight;
    @Shadow @Final
    private Map<ChatMessageType, List<ClientChatListener>> listeners;
    @Shadow
    public TextRenderer getFontRenderer() {return client.textRenderer;}
    @Shadow
    private void renderVignetteOverlay(Entity entity_1) {}
    @Shadow
    private void renderPumpkinOverlay() {}
    @Shadow
    private void renderPortalOverlay(float float_1) {}
    @Shadow
    protected void renderHotbar(float float_1) {}
    /*
    @Shadow
    private void renderCrosshair() {}
*/
    @Shadow
    private void drawMountHealth() {}
    @Shadow
    public void renderMountJumpBar(int remainingHealth) {}
    @Shadow
    public void renderExperienceBar(int remainingHealth) {}
    @Shadow
    public void renderHeldItemTooltip() {}
    @Shadow
    public void renderDemoTimer() {}
    @Shadow
    protected void renderStatusEffectOverlay() {}
    @Shadow
    private void method_19346(TextRenderer textRenderer_1, int remainingHealth, int int_2) {}
    @Shadow
    private void renderScoreboardSidebar(ScoreboardObjective scoreboardObjective_1) {}
    ///*
    @Shadow
    private PlayerEntity getCameraPlayer() { return null; }



    @Shadow
    private LivingEntity getRiddenEntity() { return null; }
    @Shadow
    private int method_1744(LivingEntity livingEntity_1) { return 0; }
    @Shadow
    private int method_1733(int remainingHealth) { return 0; }
    @Shadow
    private boolean shouldRenderSpectatorCrosshair(HitResult hitResult_1) { return true;}


        private void renderCrosshair() {
        GameOptions gameOptions_1 = this.client.options;
        if (gameOptions_1.perspective == 0) {
            if (this.client.interactionManager.getCurrentGameMode() != GameMode.SPECTATOR || this.shouldRenderSpectatorCrosshair(this.client.hitResult)) {
                if (gameOptions_1.debugEnabled && !gameOptions_1.hudHidden && !this.client.player.getReducedDebugInfo() && !gameOptions_1.reducedDebugInfo) {
                    GlStateManager.pushMatrix();
                    GlStateManager.translatef((float)(this.scaledWidth / 2), (float)(this.scaledHeight / 2), (float)this.blitOffset);
                    Camera camera_1 = this.client.gameRenderer.getCamera();
                    GlStateManager.rotatef(camera_1.getPitch(), -1.0F, 0.0F, 0.0F);
                    GlStateManager.rotatef(camera_1.getYaw(), 0.0F, 1.0F, 0.0F);
                    GlStateManager.scalef(-1.0F, -1.0F, -1.0F);
                    GLX.renderCrosshair(10);
                    GlStateManager.popMatrix();
                } else {
                    GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    int int_1 = 1;
                    this.blit((this.scaledWidth - 15) / 2, (this.scaledHeight - 15) / 2, 0, 0, 15, 15); //This is where. 15 15 -> 32 32
                    if (this.client.options.attackIndicator == AttackIndicator.CROSSHAIR) {
                        float float_1 = this.client.player.getAttackCooldownProgress(0.0F);
                        boolean boolean_1 = false;
                        if (this.client.targetedEntity != null && this.client.targetedEntity instanceof LivingEntity && float_1 >= 1.0F) {
                            boolean_1 = this.client.player.getAttackCooldownProgressPerTick() > 5.0F;
                            boolean_1 &= this.client.targetedEntity.isAlive();
                        }

                        int int_2 = this.scaledHeight / 2 - 7 + 16;
                        int int_3 = this.scaledWidth / 2 - 8;
                        if (boolean_1) {
                            this.blit(int_3, int_2, 68, 94, 16, 16);
                        } else if (float_1 < 1.0F) {
                            int int_4 = (int)(float_1 * 17.0F);
                            this.blit(int_3, int_2, 36, 94, 16, 4);
                            this.blit(int_3, int_2, 52, 94, int_4, 4);
                        }
                    }
                }

            }
        }
    }

    @Overwrite
    private void renderStatusBars() {
        int circleHori[] = {40,30,50,22,58,22,58,28,52,40};
        int circleVert[] = {223,220,220,210,210,200,200,190,190,186};
        //int circleHori[] = {40,31,49,27,53,27,53,31,49,40};
        //int circleVert[] = {216,215,215,210,210,203,203,195,195,192};
        this.blit(36, 199, 184,0,18, 18);


        PlayerEntity playerEntity_1 = this.getCameraPlayer();
        if (playerEntity_1 != null) {
            int remainingHealth = MathHelper.ceil(playerEntity_1.getHealth());
            boolean boolean_1 = this.field_2032 > (long)this.ticks && (this.field_2032 - (long)this.ticks) / 3L % 2L == 1L;
            long long_1 = SystemUtil.getMeasuringTimeMs();
            if (remainingHealth < this.field_2014 && playerEntity_1.field_6008 > 0) {
                this.field_2012 = long_1;
                this.field_2032 = (long)(this.ticks + 20);
            } else if (remainingHealth > this.field_2014 && playerEntity_1.field_6008 > 0) {
                this.field_2012 = long_1;
                this.field_2032 = (long)(this.ticks + 10);
            }

            if (long_1 - this.field_2012 > 1000L) {
                this.field_2014 = remainingHealth;
                this.field_2033 = remainingHealth;
                this.field_2012 = long_1;
            }

            this.field_2014 = remainingHealth;
            int int_2 = this.field_2033;
            this.random.setSeed((long)(this.ticks * 312871));
            HungerManager hungerManager_1 = playerEntity_1.getHungerManager();
            int int_3 = hungerManager_1.getFoodLevel();
            EntityAttributeInstance entityAttributeInstance_1 = playerEntity_1.getAttributeInstance(EntityAttributes.MAX_HEALTH);
            int AHleftMargin = this.scaledWidth / 2 - 91; //AHleftMargin is the health and armor leftMargin
            int hungLeftMargin = this.scaledWidth / 2 + 91;
            int HAHtopMargin = this.scaledHeight - 39;
            HAHtopMargin = 0;
            float float_1 = (float)entityAttributeInstance_1.getValue();
            int currEnchanted = MathHelper.ceil(playerEntity_1.getAbsorptionAmount());
            int int_8 = MathHelper.ceil((float_1 + (float)currEnchanted) / 2.0F / 10.0F);
            int int_9 = Math.max(10 - (int_8 - 2), 3);
            int armorTopMargin = HAHtopMargin - (int_8 - 1) * int_9 - 10;
            int int_11 = HAHtopMargin - 10;
            int IcurrEnchanted = currEnchanted;
            int int_13 = playerEntity_1.getArmor();
            int int_14 = -1;
            if (playerEntity_1.hasStatusEffect(StatusEffects.REGENERATION)) {
                int_14 = this.ticks % MathHelper.ceil(float_1 + 5.0F);
            }

            this.client.getProfiler().push("armor");

            int IcurrHealth;
            int armorLeftMargin;
            for(IcurrHealth = 0; IcurrHealth < 10; ++IcurrHealth) {
                if (int_13 > 0) {
                    armorLeftMargin = AHleftMargin + IcurrHealth * 8; //armorLeftMargin is armor icon horizontal margins
                    if (IcurrHealth * 2 + 1 < int_13) {
                        this.blit(armorLeftMargin, armorTopMargin, 34, 9, 9, 9);

                    }

                    if (IcurrHealth * 2 + 1 == int_13) {
                        this.blit(armorLeftMargin, armorTopMargin, 25, 9, 9, 9);
                    }

                    if (IcurrHealth * 2 + 1 > int_13) {
                        this.blit(armorLeftMargin, armorTopMargin, 16, 9, 9, 9);
                    }
                }
            }

            this.client.getProfiler().swap("health");

            int int_26;
            int heartHorizontalMargin;
            int heartTopMargin;

            float is = 0;
            float ia = 0;
            double Spadding = (6.28 * 20) / 10;
            double Apadding = 360 / 9;


            //float_1 is probably total health, since always 20.0.
            //currEnchanted is the number of enchanted health points (The extra, yellow ones you get after eating an enchanted apple.)
            //for(IcurrHealth = MathHelper.ceil((float_1 + (float)currEnchanted) / 2.0F) - 1; IcurrHealth >= 0; --IcurrHealth) {


            for(IcurrHealth = MathHelper.ceil(float_1  / 2.0F) - 1; IcurrHealth >= 0; --IcurrHealth) {
                //IcurrHealth is an iterator that goes from # of hearts to 0.

                armorLeftMargin = 16;
                if (playerEntity_1.hasStatusEffect(StatusEffects.POISON)) {
                    armorLeftMargin += 36;
                } else if (playerEntity_1.hasStatusEffect(StatusEffects.WITHER)) {
                    armorLeftMargin += 72;
                }

                int int_19 = 0;
                if (boolean_1) {
                    int_19 = 1;
                }

                int_26 = MathHelper.ceil((float)(IcurrHealth + 1) / 10.0F) - 1;
                //heartHorizontalMargin = AHleftMargin + IcurrHealth % 10 * 19; //8 -> 19
                //heartTopMargin = HAHtopMargin - int_26 * int_9;

                /*
                heartHorizontalMargin = Math.round((float) (Math.cos(ia) * 50)) + 100;
                heartTopMargin = Math.round((float) (Math.sin(ia) * 50)) + 100;
                System.out.println("horiz " + heartHorizontalMargin);
                System.out.println("top " + heartTopMargin);
                System.out.println(IcurrHealth);
                is += Spadding;
                ia += Apadding;
                */
                heartHorizontalMargin = circleHori[IcurrHealth];
                heartTopMargin = circleVert[IcurrHealth];
                /*
                if (remainingHealth <= 4) {
                    heartTopMargin += this.random.nextInt(2); //If total health is two hearts or less, then increment heartTopMargin by random number btweeen 0 and 2
                }

                if (IcurrEnchanted <= 0 && IcurrHealth == int_14) { //If got that enchanted apple regen, then have hearts jump.
                    heartTopMargin -= 2;
                }
                */
                //iconRow is row in icons.png?
                int iconRow = 0;
                if (playerEntity_1.world.getLevelProperties().isHardcore()) {
                    iconRow = 5;
                }
                //This line spawns the back heart frames.
                this.blit(heartHorizontalMargin, heartTopMargin, 34 + int_19 * 9, 9 * iconRow, 9, 9); // Changing last 2 nines does something, 16 -> 34
                if (boolean_1) { //boolean_1 is changes to reg health
                    //int_2 is always positive and between 0 and 20 probs? Doesn't seem to be remaining health?
                    if (IcurrHealth * 2 + 1 < int_2) {
                        //this.blit(heartHorizontalMargin + 100, heartTopMargin + 100, armorLeftMargin + 54, 9 * iconRow, 19, 19); // + 0 + 0 9 9 -> + 100 + 100 19 19
                    }

                    if (IcurrHealth * 2 + 1 == int_2) {
                        //this.blit(heartHorizontalMargin + 100, heartTopMargin + 100, armorLeftMargin + 63, 9 * iconRow, 19,19);
                    }
                }

                if (IcurrEnchanted > 0) {

                    if (IcurrEnchanted == currEnchanted && currEnchanted % 2 == 1) {
                        this.blit(heartHorizontalMargin, heartTopMargin, armorLeftMargin + 153, 9 * iconRow, 9, 9);
                        --IcurrEnchanted;
                    } else {
                        this.blit(heartHorizontalMargin, heartTopMargin, armorLeftMargin + 144, 9 * iconRow, 9, 9);
                        IcurrEnchanted -= 2;
                    }
                } else { //Controls regular hearts spawning
                    if (IcurrHealth * 2 + 1 < remainingHealth) {
                        this.blit(heartHorizontalMargin, heartTopMargin, armorLeftMargin + 36, 9 * iconRow, 9, 9);
                    }

                    if (IcurrHealth * 2 + 1 == remainingHealth) {
                        this.blit(heartHorizontalMargin, heartTopMargin, armorLeftMargin + 45, 9 * iconRow, 9, 9);
                    }
                }
            }
            LivingEntity livingEntity_1 = this.getRiddenEntity();
            armorLeftMargin = this.method_1744(livingEntity_1);
            int int_25;
            int int_29;
            /*
            if (armorLeftMargin == 0) {
                this.client.getProfiler().swap("food");

                for(int_25 = 0; int_25 < 10; ++int_25) {
                    int_26 = HAHtopMargin;
                    heartHorizontalMargin = 16;
                    int int_28 = 0;
                    if (playerEntity_1.hasStatusEffect(StatusEffects.HUNGER)) {
                        heartHorizontalMargin += 36;
                        int_28 = 13;
                    }

                    if (playerEntity_1.getHungerManager().getSaturationLevel() <= 0.0F && this.ticks % (int_3 * 3 + 1) == 0) {
                        int_26 = HAHtopMargin + (this.random.nextInt(3) - 1);
                    }

                    int_29 = hungLeftMargin - int_25 * 8 - 9;
                    this.blit(int_29, int_26, 16 + int_28 * 9, 27, 9, 9);
                    if (int_25 * 2 + 1 < int_3) {
                        this.blit(int_29, int_26, heartHorizontalMargin + 36, 27, 9, 9);
                    }

                    if (int_25 * 2 + 1 == int_3) {
                        this.blit(int_29, int_26, heartHorizontalMargin + 45, 27, 9, 9);
                    }
                }

                int_11 -= 10;
            }
            */
            this.client.getProfiler().swap("air");
            int_25 = playerEntity_1.getBreath();
            int_26 = playerEntity_1.getMaxBreath();
            if (playerEntity_1.isInFluid(FluidTags.WATER) || int_25 < int_26) {
                heartHorizontalMargin = this.method_1733(armorLeftMargin) - 1;
                int_11 -= heartHorizontalMargin * 10;
                heartTopMargin = MathHelper.ceil((double)(int_25 - 2) * 10.0D / (double)int_26);
                int_29 = MathHelper.ceil((double)int_25 * 10.0D / (double)int_26) - heartTopMargin;

                for(int int_35 = 0; int_35 < heartTopMargin + int_29; ++int_35) {
                    if (int_35 < heartTopMargin) {
                        this.blit(hungLeftMargin - int_35 * 8 - 9, int_11, 16, 18, 9, 9);
                    } else {
                        this.blit(hungLeftMargin - int_35 * 8 - 9, int_11, 25, 18, 9, 9);
                    }
                }
            }

            this.client.getProfiler().pop();
        }
    }

//*/
    @Overwrite
    public void draw(float float_1) {
        this.scaledWidth = this.client.window.getScaledWidth();
        this.scaledHeight = this.client.window.getScaledHeight();
        TextRenderer textRenderer_1 = this.getFontRenderer();
        GlStateManager.enableBlend();
        if (MinecraftClient.isFancyGraphicsEnabled()) {
            this.renderVignetteOverlay(this.client.getCameraEntity());
        } else {
            GlStateManager.enableDepthTest();
            GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }

        ItemStack itemStack_1 = this.client.player.inventory.getArmorStack(3);
        if (this.client.options.perspective == 0 && itemStack_1.getItem() == Blocks.CARVED_PUMPKIN.asItem()) {
            this.renderPumpkinOverlay();
        }

        float float_6;
        if (!this.client.player.hasStatusEffect(StatusEffects.NAUSEA)) {
            float_6 = MathHelper.lerp(float_1, this.client.player.lastNauseaStrength, this.client.player.nextNauseaStrength);
            if (float_6 > 0.0F) {
                this.renderPortalOverlay(float_6);
            }
        }

        if (this.client.interactionManager.getCurrentGameMode() == GameMode.SPECTATOR) {
            this.spectatorHud.draw(float_1);
        } else if (!this.client.options.hudHidden) {
            this.renderHotbar(float_1);

        }

        if (!this.client.options.hudHidden) {
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.client.getTextureManager().bindTexture(GUI_ICONS_LOCATION);
            GlStateManager.enableBlend();
            GlStateManager.enableAlphaTest();
            this.renderCrosshair();
            GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            this.client.getProfiler().push("bossHealth");
            this.bossBarHud.draw();
            this.client.getProfiler().pop();
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.client.getTextureManager().bindTexture(GUI_ICONS_LOCATION);
            if (this.client.interactionManager.hasStatusBars()) {
                this.renderStatusBars();
            }

            this.drawMountHealth();
            GlStateManager.disableBlend();
            int remainingHealth = this.scaledWidth / 2 - 91;
            if (this.client.player.hasJumpingMount()) {
                this.renderMountJumpBar(remainingHealth);
            } else if (this.client.interactionManager.hasExperienceBar()) {
                this.renderExperienceBar(remainingHealth);
            }

            if (this.client.options.heldItemTooltips && this.client.interactionManager.getCurrentGameMode() != GameMode.SPECTATOR) {
                this.renderHeldItemTooltip();
            } else if (this.client.player.isSpectator()) {
                this.spectatorHud.draw();
            }
        }

        int currEnchanted;
        if (this.client.player.getSleepTimer() > 0) {
            this.client.getProfiler().push("sleep");
            GlStateManager.disableDepthTest();
            GlStateManager.disableAlphaTest();
            float_6 = (float)this.client.player.getSleepTimer();
            float float_4 = float_6 / 100.0F;
            if (float_4 > 1.0F) {
                float_4 = 1.0F - (float_6 - 100.0F) / 10.0F;
            }

            currEnchanted = (int)(220.0F * float_4) << 24 | 1052704;
            fill(0, 0, this.scaledWidth, this.scaledHeight, currEnchanted);
            GlStateManager.enableAlphaTest();
            GlStateManager.enableDepthTest();
            this.client.getProfiler().pop();
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        }

        if (this.client.isDemo()) {
            this.renderDemoTimer();
        }

        this.renderStatusEffectOverlay();
        if (this.client.options.debugEnabled) {
            this.debugHud.draw();
        }

        if (!this.client.options.hudHidden) {
            int int_8;
            int HAHtopMargin;
            if (this.overlayRemaining > 0) {
                this.client.getProfiler().push("overlayMessage");
                float_6 = (float)this.overlayRemaining - float_1;
                HAHtopMargin = (int)(float_6 * 255.0F / 20.0F);
                if (HAHtopMargin > 255) {
                    HAHtopMargin = 255;
                }

                if (HAHtopMargin > 8) {
                    GlStateManager.pushMatrix();
                    GlStateManager.translatef((float)(this.scaledWidth / 2), (float)(this.scaledHeight - 68), 0.0F);
                    GlStateManager.enableBlend();
                    GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    currEnchanted = 16777215;
                    if (this.overlayTinted) {
                        currEnchanted = MathHelper.hsvToRgb(float_6 / 50.0F, 0.7F, 0.6F) & 16777215;
                    }

                    int_8 = HAHtopMargin << 24 & -16777216;
                    this.method_19346(textRenderer_1, -4, textRenderer_1.getStringWidth(this.overlayMessage));
                    textRenderer_1.draw(this.overlayMessage, (float)(-textRenderer_1.getStringWidth(this.overlayMessage) / 2), -4.0F, currEnchanted | int_8);
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                }

                this.client.getProfiler().pop();
            }

            if (this.titleTotalTicks > 0) {
                this.client.getProfiler().push("titleAndSubtitle");
                float_6 = (float)this.titleTotalTicks - float_1;
                HAHtopMargin = 255;
                if (this.titleTotalTicks > this.titleFadeOutTicks + this.titleRemainTicks) {
                    float float_7 = (float)(this.titleFadeInTicks + this.titleRemainTicks + this.titleFadeOutTicks) - float_6;
                    HAHtopMargin = (int)(float_7 * 255.0F / (float)this.titleFadeInTicks);
                }

                if (this.titleTotalTicks <= this.titleFadeOutTicks) {
                    HAHtopMargin = (int)(float_6 * 255.0F / (float)this.titleFadeOutTicks);
                }

                HAHtopMargin = MathHelper.clamp(HAHtopMargin, 0, 255);
                if (HAHtopMargin > 8) {
                    GlStateManager.pushMatrix();
                    GlStateManager.translatef((float)(this.scaledWidth / 2), (float)(this.scaledHeight / 2), 0.0F);
                    GlStateManager.enableBlend();
                    GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    GlStateManager.pushMatrix();
                    GlStateManager.scalef(4.0F, 4.0F, 4.0F);
                    currEnchanted = HAHtopMargin << 24 & -16777216;
                    int_8 = textRenderer_1.getStringWidth(this.title);
                    this.method_19346(textRenderer_1, -10, int_8);
                    textRenderer_1.drawWithShadow(this.title, (float)(-int_8 / 2), -10.0F, 16777215 | currEnchanted);
                    GlStateManager.popMatrix();
                    if (!this.subtitle.isEmpty()) {
                        GlStateManager.pushMatrix();
                        GlStateManager.scalef(2.0F, 2.0F, 2.0F);
                        int int_9 = textRenderer_1.getStringWidth(this.subtitle);
                        this.method_19346(textRenderer_1, 5, int_9);
                        textRenderer_1.drawWithShadow(this.subtitle, (float)(-int_9 / 2), 5.0F, 16777215 | currEnchanted);
                        GlStateManager.popMatrix();
                    }

                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                }

                this.client.getProfiler().pop();
            }

            this.subtitlesHud.draw();
            Scoreboard scoreboard_1 = this.client.world.getScoreboard();
            ScoreboardObjective scoreboardObjective_1 = null;
            Team team_1 = scoreboard_1.getPlayerTeam(this.client.player.getEntityName());
            if (team_1 != null) {
                int_8 = team_1.getColor().getId();
                if (int_8 >= 0) {
                    scoreboardObjective_1 = scoreboard_1.getObjectiveForSlot(3 + int_8);
                }
            }

            ScoreboardObjective scoreboardObjective_2 = scoreboardObjective_1 != null ? scoreboardObjective_1 : scoreboard_1.getObjectiveForSlot(1);
            if (scoreboardObjective_2 != null) {
                this.renderScoreboardSidebar(scoreboardObjective_2);
            }

            GlStateManager.enableBlend();
            GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.disableAlphaTest();
            GlStateManager.pushMatrix();
            GlStateManager.translatef(0.0F, (float)(this.scaledHeight - 48), 0.0F);
            this.client.getProfiler().push("chat");
            this.chatHud.draw(this.ticks);
            this.client.getProfiler().pop();
            GlStateManager.popMatrix();
            scoreboardObjective_2 = scoreboard_1.getObjectiveForSlot(0);
            if (!this.client.options.keyPlayerList.isPressed() || this.client.isInSingleplayer() && this.client.player.networkHandler.getPlayerList().size() <= 1 && scoreboardObjective_2 == null) {
                this.playerListHud.tick(false);
            } else {
                this.playerListHud.tick(true);
                this.playerListHud.draw(this.scaledWidth, scoreboard_1, scoreboardObjective_2);
            }
        }

        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();
        GlStateManager.enableAlphaTest();
        //*/
    }
}
