package id.asistenrakyat.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import id.asistenrakyat.R
import id.asistenrakyat.databinding.ActivityMainBinding
import id.asistenrakyat.ui.chat.TanyaActivity
import id.asistenrakyat.utils.FormatHelper
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    private var currentImageIndex = 0

    private val imageResources = arrayOf(
        R.drawable.indonesia1,
        R.drawable.indonesia2,
        R.drawable.indonesia3
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startRealtimeClock()
        setupWelcomingText()
        imageSwitcher()
        menuListener()
    }

    private fun menuListener() {
        binding.apply {
            menuPajak.setOnClickListener {
                val intent = Intent(this@MainActivity, TanyaActivity::class.java)
                intent.putExtra("category", "pajak")
                startActivity(intent)
            }
            menuTanyaBpjs.setOnClickListener {
                val intent = Intent(this@MainActivity, TanyaActivity::class.java)
                intent.putExtra("category", "bpjs")
                startActivity(intent)
            }
            menuTanyaPdam.setOnClickListener {
                val intent = Intent(this@MainActivity, TanyaActivity::class.java)
                intent.putExtra("category", "pdam")
                startActivity(intent)
            }
            menuTanyaImigrasi.setOnClickListener {
                val intent = Intent(this@MainActivity, TanyaActivity::class.java)
                intent.putExtra("category", "imigrasi")
                startActivity(intent)
            }
            menuTanyaKtp.setOnClickListener {
                val intent = Intent(this@MainActivity, TanyaActivity::class.java)
                intent.putExtra("category", "ktp")
                startActivity(intent)
            }
            menuTanyaPln.setOnClickListener {
                val intent = Intent(this@MainActivity, TanyaActivity::class.java)
                intent.putExtra("category", "pln")
                startActivity(intent)
            }
        }
    }

    private fun imageSwitcher() {
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                // Animasi fade-out
                binding.ivBannerHome.animate()
                    .alpha(0f)
                    .setDuration(3000)
                    .withEndAction {
                        // Ganti gambar setelah fade-out
                        currentImageIndex = (currentImageIndex + 1) % imageResources.size
                        binding.ivBannerHome.setImageResource(imageResources[currentImageIndex])

                        // Animasi fade-in
                        binding.ivBannerHome.animate()
                            .alpha(1f)
                            .setDuration(3000)
                            .start()
                    }
                    .start()

                handler.postDelayed(this, 20000)
            }
        }
        handler.post(runnable)
    }

    private fun startRealtimeClock() {
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                binding.tvTimeNow.text = getString(R.string.time_now, FormatHelper.formatDate())
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)
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

    override fun onDestroy() {
        super.onDestroy()
    }
}