package br.digitalhouse.cookbook.ui.signin.view

import android.content.IntentFilter
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.digitalhouse.cookbook.R
import br.digitalhouse.cookbook.data.utils.NetworkReceiver
import br.digitalhouse.cookbook.databinding.ActivityPdfViewerBinding
import com.danjdt.pdfviewer.PdfViewer
import com.danjdt.pdfviewer.interfaces.OnErrorListener
import com.danjdt.pdfviewer.interfaces.OnPageChangedListener
import com.danjdt.pdfviewer.utils.PdfPageQuality
import com.danjdt.pdfviewer.view.PdfViewerRecyclerView
import java.io.IOException
import java.lang.Exception

class PdfViewerActivity : AppCompatActivity() {
    private val binding: ActivityPdfViewerBinding by lazy { ActivityPdfViewerBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setOnClickListener()
        pdfViewer()
    }

    private fun setOnClickListener() {
        binding.btnBack.setOnClickListener{
            finish()
        }
    }

    private fun pdfViewer(){
        PdfViewer.Builder(binding.rootView)
            .view(PdfViewerRecyclerView(this))
            .quality(PdfPageQuality.QUALITY_1080)
            .setZoomEnabled(true)
            .setMaxZoom(3f)
            .setOnPageChangedListener(object : OnPageChangedListener {
                override fun onPageChanged(page: Int, total: Int) {
                    binding.tvCounter.text = getString(R.string.pdf_page_counter, page, total)
                }

            })
            .setOnErrorListener(object : OnErrorListener {
                override fun onAttachViewError(e: Exception) {
                }

                override fun onFileLoadError(e: Exception) {
                }

                override fun onPdfRendererError(e: IOException) {
                }

            })
            .build()
            .load(R.raw.pdftermos)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)
        registerReceiver(NetworkReceiver(), intentFilter)
    }
}