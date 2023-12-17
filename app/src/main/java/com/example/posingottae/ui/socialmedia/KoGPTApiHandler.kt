package com.example.posingottae.ui.socialmedia

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*


@Serializable
data class KoGPTApiResponse(val text: String)

class KoGPTApiHandler (private val apiKey: String)  {

    private val json = Json { ignoreUnknownKeys = true }
    fun performKoGPTApiCall(prompt: String): KoGPTApiResponse {
        val url = "https://api.kakaobrain.com/v1/inference/kogpt/generation"
        val headers = mapOf("Authorization" to "KakaoAK $apiKey")

        val body = KoGPTApiRequest( prompt, max_tokens =128, temperature = 0.2)
        val (_, _, result) = Fuel.post(url)
            .header(headers)
            .jsonBody(json.encodeToString(body))
            .responseString()
        val responseJson = json.parseToJsonElement(result.get())
        val text = responseJson.jsonObject["generations"]?.jsonArray?.get(0)?.jsonObject?.get("text")?.jsonPrimitive?.contentOrNull
        return KoGPTApiResponse(text.orEmpty())
    }
}
@Serializable
data class KoGPTApiRequest(
    val prompt: String,
    val max_tokens: Int,
    val temperature: Double)
