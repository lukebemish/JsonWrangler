package dev.lukebemish.jsonwrangler.mixin;

import java.util.HashMap;
import java.util.Map;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.lukebemish.jsonwrangler.ResourceMutator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

@Mixin(FileToIdConverter.class)
public abstract class FileToIdConverterMixin {

    @Accessor
    public abstract String getExtension();
    @Accessor
    public abstract String getPrefix();

    @ModifyReturnValue(
        method = "listMatchingResources",
        at = @At("RETURN")
    )
    private Map<ResourceLocation, Resource> jsonwrangler$wrapResources(Map<ResourceLocation, Resource> resources, ResourceManager manager) {
        if (this.getExtension().equals(".groovy")) {
            return resources;
        }
        var groovyConverter = new FileToIdConverter(this.getPrefix(), ".groovy");
        var scripts = groovyConverter.listMatchingResourceStacks(manager);
        HashMap<ResourceLocation, Resource> newResources = new HashMap<>(resources);
        for (var entry : scripts.entrySet()) {
            var location = groovyConverter.fileToId(entry.getKey());
            var originalFile = ((FileToIdConverter)(Object)this).idToFile(location);
            if (!newResources.containsKey(originalFile)) {
                continue;
            }
            var scriptStack = entry.getValue();
            var wrapped = ResourceMutator.wrap(entry.getKey(), newResources.get(originalFile), scriptStack);
            newResources.put(originalFile, wrapped);
        }
        return newResources;
    }
}
