package net.fabricmc.example.mixin;

import net.fabricmc.example.ExampleMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public class ItemMixin {
///*
    public boolean method_7838(ItemStack itemStack_1) {
        return itemStack_1.getItem() == Items.CROSSBOW || itemStack_1.getItem() == ExampleMod.ABC;
    }

 //*/

}
