package com.developerobaida.learningacademy

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp(this)
        }

//        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this, listOf( String(Manifest.permission.WRITE_EXTERNAL_STORAGE),),100)
//        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(context: Context) {

    val title : String = context.getString(R.string.app_name)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = title)
                } },

            )
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            TopBanner()
            GridList()
        }
    }
}

@Composable
fun TopBanner(){
    Image(
        painter = painterResource(id = R.drawable.bacground__),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun GridList() {
    val context = LocalContext.current
    val items = listOf(
        Triple("মাখরাজ চিত্র সহ","https://drive.google.com/uc?export=download&id=1RR093Xg-xBETO3s_0izGJt-rFmG2kCDu","makhraj.pdf"),
        Triple("নুরানী পদ্ধতি","https://drive.google.com/uc?export=download&id=1tlYWvjUyjafjM4sGwtA3bD-YVuyd2UNw","nurani_poddhoti.pdf"),
        Triple( "নাদিয়াতুল কুরআন কায়দা","https://drive.google.com/uc?export=download&id=1QcNhBmThglh06TyfgSAXKsQkOK-i6hVy","nadiyatul_quran.pdf"),
        Triple("আমপারা","https://drive.google.com/uc?export=download&id=1n8ihDVSvENQUV4-0O0j4EJiRfAuRnRh6","nadiya_ampara.pdf"),
        Triple( "আল কুরআন","https://drive.google.com/uc?export=download&id=1biE4dyXQVaLdAXPhR42ZWJ52u3PuSGSQ","al_quran.pdf")
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        itemsIndexed(items) {index,(title,url,fileName) ->
            GridItem(title,url, fileName) {
                val intent = Intent(context, PdfActivity::class.java)
                if (index == 0){
                    intent.putExtra("title", title)
                    intent.putExtra("data", index)
                    context.startActivity(intent)
                }else if (index ==1){
                    intent.putExtra("title", title)
                    intent.putExtra("data", index)
                    context.startActivity(intent)
                }else if (index == 2){
                    intent.putExtra("title", title)
                    intent.putExtra("data", index)
                    context.startActivity(intent)
                }else if (index == 3){
                    intent.putExtra("title", title)
                    intent.putExtra("data", index)
                    context.startActivity(intent)
                }else if (index == 4){
                    intent.putExtra("title", title)
                    intent.putExtra("data", index)
                    context.startActivity(intent)
                }

            }
        }
    }
}

@Composable
fun GridItem(title: String, url: String, fileName: String, onClick: (title: String) -> Unit) {
    val context = LocalContext.current
    val bitmapState = remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(fileName) {
        getFirstPageAsBitmapFromUrl(url, fileName, context) { bitmap ->
            bitmapState.value = bitmap
        }
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(160.dp)
            .clickable { onClick(title) },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Spacer(Modifier.size(8.dp))

            bitmapState.value?.let { bmp ->
                Image(
                    bitmap = bmp.asImageBitmap(),
                    contentDescription = title,
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Crop
                )
            } ?: run {
                CircularProgressIndicator()
            }
            Spacer(Modifier.size(8.dp))
            Text(
                text = title,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}


fun getFirstPageAsBitmapFromUrl(
    pdfUrl: String,
    fileName: String,
    context: Context,
    onBitmapReady: (Bitmap?) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val cacheDir = File(context.cacheDir, "pdf_cache")
            if (!cacheDir.exists()) cacheDir.mkdirs()

            val cachedImageFile = File(cacheDir, "$fileName.png")

            // Check if cached image exists
            if (cachedImageFile.exists()) {
                val bitmap = BitmapFactory.decodeFile(cachedImageFile.absolutePath)
                onBitmapReady(bitmap)
                return@launch
            }

            val url = URL(pdfUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.doInput = true
            connection.connect()

            val inputStream = connection.inputStream
            val tempFile = File.createTempFile("temp_pdf", ".pdf", context.cacheDir)
            tempFile.outputStream().use { output -> inputStream.copyTo(output) }
            inputStream.close()

            val bitmap = renderFirstPageFromFile(tempFile)

            // Save bitmap to cache
            bitmap?.let {
                cachedImageFile.outputStream().use { output ->
                    it.compress(Bitmap.CompressFormat.PNG, 100, output)
                }
            }

            onBitmapReady(bitmap)

            tempFile.delete()
        } catch (e: Exception) {
            e.printStackTrace()
            onBitmapReady(null)
        }
    }
}

fun renderFirstPageFromFile(file: File): Bitmap? {
    return try {
        val parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        val pdfRenderer = PdfRenderer(parcelFileDescriptor)
        val page = pdfRenderer.openPage(0)

        val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

        page.close()
        pdfRenderer.close()
        parcelFileDescriptor.close()

        bitmap
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}