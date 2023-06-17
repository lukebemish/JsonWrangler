/*
 * Copyright (C) 2023 Luke Bemish and contributors
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */

package dev.lukebemish.jsonwrangler.forge

import org.groovymc.gml.GMod
import dev.lukebemish.jsonwrangler.Constants
import dev.lukebemish.jsonwrangler.JsonWranglerCommon
import groovy.transform.CompileStatic

@GMod(Constants.MOD_ID)
@CompileStatic
class JsonWranglerForge {
    JsonWranglerForge() {
        JsonWranglerCommon.init()
    }
}
