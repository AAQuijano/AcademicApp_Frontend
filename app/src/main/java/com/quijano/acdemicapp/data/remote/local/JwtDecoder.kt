package com.quijano.acdemicapp.data.remote.local

import android.util.Base64
import org.json.JSONObject

object JwtDecoder {
    fun decode(token: String): Map<String, Any> {
        val parts = token.split(".")
        val payload = String(Base64.decode(parts[1], Base64.URL_SAFE), Charsets.UTF_8)
        return JSONObject(payload).toMap()
    }

    private fun JSONObject.toMap(): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        keys().forEach { key ->
            map[key] = get(key)
        }
        return map
    }
}