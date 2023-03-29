package dev.lukebemish.jsonwrangler

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.mojang.serialization.JsonOps
import dev.lukebemish.jsonwrangler.mixin.ResourceAccessor
import dev.lukebemish.jsonwrangler.services.Services
import groovy.transform.CompileStatic
import io.github.groovymc.cgl.api.codec.ObjectOps
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.Resource
import org.codehaus.groovy.control.CompilerConfiguration

@CompileStatic
final class ResourceMutator {
    public static final CompilerConfiguration COMPILER_CONFIGURATION = new CompilerConfiguration().tap {
        Services.PLATFORM.customize(it)
    }
    public static final Gson GSON = new GsonBuilder().setLenient().create()

    static class Overrider {
        boolean override = false

        void call() {
            override = true
        }
    }

    private ResourceMutator() {}

    static Resource wrap(ResourceLocation location, Resource resource, List<Resource> scripts) {
        if (scripts.empty) {
            return resource
        }
        return new Resource(resource.source(), {->
            JsonElement json = GSON.fromJson(resource.openAsReader(), JsonElement.class)
            try {
                JsonElement mutated = mutateResource(location, json, scripts)
                return new ByteArrayInputStream(GSON.toJson(mutated).bytes)
            } catch (RuntimeException e) {
                throw new IOException("Failed to mutate resource with script ${location}: ", e)
            }
        }, ((ResourceAccessor) resource).getMetadataSupplier())
    }

    static JsonElement mutateResource(ResourceLocation location, JsonElement resource, List<Resource> scripts) {
        if (scripts.empty) {
            return resource
        }
        Object json = JsonOps.INSTANCE.convertTo(ObjectOps.instance, resource)
        Overrider override = new Overrider()
        for (int i = scripts.size() - 1; i >= 0; i--) {
            Resource script = scripts[i]
            Map props = [
                'json': json,
                'override': override,
            ]
            props += JsonWranglerCommon.sharedEnvMap
            Binding binding = new Binding(props)
            GroovyShell shell = new GroovyShell(ResourceMutator.classLoader, binding, COMPILER_CONFIGURATION)
            try (var reader = script.openAsReader()) {
                json = shell.evaluate(reader)
            }
            if (override.override) {
                break
            }
        }
        return ObjectOps.instance.convertTo(JsonOps.INSTANCE, json)
    }
}
