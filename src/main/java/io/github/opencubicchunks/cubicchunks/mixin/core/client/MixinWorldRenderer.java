package io.github.opencubicchunks.cubicchunks.mixin.core.client;

import io.github.opencubicchunks.cubicchunks.mixin.access.client.ViewFrustumAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ViewFrustum;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {

    @Shadow
    private int renderDistanceChunks;

    @Shadow
    private ViewFrustum viewFrustum;

    @Final
    private Minecraft mc;

    /**
     * @author Barteks2x
     */
    @javax.annotation.Nullable
    @Overwrite
    private ChunkRenderDispatcher.ChunkRender getRenderChunkOffset(BlockPos playerPos, ChunkRenderDispatcher.ChunkRender renderChunkBase, Direction facing) {

        BlockPos blockpos = renderChunkBase.getBlockPosOffset16(facing);
        if (MathHelper.abs(playerPos.getX() - blockpos.getX()) <= this.renderDistanceChunks * 16
                && MathHelper.abs(playerPos.getY() - blockpos.getY()) <= this.renderDistanceChunks * 16
                && MathHelper.abs(playerPos.getZ() - blockpos.getZ()) <= this.renderDistanceChunks * 16) {
            return ((ViewFrustumAccess) this.viewFrustum).getRenderChunkAt(blockpos);
        }
        return null;
    }



    @Redirect(method = "setupTerrain", at = @At(value = "FIELD",
            target = "Lnet/minecraft/client/renderer/WorldRenderer;renderDistanceChunks:I"),
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WorldRenderer;loadRenderers()V")))
    private int setUpterrain(WorldRenderer _this) {
//        if (!((ICubicWorld) world).isCubicWorld()) {
//            return mc.gameSettings.renderDistanceChunks;
//        }
        return mc.gameSettings.renderDistanceChunks;
    }

    @ModifyConstant(method = "renderWorldBorder", constant = @Constant(intValue = 256))
    private static int modifyrenderWorldBorder(int original) {
        return 512;
    }
}