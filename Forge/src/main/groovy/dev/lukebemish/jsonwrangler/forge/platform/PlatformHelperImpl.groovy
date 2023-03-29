package dev.lukebemish.jsonwrangler.forge.platform

import com.google.auto.service.AutoService
import dev.lukebemish.jsonwrangler.services.IPlatformHelper
import groovy.transform.CompileStatic
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.loading.FMLLoader
import net.minecraftforge.fml.loading.FMLPaths
import org.codehaus.groovy.control.CompilerConfiguration

import java.nio.file.Path

@AutoService(IPlatformHelper)
@CompileStatic
class PlatformHelperImpl implements IPlatformHelper {

    @Override
    boolean isDevelopmentEnvironment() {
        return !FMLLoader.production
    }

    @Override
    boolean isClient() {
        return FMLLoader.dist == Dist.CLIENT
    }

    @Override
    Path getConfigFolder() {
        return FMLPaths.CONFIGDIR.get()
    }

    @Override
    Platform getPlatform() {
        return Platform.FORGE
    }

    @Override
    void customize(CompilerConfiguration compilerConfiguration) {
        // Does nothing
    }
}
