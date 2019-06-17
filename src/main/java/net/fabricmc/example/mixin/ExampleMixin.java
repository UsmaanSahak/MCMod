package net.fabricmc.example.mixin;

import com.google.common.cache.LoadingCache;
import com.sun.istack.internal.Nullable;
import it.unimi.dsi.fastutil.objects.Object2ByteLinkedOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateFactory;
import net.minecraft.util.IdList;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.block.BlockState;

import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.logging.Logger;

/*
@Mixin(MinecraftClient.class)
public class ExampleMixin {
	@Inject(at = @At("HEAD"), method = "init()V")
	private void init(CallbackInfo info) {
		System.out.println("This line is printed by an example mod mixin!");
	}
}
*/
/*
@Mixin(Block.class)
public class ExampleMixin {
	@Shadow @Mutable @Final
	protected int lightLevel;
	@Inject(at = @At("RETURN"), method = "<init>*")
	private void constructor(CallbackInfo info) {
		//System.out.println("This line is printed by an example mod mixin!");
		this.lightLevel = 15;
	}
}
*/

@Mixin(Block.class)
public class ExampleMixin {
	public int getLuminance(BlockState state) {
		return 15;
	}
}
