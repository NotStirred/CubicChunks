package cubicchunks.cc.mixin.core.common.ticket.interfaces;

import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ChunkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChunkManager.class)
public interface InvokeChunkManager {

    @Invoker("func_219220_a")
    ChunkHolder chunkHold(long chunkPosIn);
}
