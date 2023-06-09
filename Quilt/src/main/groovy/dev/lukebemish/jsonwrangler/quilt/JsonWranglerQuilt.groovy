/*
 * Copyright (C) 2023 Luke Bemish and contributors
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */

package dev.lukebemish.jsonwrangler.quilt


import dev.lukebemish.jsonwrangler.JsonWranglerCommon
import groovy.transform.CompileStatic
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer

@CompileStatic
class JsonWranglerQuilt implements ModInitializer {

    @Override
    void onInitialize(ModContainer mod) {
        JsonWranglerCommon.init()
    }
}
