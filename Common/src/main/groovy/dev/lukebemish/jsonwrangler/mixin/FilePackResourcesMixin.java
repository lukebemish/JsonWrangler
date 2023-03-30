/*
 * Copyright (C) 2023 Luke Bemish and contributors
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */

package dev.lukebemish.jsonwrangler.mixin;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.lukebemish.jsonwrangler.CanBeServerSource;
import dev.lukebemish.jsonwrangler.ResourceMutator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.resources.IoSupplier;

@Mixin(FilePackResources.class)
public class FilePackResourcesMixin implements CanBeServerSource {
    @Unique
    private boolean jsonwrangler$serverSource = false;

    @ModifyReturnValue(
        method = "getResource(Ljava/lang/String;)Lnet/minecraft/server/packs/resources/IoSupplier;",
        at = @At("RETURN")
    )
    private IoSupplier<InputStream> jsonwrangler$getResource(IoSupplier<InputStream> old, String resourcePath) {
        if (this.jsonwrangler$serverSource && resourcePath.endsWith(".groovy")) {
            return () -> {
                InputStream prepend = new ByteArrayInputStream((ResourceMutator.SERVER_SCRIPT+"\n").getBytes());
                InputStream stream = old.get();
                return new SequenceInputStream(prepend, stream);
            };
        }
        return old;
    }

    @Override
    public void jsonwrangler$setServerSource() {
        this.jsonwrangler$serverSource = true;
    }

    @WrapOperation(
        method = "listResources",
        at = @At(
            value = "INVOKE",
            target ="Lnet/minecraft/server/packs/PackResources$ResourceOutput;accept(Ljava/lang/Object;Ljava/lang/Object;)V"
        )
    )
    private void jsonwrangler$listResources(PackResources.ResourceOutput output, Object objLocation, Object objIoSupplier, Operation<Void> operation) {
        var location = (ResourceLocation) objLocation;
        var ioSupplier = (IoSupplier<InputStream>) objIoSupplier;
        if (this.jsonwrangler$serverSource && location.getPath().endsWith(".groovy")) {
            IoSupplier<InputStream> oldIoSupplier = ioSupplier;
            ioSupplier = () -> {
                InputStream prepend = new ByteArrayInputStream((ResourceMutator.SERVER_SCRIPT+"\n").getBytes());
                InputStream stream = oldIoSupplier.get();
                return new SequenceInputStream(prepend, stream);
            };
        }
        operation.call(output, location, ioSupplier);
    }
}
