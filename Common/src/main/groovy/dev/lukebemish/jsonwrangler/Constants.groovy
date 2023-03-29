package dev.lukebemish.jsonwrangler


import com.google.gson.Gson
import com.google.gson.GsonBuilder
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@CompileStatic
class Constants {
    private Constants() {}

    public static final String MOD_ID = "jsonwrangler"
    public static final String MOD_NAME = "Json Wrangler"
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME)
    public static final Gson GSON = new GsonBuilder().setLenient().setPrettyPrinting().create()
}
