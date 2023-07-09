/*
 * Copyright (C) 2023 Luke Bemish and contributors
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */

package dev.lukebemish.jsonwrangler

import net.minecraft.client.resources.DownloadedPackSource

class JsonWranglerClient {
    static Map<Class, String> NECESSARY_MIXIN_TARGETS = Map.of(DownloadedPackSource, 'DownloadedPackSource')

    static void init() {
        MixinStatuses.setupNecessaryMixins(NECESSARY_MIXIN_TARGETS)
    }
}
