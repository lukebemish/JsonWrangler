package dev.lukebemish.jsonwrangler

import net.minecraft.client.resources.DownloadedPackSource
import net.minecraft.server.packs.FilePackResources

class JsonWranglerClient {
    static Map<Class, String> NECESSARY_MIXIN_TARGETS = Map.of(DownloadedPackSource, 'DownloadedPackSource')

    static void init() {
        MixinStatuses.setupNecessaryMixins(NECESSARY_MIXIN_TARGETS)
    }
}
