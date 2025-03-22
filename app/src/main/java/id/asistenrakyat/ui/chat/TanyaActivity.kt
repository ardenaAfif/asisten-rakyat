package id.asistenrakyat.ui.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import id.asistenrakyat.BuildConfig
import id.asistenrakyat.databinding.ActivityTanyaBinding
import id.asistenrakyat.utils.PelayananInfo.bpjsInfo
import id.asistenrakyat.utils.PelayananInfo.imigrasiInfo
import id.asistenrakyat.utils.PelayananInfo.kependudukanInfo
import id.asistenrakyat.utils.PelayananInfo.pajakInfo
import id.asistenrakyat.utils.PelayananInfo.pdamInfo
import id.asistenrakyat.utils.PelayananInfo.plnInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TanyaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTanyaBinding

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTanyaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chatResponse()
    }

    private fun chatResponse() {
        // Todo here is the code for chat response
    }

    // Tanya Pajak
    private fun getPajakResponse(userInput: String, scope: CoroutineScope, onResponse: (String) -> Unit) {
        scope.launch(Dispatchers.IO) {
            val chatHistory = listOf(
            )

            val chat = model.startChat(chatHistory)

            val prompt = """
            Anda adalah chatbot layanan instansi pemerintah bernama Asra. Jawab pertanyaan berdasarkan informasi berikut:
            $pajakInfo

            Jika pertanyaan di luar topik pajak, jawab dengan sopan bahwa Anda hanya bisa menjawab terkait pajak.

            Pertanyaan Pengguna: $userInput
        """.trimIndent()

            try {
                val response = chat.sendMessage(prompt)
                val answer = response.text ?: "Maaf, terjadi kesalahan."
                scope.launch(Dispatchers.Main) {
                    onResponse(answer)
                }
            } catch (e: Exception) {
                scope.launch(Dispatchers.Main) {
                    onResponse("Maaf, terjadi kesalahan. Silakan coba lagi.")
                }
            }
        }
    }

    // Tanya PBJS
    private fun getBpjsResponse(userInput: String, scope: CoroutineScope, onResponse: (String) -> Unit) {
        scope.launch(Dispatchers.IO) {
            val chatHistory = listOf(
            )

            val chat = model.startChat(chatHistory)

            val prompt = """
            Anda adalah chatbot layanan instansi pemerintah bernama Asra. Jawab pertanyaan berdasarkan informasi berikut:
            $bpjsInfo

            Jika pertanyaan di luar topik BPJS, jawab dengan sopan bahwa Anda hanya bisa menjawab terkait BPJS.

            Pertanyaan Pengguna: $userInput
        """.trimIndent()

            try {
                val response = chat.sendMessage(prompt)
                val answer = response.text ?: "Maaf, terjadi kesalahan."
                scope.launch(Dispatchers.Main) {
                    onResponse(answer)
                }
            } catch (e: Exception) {
                scope.launch(Dispatchers.Main) {
                    onResponse("Maaf, terjadi kesalahan. Silakan coba lagi.")
                }
            }
        }
    }

    // Tanya PDAM
    private fun getPdamResponse(userInput: String, scope: CoroutineScope, onResponse: (String) -> Unit) {
        scope.launch(Dispatchers.IO) {
            val chatHistory = listOf(
            )

            val chat = model.startChat(chatHistory)

            val prompt = """
            Anda adalah chatbot layanan instansi pemerintah bernama Asra. Jawab pertanyaan berdasarkan informasi berikut:
            $pdamInfo

            Jika pertanyaan di luar topik PDAM, jawab dengan sopan bahwa Anda hanya bisa menjawab terkait PDAM.

            Pertanyaan Pengguna: $userInput
        """.trimIndent()

            try {
                val response = chat.sendMessage(prompt)
                val answer = response.text ?: "Maaf, terjadi kesalahan."
                scope.launch(Dispatchers.Main) {
                    onResponse(answer)
                }
            } catch (e: Exception) {
                scope.launch(Dispatchers.Main) {
                    onResponse("Maaf, terjadi kesalahan. Silakan coba lagi.")
                }
            }
        }
    }

    // Tanya Imigrasi
    private fun getImigrasiResponse(userInput: String, scope: CoroutineScope, onResponse: (String) -> Unit) {
        scope.launch(Dispatchers.IO) {
            val chatHistory = listOf(
            )

            val chat = model.startChat(chatHistory)

            val prompt = """
            Anda adalah chatbot layanan instansi pemerintah bernama Asra. Jawab pertanyaan berdasarkan informasi berikut:
            $imigrasiInfo

            Jika pertanyaan di luar topik imigrasi, jawab dengan sopan bahwa Anda hanya bisa menjawab terkait imigrasi.

            Pertanyaan Pengguna: $userInput
        """.trimIndent()

            try {
                val response = chat.sendMessage(prompt)
                val answer = response.text ?: "Maaf, terjadi kesalahan."
                scope.launch(Dispatchers.Main) {
                    onResponse(answer)
                }
            } catch (e: Exception) {
                scope.launch(Dispatchers.Main) {
                    onResponse("Maaf, terjadi kesalahan. Silakan coba lagi.")
                }
            }
        }
    }

    // Tanya Kependudukan
    private fun getKependudukanResponse(userInput: String, scope: CoroutineScope, onResponse: (String) -> Unit) {
        scope.launch(Dispatchers.IO) {
            val chatHistory = listOf(
            )

            val chat = model.startChat(chatHistory)

            val prompt = """
            Anda adalah chatbot layanan instansi pemerintah bernama Asra. Jawab pertanyaan berdasarkan informasi berikut:
            $kependudukanInfo

            Jika pertanyaan di luar topik kependudukan, jawab dengan sopan bahwa Anda hanya bisa menjawab terkait kependudukan.

            Pertanyaan Pengguna: $userInput
        """.trimIndent()

            try {
                val response = chat.sendMessage(prompt)
                val answer = response.text ?: "Maaf, terjadi kesalahan."
                scope.launch(Dispatchers.Main) {
                    onResponse(answer)
                }
            } catch (e: Exception) {
                scope.launch(Dispatchers.Main) {
                    onResponse("Maaf, terjadi kesalahan. Silakan coba lagi.")
                }
            }
        }
    }

    // Tanya PLN
    private fun getPlnResponse(userInput: String, scope: CoroutineScope, onResponse: (String) -> Unit) {
        scope.launch(Dispatchers.IO) {
            val chatHistory = listOf(
            )

            val chat = model.startChat(chatHistory)

            val prompt = """
            Anda adalah chatbot layanan instansi pemerintah bernama Asra. Jawab pertanyaan berdasarkan informasi berikut:
            $plnInfo

            Jika pertanyaan di luar topik PLN, jawab dengan sopan bahwa Anda hanya bisa menjawab terkait PLN.

            Pertanyaan Pengguna: $userInput
        """.trimIndent()

            try {
                val response = chat.sendMessage(prompt)
                val answer = response.text ?: "Maaf, terjadi kesalahan."
                scope.launch(Dispatchers.Main) {
                    onResponse(answer)
                }
            } catch (e: Exception) {
                scope.launch(Dispatchers.Main) {
                    onResponse("Maaf, terjadi kesalahan. Silakan coba lagi.")
                }
            }
        }
    }
}