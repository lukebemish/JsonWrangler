/*
 * Copyright (C) 2023 Luke Bemish and contributors
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */

package dev.lukebemish.jsonwrangler.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.lukebemish.jsonwrangler.CanBeServerSource;
import dev.lukebemish.jsonwrangler.MixinStatuses;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.resources.DownloadedPackSource;
import net.minecraft.server.packs.FilePackResources;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DownloadedPackSource.class)
public class DownloadedPackSourceMixin {
    @ModifyExpressionValue(
        method = {"method_4637", "lambda$setServerPack$8", "m_254765_"},
        at = @At(
            value = "NEW",
            target = "net/minecraft/server/packs/FilePackResources"
        ),
        require = 1
    )
    private static FilePackResources jsonwrangler$modifyFilePackResources(FilePackResources old) {
        ((CanBeServerSource) old).jsonwrangler$setServerSource();
        return old;
    }

    @Inject(
        method = "<clinit>",
        at = @At("RETURN")
    )
    private static void setupVerify(CallbackInfo ci) {
        MixinStatuses.MIXIN_STATUSES.add("DownloadedPackSource");
    }
}
