package dev.lukebemish.jsonwrangler.forge

import com.matyrobbrt.gml.GMod
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
