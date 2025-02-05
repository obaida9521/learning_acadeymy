package com.developerobaida.learningacademy

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.rizzi.bouquet.HorizontalPDFReader
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.rememberHorizontalPdfReaderState
import java.io.File

class PdfActivity : ComponentActivity() {
    var fileName = "nadiyatul_kayda.pdf"
    private var pdfUri: Uri? = null
    var pdfUrl: String = ""
    var title =""
    private var showProgressDialog by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = intent.getStringExtra("title") ?: "Not found"
        val  data = intent.getStringExtra("data")

        if (data!!.contains("makhraj.pdf")){
            fileName = "makhraj.pdf"
            pdfUrl = "https://drive.google.com/uc?export=download&id=1RR093Xg-xBETO3s_0izGJt-rFmG2kCDu"

        }else if (data!!.contains("nurani_poddhoti.pdf")){
            fileName = "nurani_poddhoti.pdf"
            pdfUrl = "https://drive.google.com/uc?export=download&id=1tlYWvjUyjafjM4sGwtA3bD-YVuyd2UNw"
        }else if (data!!.contains("nadiyatul_quran.pdf")){
            fileName = "nadiyatul_quran.pdf"
            pdfUrl = "https://drive.google.com/uc?export=download&id=1QcNhBmThglh06TyfgSAXKsQkOK-i6hVy"
        }else if (data!!.contains("nadiya_ampara.pdf")){

            fileName = "nadiya_ampara.pdf"
            pdfUrl = "https://drive.google.com/uc?export=download&id=1n8ihDVSvENQUV4-0O0j4EJiRfAuRnRh6"

        }else if (data!!.contains("al_quran.pdf")){

            fileName = "al_quran.pdf"
            pdfUrl = "https://drive.google.com/uc?export=download&id=1biE4dyXQVaLdAXPhR42ZWJ52u3PuSGSQ"
        }

        setContent {
            val showProgress = remember { mutableStateOf(true) }
            val pdfUriState = remember { mutableStateOf<Uri?>(null) }

            LaunchedEffect(Unit) {
                val file = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)
                if (file.exists()) {
                    pdfUriState.value = Uri.fromFile(file)
                    showProgress.value = false
                } else {
                    downloadPdf(this@PdfActivity, pdfUrl, fileName) { uri ->
                        pdfUriState.value = uri
                        showProgress.value = false
                    }
                }
            }

            DetailScreen(title = title, pdfUriState.value, showProgress.value, onBackPressed = { finish() })
        }

    }
}
fun downloadPdf(context: Context, url: String, fileName: String, onDownloadComplete: (Uri?) -> Unit) {
    val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)

    if (file.exists()) {
        onDownloadComplete(Uri.fromFile(file))
        return
    }

    val request = DownloadManager.Request(Uri.parse(url))
        .setTitle(fileName)
        .setDescription("Downloading PDF...")
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setDestinationUri(Uri.fromFile(file))

    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val downloadId = downloadManager.enqueue(request)

    val query = DownloadManager.Query().setFilterById(downloadId)
    val handler = Handler(Looper.getMainLooper())
    handler.postDelayed(object : Runnable {
        override fun run() {
            val cursor = downloadManager.query(query)
            if (cursor.moveToFirst()) {
                val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    onDownloadComplete(Uri.fromFile(file))
                    return
                }
            }
            cursor.close()
            handler.postDelayed(this, 1000)
        }
    }, 1000)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(title: String, fileUri: Uri?, showProgressDialog: Boolean,onBackPressed: () -> Unit) {
    if (showProgressDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Downloading...") },
            text = { CircularProgressIndicator() },
            confirmButton = {}
        )
    }

    Scaffold(
        topBar = { TopAppBar(
            title = { Text(title) },
            navigationIcon = {
                IconButton(onClick = { onBackPressed() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        ) }
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            fileUri?.let { uri ->
                val pdfState = rememberHorizontalPdfReaderState(
                    resource = ResourceType.Local(uri = uri),
                    isZoomEnable = true
                )
                HorizontalPDFReader(
                    state = pdfState,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                )
            } ?: Text("Downloading PDF...")
        }
    }
}
