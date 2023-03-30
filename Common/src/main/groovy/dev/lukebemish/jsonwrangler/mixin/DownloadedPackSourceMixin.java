/*
 * Copyright (C) 2023 Luke Bemish and contributors
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */

package dev.lukebemish.jsonwrangler.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.lukebemish.jsonwrangler.CanBeServerSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.resources.DownloadedPackSource;
import net.minecraft.server.packs.FilePackResources;

@Mixin(DownloadedPackSource.class)
public class DownloadedPackSourceMixin {
    @ModifyExpressionValue(
        method = {"method_4637", "lambda$setServerPack$8"},
        at = @At(
            value = "NEW",
            target = "net/minecraft/server/packs/FilePackResources"
        )
    )
    private static FilePackResources jsonwrangler$modifyFilePackResources(FilePackResources old) {
        ((CanBeServerSource) old).jsonwrangler$setServerSource();
        return old;
    }
}
