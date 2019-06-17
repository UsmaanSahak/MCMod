package net.fabricmc.example;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.CreeperEntityModel;
import net.minecraft.util.Identifier;

public class CookieCreeperRenderer extends MobEntityRenderer<CookieCreeperEntity, CreeperEntityModel<CookieCreeperEntity>> {
    public CookieCreeperRenderer(EntityRenderDispatcher entityRenderDispatcher_1) {
        super(entityRenderDispatcher_1, new CreeperEntityModel<>(),1);
    }
    @Override
    protected Identifier getTexture(CookieCreeperEntity cookieCreeperEntity) {
        return new Identifier("wiki-entity:textures/entity/cookie_creeper/creeper.png");
    }
}
