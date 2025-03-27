package id.asistenrakyat.ui.about

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import id.asistenrakyat.R
import id.asistenrakyat.databinding.ActivityPrivacyBinding

class PrivacyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrivacyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbarSetup()
        setupWebview()
    }

    private fun setupWebview() {
        val webView = binding.webView
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            setSupportZoom(true)
        }

        // Muat link Google Docs
        binding.webView.loadDataWithBaseURL(
            null,
            "<iframe src='https://docs.google.com/document/d/1vFEUBOBbREK-6Plr6Y4NglXglP7A7YItQmSmyOKK9Kk/preview' width='100%' height='100%'></iframe>",
            "text/html",
            "UTF-8",
            null
        )

//        webView.loadUrl("https://docs.google.com/document/d/1vFEUBOBbREK-6Plr6Y4NglXglP7A7YItQmSmyOKK9Kk/preview")
    }

    private fun toolbarSetup() {
        binding.toolbar.apply {
            tvToolbarName.text = "Privacy & Policy"
            navBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }
}