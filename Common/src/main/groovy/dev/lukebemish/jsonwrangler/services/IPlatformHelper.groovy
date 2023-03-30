/*
 * Copyright (C) 2023 Luke Bemish and contributors
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */

package dev.lukebemish.jsonwrangler.services

import groovy.transform.CompileStatic
import org.codehaus.groovy.control.CompilerConfiguration

import java.nio.file.Path

@CompileStatic
interface IPlatformHelper {
    boolean isDevelopmentEnvironment();

    boolean isClient()

    Path getConfigFolder()

    Platform getPlatform()

    void customize(CompilerConfiguration compilerConfiguration)

    enum Platform {
        FORGE,
        QUILT
    }
}
