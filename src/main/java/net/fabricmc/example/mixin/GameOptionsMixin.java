package net.fabricmc.example.mixin;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.sun.istack.internal.Nullable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.*;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.tutorial.TutorialStep;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.AbsoluteHand;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static net.minecraft.client.options.GameOptions.COLON_SPLITTER;

@Mixin(GameOptions.class)
public class GameOptionsMixin {
///*
    @Shadow
    public TutorialStep tutorialStep;

    @Shadow
    protected MinecraftClient client;
    @Shadow
    private static float parseFloat(String string_1) { return 1.0f;}
    @Shadow
    private CompoundTag method_1626(CompoundTag compoundTag_1) {return null;}
    @Shadow @Final
    private static Logger LOGGER;
    @Shadow @Final
    private File optionsFile;
    @Shadow @Final
    private  Map<SoundCategory, Float> soundVolumeLevels;
    @Shadow @Final
    private static Gson GSON;
    @Shadow @Final
    private static Type STRING_LIST_TYPE;

//*/
    @Inject(at = @At("RETURN"), method="<init>*")
    private void constructor(MinecraftClient minecraftClient_1, File file_1,CallbackInfo info) {
        System.out.println("Creating GameOptions.");

    }
    ///*
    @Overwrite
    public void load() {
        try {
            if (!this.optionsFile.exists()) {

                return;
            }

            this.soundVolumeLevels.clear();
            List<String> list_1 = IOUtils.readLines(new FileInputStream(this.optionsFile));
            CompoundTag compoundTag_1 = new CompoundTag();
            Iterator var3 = list_1.iterator();

            String string_2;
            while(var3.hasNext()) {
                string_2 = (String)var3.next();

                try {
                    Iterator<String> iterator_1 = COLON_SPLITTER.omitEmptyStrings().limit(2).split(string_2).iterator();
                    compoundTag_1.putString((String)iterator_1.next(), (String)iterator_1.next());
                } catch (Exception var10) {
                    LOGGER.warn("Skipping bad option: {}", string_2);
                }
            }

            compoundTag_1 = this.method_1626(compoundTag_1);
            var3 = compoundTag_1.getKeys().iterator();

            while(var3.hasNext()) {
                string_2 = (String)var3.next();
                String string_3 = compoundTag_1.getString(string_2);

                try {
                    if ("autoJump".equals(string_2)) {
                        Option.AUTO_JUMP.set(((GameOptions)(Object)this), string_3);
                    }

                    if ("autoSuggestions".equals(string_2)) {
                        Option.AUTO_SUGGESTIONS.set(((GameOptions)(Object)this), string_3);
                    }

                    if ("chatColors".equals(string_2)) {
                        Option.CHAT_COLOR.set(((GameOptions)(Object)this), string_3);
                    }

                    if ("chatLinks".equals(string_2)) {
                        Option.CHAT_LINKS.set(((GameOptions)(Object)this), string_3);
                    }

                    if ("chatLinksPrompt".equals(string_2)) {
                        Option.CHAT_LINKS_PROMPT.set(((GameOptions)(Object)this), string_3);
                    }

                    if ("enableVsync".equals(string_2)) {
                        Option.VSYNC.set(((GameOptions)(Object)this), string_3);
                    }

                    if ("entityShadows".equals(string_2)) {
                        Option.ENTITY_SHADOWS.set(((GameOptions)(Object)this), string_3);
                    }

                    if ("forceUnicodeFont".equals(string_2)) {
                        Option.FORCE_UNICODE_FONT.set(((GameOptions)(Object)this), string_3);
                    }

                    if ("discrete_mouse_scroll".equals(string_2)) {
                        Option.DISCRETE_MOUSE_SCROLL.set(((GameOptions)(Object)this), string_3);
                    }

                    if ("invertYMouse".equals(string_2)) {
                        Option.INVERT_MOUSE.set(((GameOptions)(Object)this), string_3);
                    }

                    if ("realmsNotifications".equals(string_2)) {
                        Option.REALMS_NOTIFICATIONS.set(((GameOptions)(Object)this), string_3);
                    }

                    if ("reducedDebugInfo".equals(string_2)) {
                        Option.REDUCED_DEBUG_INFO.set(((GameOptions)(Object)this), string_3);
                    }

                    if ("showSubtitles".equals(string_2)) {
                        Option.SUBTITLES.set(((GameOptions)(Object)this), string_3);
                    }

                    if ("snooperEnabled".equals(string_2)) {
                        Option.SNOOPER.set(((GameOptions)(Object)this), string_3);
                    }

                    if ("touchscreen".equals(string_2)) {
                        Option.TOUCHSCREEN.set(((GameOptions)(Object)this), string_3);
                    }

                    if ("fullscreen".equals(string_2)) {
                        Option.FULLSCREEN.set(((GameOptions)(Object)this), string_3);
                    }

                    if ("bobView".equals(string_2)) {
                        Option.VIEW_BOBBING.set(((GameOptions)(Object)this), string_3);
                    }

                    if ("mouseSensitivity".equals(string_2)) {
                        ((GameOptions)(Object)this).mouseSensitivity = (double)parseFloat(string_3);
                    }

                    if ("fov".equals(string_2)) {
                        ((GameOptions)(Object)this).fov = (double)(parseFloat(string_3) * 40.0F + 70.0F);
                    }

                    if ("gamma".equals(string_2)) {
                        ((GameOptions)(Object)this).gamma = (double)parseFloat(string_3);
                    }

                    if ("renderDistance".equals(string_2)) {
                        ((GameOptions)(Object)this).viewDistance = Integer.parseInt(string_3);
                    }

                    if ("guiScale".equals(string_2)) {
                        ((GameOptions)(Object)this).guiScale = Integer.parseInt(string_3);
                    }

                    if ("particles".equals(string_2)) {
                        ((GameOptions)(Object)this).particles = ParticlesOption.byId(Integer.parseInt(string_3));
                    }

                    if ("maxFps".equals(string_2)) {
                        ((GameOptions)(Object)this).maxFps = Integer.parseInt(string_3);
                        if (this.client.window != null) {
                            this.client.window.setFramerateLimit(((GameOptions)(Object)this).maxFps);
                        }
                    }

                    if ("difficulty".equals(string_2)) {
                        ((GameOptions)(Object)this).difficulty = Difficulty.getDifficulty(Integer.parseInt(string_3));
                    }

                    if ("fancyGraphics".equals(string_2)) {
                        ((GameOptions)(Object)this).fancyGraphics = "true".equals(string_3);
                    }

                    if ("tutorialStep".equals(string_2)) {
                        this.tutorialStep = TutorialStep.byName(string_3);
                    }

                    if ("ao".equals(string_2)) {
                        if ("true".equals(string_3)) {
                            ((GameOptions)(Object)this).ao = AoOption.MAX;
                        } else if ("false".equals(string_3)) {
                            ((GameOptions)(Object)this).ao = AoOption.OFF;
                        } else {
                            ((GameOptions)(Object)this).ao = AoOption.getOption(Integer.parseInt(string_3));
                        }
                    }

                    if ("renderClouds".equals(string_2)) {
                        if ("true".equals(string_3)) {
                            ((GameOptions)(Object)this).cloudRenderMode = CloudRenderMode.FANCY;
                        } else if ("false".equals(string_3)) {
                            ((GameOptions)(Object)this).cloudRenderMode = CloudRenderMode.OFF;
                        } else if ("fast".equals(string_3)) {
                            ((GameOptions)(Object)this).cloudRenderMode = CloudRenderMode.FAST;
                        }
                    }

                    if ("attackIndicator".equals(string_2)) {
                        ((GameOptions)(Object)this).attackIndicator = AttackIndicator.byId(Integer.parseInt(string_3));
                    }

                    if ("resourcePacks".equals(string_2)) {
                        ((GameOptions)(Object)this).resourcePacks = (List) JsonHelper.deserialize(GSON, string_3, STRING_LIST_TYPE);
                        if (((GameOptions)(Object)this).resourcePacks == null) {
                            ((GameOptions)(Object)this).resourcePacks = Lists.newArrayList();
                        }
                    }

                    if ("incompatibleResourcePacks".equals(string_2)) {
                        ((GameOptions)(Object)this).incompatibleResourcePacks = (List)JsonHelper.deserialize(GSON, string_3, STRING_LIST_TYPE);
                        if (((GameOptions)(Object)this).incompatibleResourcePacks == null) {
                            ((GameOptions)(Object)this).incompatibleResourcePacks = Lists.newArrayList();
                        }
                    }

                    if ("lastServer".equals(string_2)) {
                        ((GameOptions)(Object)this).lastServer = string_3;
                    }

                    if ("lang".equals(string_2)) {
                        ((GameOptions)(Object)this).language = string_3;
                    }

                    if ("chatVisibility".equals(string_2)) {
                        ((GameOptions)(Object)this).chatVisibility = ChatVisibility.byId(Integer.parseInt(string_3));
                    }

                    if ("chatOpacity".equals(string_2)) {
                        ((GameOptions)(Object)this).chatOpacity = (double)parseFloat(string_3);
                    }

                    if ("textBackgroundOpacity".equals(string_2)) {
                        ((GameOptions)(Object)this).textBackgroundOpacity = (double)parseFloat(string_3);
                    }

                    if ("backgroundForChatOnly".equals(string_2)) {
                        ((GameOptions)(Object)this).backgroundForChatOnly = "true".equals(string_3);
                    }

                    if ("fullscreenResolution".equals(string_2)) {
                        ((GameOptions)(Object)this).fullscreenResolution = string_3;
                    }

                    if ("hideServerAddress".equals(string_2)) {
                        ((GameOptions)(Object)this).hideServerAddress = "true".equals(string_3);
                    }

                    if ("advancedItemTooltips".equals(string_2)) {
                        ((GameOptions)(Object)this).advancedItemTooltips = "true".equals(string_3);
                    }

                    if ("pauseOnLostFocus".equals(string_2)) {
                        ((GameOptions)(Object)this).pauseOnLostFocus = "true".equals(string_3);
                    }

                    if ("overrideHeight".equals(string_2)) {
                        ((GameOptions)(Object)this).overrideHeight = Integer.parseInt(string_3);
                    }

                    if ("overrideWidth".equals(string_2)) {
                        ((GameOptions)(Object)this).overrideWidth = Integer.parseInt(string_3);
                    }

                    if ("heldItemTooltips".equals(string_2)) {
                        ((GameOptions)(Object)this).heldItemTooltips = "true".equals(string_3);
                    }

                    if ("chatHeightFocused".equals(string_2)) {
                        ((GameOptions)(Object)this).chatHeightFocused = (double)parseFloat(string_3);
                    }

                    if ("chatHeightUnfocused".equals(string_2)) {
                        ((GameOptions)(Object)this).chatHeightUnfocused = (double)parseFloat(string_3);
                    }

                    if ("chatScale".equals(string_2)) {
                        ((GameOptions)(Object)this).chatScale = (double)parseFloat(string_3);
                    }

                    if ("chatWidth".equals(string_2)) {
                        ((GameOptions)(Object)this).chatWidth = (double)parseFloat(string_3);
                    }

                    if ("mipmapLevels".equals(string_2)) {
                        ((GameOptions)(Object)this).mipmapLevels = Integer.parseInt(string_3);
                    }

                    if ("useNativeTransport".equals(string_2)) {
                        ((GameOptions)(Object)this).useNativeTransport = "true".equals(string_3);
                    }

                    if ("mainHand".equals(string_2)) {
                        ((GameOptions)(Object)this).mainHand = "left".equals(string_3) ? AbsoluteHand.LEFT : AbsoluteHand.RIGHT;
                    }

                    if ("narrator".equals(string_2)) {
                        ((GameOptions)(Object)this).narrator = NarratorOption.byId(Integer.parseInt(string_3));
                    }

                    if ("biomeBlendRadius".equals(string_2)) {
                        ((GameOptions)(Object)this).biomeBlendRadius = Integer.parseInt(string_3);
                    }

                    if ("mouseWheelSensitivity".equals(string_2)) {
                        ((GameOptions)(Object)this).mouseWheelSensitivity = (double)parseFloat(string_3);
                    }

                    if ("glDebugVerbosity".equals(string_2)) {
                        ((GameOptions)(Object)this).glDebugVerbosity = Integer.parseInt(string_3);
                    }

                    KeyBinding[] var6 = ((GameOptions)(Object)this).keysAll;
                    int var7 = var6.length;

                    int var8;
                    for(var8 = 0; var8 < var7; ++var8) {
                        KeyBinding keyBinding_1 = var6[var8];
                        if (string_2.equals("key_" + keyBinding_1.getId())) {
                            keyBinding_1.setKeyCode(InputUtil.fromName(string_3));
                        }
                    }

                    SoundCategory[] var14 = SoundCategory.values();
                    var7 = var14.length;

                    for(var8 = 0; var8 < var7; ++var8) {
                        SoundCategory soundCategory_1 = var14[var8];
                        if (string_2.equals("soundCategory_" + soundCategory_1.getName())) {
                            this.soundVolumeLevels.put(soundCategory_1, parseFloat(string_3));
                        }
                    }

                    PlayerModelPart[] var15 = PlayerModelPart.values();
                    var7 = var15.length;

                    for(var8 = 0; var8 < var7; ++var8) {
                        PlayerModelPart playerModelPart_1 = var15[var8];
                        if (string_2.equals("modelPart_" + playerModelPart_1.getName())) {
                            ((GameOptions)(Object)this).setPlayerModelPart(playerModelPart_1, "true".equals(string_3));
                        }
                    }
                } catch (Exception var11) {
                    LOGGER.warn("Skipping bad option: {}:{}", string_2, string_3);
                }
            }

            KeyBinding.updateKeysByCode();
        } catch (Exception var12) {
            LOGGER.error("Failed to load options", var12);
        }

    }
    //*/
}
