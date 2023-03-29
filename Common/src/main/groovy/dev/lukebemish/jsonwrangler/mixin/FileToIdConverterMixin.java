package dev.lukebemish.jsonwrangler.mixin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @SuppressWarnings({"unused"})
    @ModifyReturnValue(
        method = "listMatchingResources",
        at = @At("RETURN")
    )
    private Map<ResourceLocation, Resource> jsonwrangler$wrapResources(Map<ResourceLocation, Resource> resources, ResourceManager manager) {
        if (this.getExtension().endsWith(".groovy")) {
            return resources;
        }
        var groovyConverter = new FileToIdConverter(this.getPrefix(), this.getExtension()+".groovy");
        var scripts = groovyConverter.listMatchingResourceStacks(manager);
        var metaGroovyConverter = new FileToIdConverter(this.getPrefix(), this.getExtension()+".mcmeta.groovy");
        var metaScripts = metaGroovyConverter.listMatchingResourceStacks(manager);
        var metaConverter = new FileToIdConverter(this.getPrefix(), this.getExtension()+".mcmeta");
        HashMap<ResourceLocation, Resource> newResources = new HashMap<>(resources);
        Set<ResourceLocation> originalFiles = new HashSet<>();
        for (var fileLocation : scripts.keySet()) {
            var location = groovyConverter.fileToId(fileLocation);
            //noinspection DataFlowIssue
            var originalFile = ((FileToIdConverter)(Object)this).idToFile(location);
            originalFiles.add(originalFile);
        }
        for (var fileLocation : metaScripts.keySet()) {
            var location = metaGroovyConverter.fileToId(fileLocation);
            //noinspection DataFlowIssue
            var originalFile = ((FileToIdConverter)(Object)this).idToFile(location);
            originalFiles.add(originalFile);
        }
        for (var originalFile : originalFiles) {
            if (!newResources.containsKey(originalFile)) {
                continue;
            }
            //noinspection DataFlowIssue
            var location = ((FileToIdConverter)(Object)this).fileToId(originalFile);
            var metaScriptLocation = metaGroovyConverter.idToFile(location);
            var metaLocation = metaConverter.idToFile(location);
            var scriptLocation = groovyConverter.idToFile(location);
            var scriptStack = scripts.getOrDefault(scriptLocation, List.of());
            var metaScriptStack = metaScripts.getOrDefault(metaScriptLocation, List.of());
            var wrapped = ResourceMutator.wrap(newResources.get(originalFile), manager.getResource(metaLocation).orElse(null), scriptLocation, metaScriptLocation, scriptStack, metaScriptStack);
            newResources.put(originalFile, wrapped);
        }
        return newResources;
    }
}
