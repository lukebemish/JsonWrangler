/*
 * Copyright (C) 2023 Luke Bemish and contributors
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */

package dev.lukebemish.jsonwrangler


import dev.lukebemish.jsonwrangler.services.IPlatformHelper
import dev.lukebemish.jsonwrangler.services.Services
import groovy.transform.CompileStatic
import net.minecraft.server.packs.FilePackResources

@CompileStatic
final class JsonWranglerCommon {
    private JsonWranglerCommon() {}

    static Map<Class, String> NECESSARY_MIXIN_TARGETS = Map.of(FilePackResources, 'FilePackResources')

    static void init() {
        MixinStatuses.setupNecessaryMixins(NECESSARY_MIXIN_TARGETS)

        if (Services.PLATFORM.client) {
            JsonWranglerClient.init()
        }
    }

    static final Map sharedEnvMap = Collections.unmodifiableMap(['platform':switch (Services.PLATFORM.platform) {
        case IPlatformHelper.Platform.FORGE -> 'forge'
        case IPlatformHelper.Platform.QUILT -> 'quilt'
    }])
}
