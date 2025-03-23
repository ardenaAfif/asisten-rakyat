package id.asistenrakyat.ui.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.generationConfig
import id.asistenrakyat.BuildConfig
import id.asistenrakyat.adapter.ChatAdapter
import id.asistenrakyat.databinding.ActivityTanyaBinding
import id.asistenrakyat.model.GeminiService
import id.asistenrakyat.utils.ChatMessage
import id.asistenrakyat.utils.PelayananInfo.bpjsInfo
import id.asistenrakyat.utils.PelayananInfo.imigrasiInfo
import id.asistenrakyat.utils.PelayananInfo.kependudukanInfo
import id.asistenrakyat.utils.PelayananInfo.pajakInfo
import id.asistenrakyat.utils.PelayananInfo.pdamInfo
import id.asistenrakyat.utils.PelayananInfo.plnInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TanyaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTanyaBinding

    private lateinit var chatAdapter: ChatAdapter

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

        chatAdapter = ChatAdapter()
        binding.rvChat.adapter = chatAdapter
        binding.rvChat.layoutManager = LinearLayoutManager(this)

        val category = intent.getStringExtra("category")

        binding.btnSend.setOnClickListener {
            val userMessage = binding.edtMessage.text.toString().trim()
            if (userMessage.isNotEmpty()) {
                chatAdapter.sendMessage(ChatMessage(userMessage, ChatMessage.Type.USER))
                binding.edtMessage.text.clear()
                sendUserMessageToGemini(userMessage, category)
            }
        }

        customToolbar()
    }

    private fun customToolbar() {
        binding.apply {
            toolbar.navBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun sendUserMessageToGemini(userInput: String, category: String?) {
        val scope = CoroutineScope(Dispatchers.Main)
        when (category) {
            "pajak" -> getPajakResponse(userInput, scope) { response ->
                chatAdapter.sendMessage(ChatMessage(response, ChatMessage.Type.BOT))
            }
            "bpjs" -> getBpjsResponse(userInput, scope) { response ->
                chatAdapter.sendMessage(ChatMessage(response, ChatMessage.Type.BOT))
            }
            "pdam" -> getPdamResponse(userInput, scope) { response ->
                chatAdapter.sendMessage(ChatMessage(response, ChatMessage.Type.BOT))
            }
            "imigrasi" -> getImigrasiResponse(userInput, scope) { response ->
                chatAdapter.sendMessage(ChatMessage(response, ChatMessage.Type.BOT))
            }
            "ktp" -> getKependudukanResponse(userInput, scope) { response ->
                chatAdapter.sendMessage(ChatMessage(response, ChatMessage.Type.BOT))
            }
            "pln" -> getPlnResponse(userInput, scope) { response ->
                chatAdapter.sendMessage(ChatMessage(response, ChatMessage.Type.BOT))
            }
            else -> {
                // Handle jika kategori tidak dikenali atau kosong
                val errorMessage = "Maaf, kategori tidak dikenali."
                chatAdapter.sendMessage(ChatMessage(errorMessage, ChatMessage.Type.BOT))
            }
        }
    }

    // Tanya Pajak
    private fun getPajakResponse(userInput: String, scope: CoroutineScope, onResponse: (String) -> Unit) {
        scope.launch(Dispatchers.IO) {
            val chatHistory = listOf<Content>()
            val chat = model.startChat(chatHistory)

            val pajakWebsiteInfo = """
            Informasi dari situs web resmi Direktorat Jenderal Pajak (pajak.go.id):
            - Cara membayar pajak kendaraan bermotor: [Informasi dari situs web]
            - Dokumen yang diperlukan untuk mengurus NPWP: [Informasi dari situs web]
            - Cara melaporkan SPT Tahunan: [Informasi dari situs web]
            - Tarif pajak penghasilan terbaru: [Informasi dari situs web]
            - Cara mengecek status pembayaran pajak: [Informasi dari situs web]
            - Jenis pajak di Indonesia: [Informasi dari situs web]
            - Cara menghitung PPh 21: [Informasi dari situs web]
            - Cara mendapatkan e-FIN: [Informasi dari situs web]
            - Cara mengubah data NPWP: [Informasi dari situs web]
            - Cara mengajukan keberatan pajak: [Informasi dari situs web]
            - Cara mengurus restitusi pajak: [Informasi dari situs web]
            - Cara membuat kode billing pajak: [Informasi dari situs web]
            - Cara melakukan pembayaran pajak melalui e-billing: [Informasi dari situs web]
            - Cara mendapatkan bukti potong pajak: [Informasi dari situs web]
            - Cara mengaktifkan kembali NPWP yang non-efektif: [Informasi dari situs web]
        """.trimIndent()

            val prompt = """
            Anda adalah chatbot layanan instansi pemerintah bernama Asra. Jawab pertanyaan berdasarkan informasi berikut:
            $pajakInfo
            $pajakWebsiteInfo

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

    // Tanya BPJS
    private fun getBpjsResponse(userInput: String, scope: CoroutineScope, onResponse: (String) -> Unit) {
        scope.launch(Dispatchers.IO) {
            val chatHistory = listOf<Content>()
            val chat = model.startChat(chatHistory)

            val bpjsWebsiteInfo = """
                Informasi dari situs web resmi BPJS Kesehatan (bpjs-kesehatan.go.id):
                - Cara mendaftar BPJS Kesehatan: [Informasi dari situs web]
                - Cara membayar iuran BPJS Kesehatan: [Informasi dari situs web]
                - Cara cek iuran BPJS Kesehatan: [Informasi dari situs web]
                - Cara mengurus kartu BPJS Kesehatan yang hilang: [Informasi dari situs web]
                - Cara mengubah data BPJS Kesehatan: [Informasi dari situs web]
                - Cara mengaktifkan kembali BPJS Kesehatan yang non-aktif: [Informasi dari situs web]
                - Cara mendapatkan pelayanan kesehatan dengan BPJS Kesehatan: [Informasi dari situs web]
                - Cara mengetahui FKTP terdaftar: [Informasi dari situs web]
                - Cara mengajukan klaim BPJS Kesehatan: [Informasi dari situs web]
                - Cara mengetahui jenis-jenis pelayanan yang ditanggung BPJS Kesehatan: [Informasi dari situs web]
                - Cara mengurus BPJS Ketenagakerjaan: [Informasi dari situs web]
                - Cara mengajukan klaim BPJS Ketenagakerjaan: [Informasi dari situs web]
                - Cara cek saldo JHT BPJS Ketenagakerjaan: [Informasi dari situs web]
                - Cara mendaftar BPJS Ketenagakerjaan secara online: [Informasi dari situs web]
                - Cara mengetahui program-program BPJS Ketenagakerjaan: [Informasi dari situs web]
            """.trimIndent()

            val prompt = """
                Anda adalah chatbot layanan instansi pemerintah bernama Asra. Jawab pertanyaan berdasarkan informasi berikut:
                $bpjsInfo
                $bpjsWebsiteInfo

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
            val chatHistory = listOf<Content>()
            val chat = model.startChat(chatHistory)

            val pdamWebsiteInfo = """
                Informasi dari situs web resmi PDAM (https://perumdamts.com/):
                - Cara mendaftar sambungan air baru: [Informasi dari situs web]
                - Cara melaporkan kebocoran air: [Informasi dari situs web]
                - Cara cek tagihan PDAM: [Informasi dari situs web]
                - Cara membayar tagihan PDAM online: [Informasi dari situs web]
                - Cara mengajukan keluhan PDAM: [Informasi dari situs web]
                - Cara mengubah nama pelanggan PDAM: [Informasi dari situs web]
                - Cara mengajukan pemasangan meteran air: [Informasi dari situs web]
                - Cara mengatasi air PDAM yang keruh: [Informasi dari situs web]
                - Cara mengetahui jadwal pengiriman air PDAM: [Informasi dari situs web]
                - Cara mengajukan pemasangan kembali PDAM yang diputus: [Informasi dari situs web]
                - Cara mengetahui kualitas air PDAM: [Informasi dari situs web]
                - Cara mengajukan pemasangan pipa PDAM baru: [Informasi dari situs web]
                - Cara mengajukan permohonan tangki air PDAM: [Informasi dari situs web]
                - Cara melaporkan pencurian air PDAM: [Informasi dari situs web]
                - Cara mengajukan penutupan sambungan air PDAM: [Informasi dari situs web]
            """.trimIndent()

            val prompt = """
                Anda adalah chatbot layanan instansi pemerintah bernama Asra. Jawab pertanyaan berdasarkan informasi berikut:
                $pdamInfo
                $pdamWebsiteInfo

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
            val chatHistory = listOf<Content>()

            val chat = model.startChat(chatHistory)

            val imigrasiWebsiteInfo = """
                Informasi dari situs web resmi Direktorat Jenderal Imigrasi (imigrasi.go.id):
                - Cara membuat paspor baru: [Informasi dari situs web]
                - Dokumen yang diperlukan untuk perpanjangan paspor: [Informasi dari situs web]
                - Biaya pembuatan paspor: [Informasi dari situs web]
                - Cara membuat paspor online: [Informasi dari situs web]
                - Cara cek status permohonan paspor: [Informasi dari situs web]
                - Jenis-jenis paspor: [Informasi dari situs web]
                - Cara mengurus visa: [Informasi dari situs web]
                - Persyaratan pembuatan paspor untuk anak: [Informasi dari situs web]
                - Cara melaporkan kehilangan paspor: [Informasi dari situs web]
                - Cara membuat KITAS: [Informasi dari situs web]
                - Cara membuat KITAP: [Informasi dari situs web]
                - Cara mengurus paspor yang rusak: [Informasi dari situs web]
                - Cara mengajukan permohonan visa on arrival (VOA): [Informasi dari situs web]
                - Cara mengajukan permohonan izin tinggal kunjungan: [Informasi dari situs web]
                - Cara melaporkan keberadaan orang asing: [Informasi dari situs web]
            """.trimIndent()

            val prompt = """
                Anda adalah chatbot layanan instansi pemerintah bernama Asra. Jawab pertanyaan berdasarkan informasi berikut:
                $imigrasiInfo
                $imigrasiWebsiteInfo

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
            val chatHistory = listOf<Content>()
            val chat = model.startChat(chatHistory)

            val kependudukanWebsiteInfo = """
                Informasi dari situs web resmi Dinas Kependudukan dan Pencatatan Sipil (dukcapil.kemendagri.go.id):
                - Cara membuat KTP baru: [Informasi dari situs web]
                - Dokumen yang diperlukan untuk membuat KK baru: [Informasi dari situs web]
                - Cara membuat akta kelahiran: [Informasi dari situs web]
                - Cara membuat akta kematian: [Informasi dari situs web]
                - Cara mengubah data KK: [Informasi dari situs web]
                - Cara membuat Kartu Identitas Anak (KIA): [Informasi dari situs web]
                - Cara mengurus surat pindah: [Informasi dari situs web]
                - Cara mengurus surat keterangan domisili: [Informasi dari situs web]
                - Cara mengurus legalisir dokumen kependudukan: [Informasi dari situs web]
                - Cara mengurus pembuatan e-KTP yang rusak atau hilang: [Informasi dari situs web]
                - Cara mengurus perubahan status perkawinan di KTP: [Informasi dari situs web]
                - Cara mengurus perubahan alamat di KTP dan KK: [Informasi dari situs web]
                - Cara mengurus pembuatan surat keterangan catatan kepolisian (SKCK): [Informasi dari situs web]
                - Cara mengurus surat keterangan tidak mampu (SKTM): [Informasi dari situs web]
                - Cara mengurus pembuatan surat keterangan ahli waris: [Informasi dari situs web]
            """.trimIndent()

            val prompt = """
            Anda adalah chatbot layanan instansi pemerintah bernama Asra. Jawab pertanyaan berdasarkan informasi berikut:
            $kependudukanInfo
            $kependudukanWebsiteInfo

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
            val chatHistory = listOf<Content>()
            val chat = model.startChat(chatHistory)

            val plnWebsiteInfo = """
                Informasi dari situs web resmi PLN (https://web.pln.co.id/):
                - Cara mendaftar pasang listrik baru: [Informasi dari situs web]
                - Cara melaporkan gangguan listrik: [Informasi dari situs web]
                - Cara cek tagihan listrik PLN: [Informasi dari situs web]
                - Cara membayar tagihan listrik PLN online: [Informasi dari situs web]
                - Cara mengajukan keluhan PLN: [Informasi dari situs web]
                - Cara mengubah daya listrik PLN: [Informasi dari situs web]
                - Cara mengajukan pemasangan meteran listrik: [Informasi dari situs web]
                - Cara mengatasi listrik mati: [Informasi dari situs web]
                - Cara mengetahui jadwal pemadaman listrik PLN: [Informasi dari situs web]
                - Cara mengajukan pemasangan kembali listrik PLN yang diputus: [Informasi dari situs web]
            """.trimIndent()

            val prompt = """
            Anda adalah chatbot layanan instansi pemerintah bernama Asra. Jawab pertanyaan berdasarkan informasi berikut:
            $plnInfo
            $plnWebsiteInfo

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