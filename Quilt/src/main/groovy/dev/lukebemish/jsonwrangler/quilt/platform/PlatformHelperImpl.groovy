/*
 * Copyright (C) 2023 Luke Bemish and contributors
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */

package dev.lukebemish.jsonwrangler.quilt.platform

import com.google.auto.service.AutoService
import dev.lukebemish.jsonwrangler.services.IPlatformHelper
import groovy.transform.CompileStatic
import io.github.lukebemish.groovyduvet.core.api.RemappingCustomizer
import net.fabricmc.api.EnvType
import org.codehaus.groovy.control.CompilerConfiguration
import org.quiltmc.loader.api.QuiltLoader
import org.quiltmc.loader.api.minecraft.MinecraftQuiltLoader

import java.nio.file.Path

@AutoService(IPlatformHelper)
@CompileStatic
class PlatformHelperImpl implements IPlatformHelper {

    @Override
    boolean isDevelopmentEnvironment() {
        return QuiltLoader.developmentEnvironment
    }

    @Override
    boolean isClient() {
        return MinecraftQuiltLoader.environmentType == EnvType.CLIENT
    }

    @Override
    Path getConfigFolder() {
        return QuiltLoader.configDir
    }

    @Override
    Platform getPlatform() {
        return Platform.QUILT
    }

    @Override
    void customize(CompilerConfiguration compilerConfiguration) {
        compilerConfiguration.addCompilationCustomizers(new RemappingCustomizer())
    }
}
