package id.asistenrakyat.model

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import id.asistenrakyat.BuildConfig

class GeminiService {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = BuildConfig.geminiApiKey
    )

    suspend fun getBotResponse(prompt: String): String {
        return try {
            val response = generativeModel.generateContent(content { text(prompt) })
            response.text ?: "Maaf, saya tidak mengerti pertanyaan Anda."
        } catch (e: Exception) {
            "Terjadi kesalahan, silakan coba lagi."
        }
    }
}