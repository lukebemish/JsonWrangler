/*
 * Copyright (C) 2023 Luke Bemish and contributors
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */

package dev.lukebemish.jsonwrangler


import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.mojang.serialization.JsonOps
import dev.lukebemish.jsonwrangler.mixin.ResourceAccessor
import dev.lukebemish.jsonwrangler.services.Services
import groovy.transform.CompileStatic
import org.groovymc.cgl.api.codec.ObjectOps
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.IoSupplier
import net.minecraft.server.packs.resources.Resource
import net.minecraft.server.packs.resources.ResourceMetadata
import org.apache.commons.io.input.SequenceReader
import org.codehaus.groovy.control.CompilerConfiguration

@CompileStatic
final class ResourceMutator {
    public static final CompilerConfiguration COMPILER_CONFIGURATION = new CompilerConfiguration().tap {
        Services.PLATFORM.customize(it)
    }
    public static final Gson GSON = new GsonBuilder().setLenient().create()
    public static final String SERVER_SCRIPT = '// JSONWRANGLER:FROM_SERVER'

    static class Overrider {
        boolean override = false

        void call() {
            override = true
        }
    }

    private ResourceMutator() {}

    static Resource wrap(Resource original, Resource meta, ResourceLocation scriptLocation, ResourceLocation metaScriptLocation, List<Resource> scripts, List<Resource> metaScripts) {
        IoSupplier<InputStream> stream
        if (scripts.empty) {
            stream = ((ResourceAccessor)original).streamSupplier
        } else {
            stream = {->
                try (var reader = original.openAsReader()) {
                    JsonElement json = GSON.fromJson(reader, JsonElement.class)
                    JsonElement mutated = mutateResource(scriptLocation, json, scripts)
                    return new ByteArrayInputStream(GSON.toJson(mutated).bytes)
                } catch (RuntimeException e) {
                    throw new IOException("Failed to mutate resource with script ${scriptLocation}: ", e)
                }
            }
        }

        IoSupplier<ResourceMetadata> metaStream
        if (meta === null || metaScripts.empty) {
            metaStream = ((ResourceAccessor)original).metadataSupplier
        } else {
            metaStream = {->
                try (var reader = meta.openAsReader()) {
                    JsonElement json = GSON.fromJson(reader, JsonElement.class)
                    JsonElement mutated = mutateResource(metaScriptLocation, json, metaScripts)
                    return ResourceMetadata.fromJsonStream(new ByteArrayInputStream(GSON.toJson(mutated).bytes))
                } catch (RuntimeException e) {
                    throw new IOException("Failed to mutate resource with script ${scriptLocation}: ", e)
                }
            }
        }

        return new Resource(original.source(), stream, metaStream)
    }

    static JsonElement mutateResource(ResourceLocation location, JsonElement resource, List<Resource> scripts) {
        if (scripts.empty) {
            return resource
        }
        Object json = JsonOps.INSTANCE.convertTo(ObjectOps.instance, resource)
        Overrider override = new Overrider()
        for (int i = scripts.size() - 1; i >= 0; i--) {
            Resource script = scripts[i]
            try (var reader = script.openAsReader()) {
                String firstLine = reader.readLine()
                if (firstLine != null && firstLine.startsWith(SERVER_SCRIPT)) {
                    continue
                }
                try (var scriptReader = new SequenceReader(new StringReader(firstLine+'\n'), reader)) {
                    Map props = [
                        'json'    : json,
                        'override': override,
                    ]
                    props += JsonWranglerCommon.sharedEnvMap
                    Binding binding = new Binding(props)
                    GroovyShell shell = new GroovyShell(ResourceMutator.classLoader, binding, COMPILER_CONFIGURATION)
                    json = shell.evaluate(scriptReader)
                    if (override.override) {
                        break
                    }
                }
            }
        }
        return ObjectOps.instance.convertTo(JsonOps.INSTANCE, json)
    }
}
