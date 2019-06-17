package net.fabricmc.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.render.EntityRendererRegistry;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ExampleMod implements ModInitializer {
/*
	public static final EntityType<CookieCreeperEntity> COOKIE_CREEPER =
			Registry.register(
					Registry.ENTITY_TYPE,
					new Identifier("wiki-entity", "cookie-creeper"),
					FabricEntityTypeBuilder.create(EntityCategory.AMBIENT, CookieCreeperEntity::new).size(1, 2).build()
			);
*/


	public static final Item FABRIC_ITEM = new Item(new Item.Settings().itemGroup(ItemGroup.MISC));
	public static final ABC ABC = new ABC(new Item.Settings().stackSize(2).itemGroup(ItemGroup.COMBAT).durability(1));

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		Registry.register(Registry.ITEM, new Identifier("wikitut", "fabric_item"), FABRIC_ITEM);
		Registry.register(Registry.ITEM, new Identifier("us", "abc"), ABC);

		//EntityRendererRegistry.INSTANCE.register(CookieCreeperEntity.class, (entityRenderDispatcher, context) -> new CookieCreeperRenderer(entityRenderDispatcher));

		System.out.println("Hello Fabric world!");
		//GameStory s = new GameStory();
	}

}
