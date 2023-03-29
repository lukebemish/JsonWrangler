package dev.lukebemish.jsonwrangler.mixin;

import java.io.InputStream;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceMetadata;

@Mixin(Resource.class)
public interface ResourceAccessor {
    @Accessor
    IoSupplier<InputStream> getStreamSupplier();

    @Accessor
    IoSupplier<ResourceMetadata> getMetadataSupplier();
}
