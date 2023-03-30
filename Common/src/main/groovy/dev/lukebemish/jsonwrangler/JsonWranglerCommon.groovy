/*
 * Copyright (C) 2023 Luke Bemish and contributors
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */

package dev.lukebemish.jsonwrangler


import dev.lukebemish.jsonwrangler.services.IPlatformHelper
import dev.lukebemish.jsonwrangler.services.Services
import groovy.transform.CompileStatic

@CompileStatic
final class JsonWranglerCommon {
    private JsonWranglerCommon() {}

    static void init() {

    }

    static final Map sharedEnvMap = Collections.unmodifiableMap(['platform':switch (Services.PLATFORM.platform) {
        case IPlatformHelper.Platform.FORGE -> 'forge'
        case IPlatformHelper.Platform.QUILT -> 'quilt'
    }])
}
