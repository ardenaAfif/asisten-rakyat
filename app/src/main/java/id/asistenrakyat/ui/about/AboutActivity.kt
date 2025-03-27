package id.asistenrakyat.ui.about

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import id.asistenrakyat.R
import id.asistenrakyat.databinding.ActivityAboutBinding
import java.util.Calendar

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbarSetup()
        menuListener()
        setupWelcomingText()
    }

    private fun menuListener() {
        binding.apply {
            layoutPrivacyPolicy.setOnClickListener {
                startActivity(Intent(this@AboutActivity, PrivacyActivity::class.java))
            }
            layoutSourceInfo.setOnClickListener {
                showSourceInfoBottomSheet()
            }
        }
    }

    private fun showSourceInfoBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_source_info, null)

        view.findViewById<TextView>(R.id.tvSourceInfoTitle).text = "Sumber Data Informasi Layanan Publik"

        view.findViewById<TextView>(R.id.tvSourceInfoContent).text = """
            Informasi layanan publik yang disediakan oleh Aplikasi bersumber dari situs web resmi instansi pemerintah berikut:
            - Direktorat Jenderal Pajak (pajak.go.id)
            - BPJS Kesehatan (bpjs-kesehatan.go.id)
            - Perusahaan Daerah Air Minum (PDAM) - (Sesuai dengan PDAM masing-masing daerah)
            - Direktorat Jenderal Imigrasi (imigrasi.go.id)
            - Direktorat Jenderal Kependudukan dan Pencatatan Sipil (dukcapil.kemendagri.go.id)
            - Perusahaan Listrik Negara (pln.co.id)
            
            Kami berupaya untuk selalu menyediakan informasi yang akurat dan terbaru, namun kami tidak menjamin keakuratan atau kelengkapan informasi yang disediakan.
        """.trimIndent()

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }

    private fun setupWelcomingText() {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val (welcomingText, emoji) = when (currentHour) {
            in 5..11 -> "Selamat Pagi" to "☀️"
            in 12..14 -> "Selamat Siang" to "🌞"
            in 15..17 -> "Selamat Sore" to "🌅"
            else -> "Selamat Malam" to "🌙"
        }

        binding.apply {
            binding.tvWelcomingNow.text = "$welcomingText  $emoji"
        }
    }

    private fun toolbarSetup() {
        binding.toolbar.apply {
            tvToolbarName.text = "Tentang Asra"
            navBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }
}