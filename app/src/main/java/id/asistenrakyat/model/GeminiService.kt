package id.asistenrakyat.model

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import id.asistenrakyat.BuildConfig

class GeminiService {
    private val model = GenerativeModel(
        "gemini-1.5-pro",
        BuildConfig.geminiApiKey,
        generationConfig = generationConfig {
            temperature = 1f
            topK = 40
            topP = 0.95f
            maxOutputTokens = 8192
            responseMimeType = "text/plain"
        },
    )
}