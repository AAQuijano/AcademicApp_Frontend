package com.quijano.acdemicapp.utils

import android.util.Base64
import org.json.JSONObject

object JwtDecoder {
    fun decode(token: String): Map<String, String> {
        return try {
            val parts = token.split(".")
            if (parts.size < 2) return emptyMap()
            val payload = parts[1]
            val decodedBytes = Base64.decode(payload, Base64.URL_SAFE)
            val json = JSONObject(String(decodedBytes))
            json.keys().asSequence().associateWith { key ->
                json.get(key).toString()
            }
        } catch (e: Exception) {
            emptyMap()
        }
    }
}