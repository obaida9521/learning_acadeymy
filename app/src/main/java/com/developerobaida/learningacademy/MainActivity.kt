package com.developerobaida.learningacademy

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
                    Text(text = title, fontWeight = FontWeight.Bold)
                } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFE3F3F3),
                    titleContentColor = Color.Blue
                )

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
@Preview
@Composable
fun TopBanner(){
    Image(
        painter = painterResource(id = R.drawable.cover),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(Modifier.height(8.dp))
    Text(
//        color = Color.Magenta,
        fontSize = 20.sp,
        text = "Yasin's Learning Academy যোগাযোগ : ০১৮৩২৪৩০১১২(Whatsapp)",
        maxLines = 1,
        modifier = Modifier.fillMaxWidth().basicMarquee(repeatDelayMillis = 1000, animationMode = MarqueeAnimationMode.Immediately)
    )
}
@Preview
@Composable
fun GridList() {
    val context = LocalContext.current
    val items = listOf(
        GridItemData.UrlItem("মাখরাজ চিত্র সহ", "https://drive.google.com/uc?export=download&id=1RR093Xg-xBETO3s_0izGJt-rFmG2kCDu", "makhraj.pdf"),
        GridItemData.UrlItem("নুরানী পদ্ধতি", "https://drive.google.com/uc?export=download&id=1tlYWvjUyjafjM4sGwtA3bD-YVuyd2UNw", "nurani_poddhoti.pdf"),
        GridItemData.UrlItem("নাদিয়াতুল কুরআন কায়দা", "https://drive.google.com/uc?export=download&id=1QcNhBmThglh06TyfgSAXKsQkOK-i6hVy", "nadiyatul_quran.pdf"),
        GridItemData.UrlItem("আমপারা", "https://drive.google.com/uc?export=download&id=1n8ihDVSvENQUV4-0O0j4EJiRfAuRnRh6", "nadiya_ampara.pdf"),
        GridItemData.UrlItem("আল কুরআন", "https://drive.google.com/uc?export=download&id=1biE4dyXQVaLdAXPhR42ZWJ52u3PuSGSQ", "al_quran.pdf"),

        GridItemData.DrawableItem("তাসবিহ", R.drawable.tasbih),
        GridItemData.DrawableItem("কমিউনিটি", R.drawable.people),
        GridItemData.DrawableItem("যোগাযোগ", R.drawable.baseline_call_24),
        GridItemData.DrawableItem("ইনবক্স", R.drawable.base_icon),
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(items) { item ->
            when (item) {
                is GridItemData.UrlItem -> GridItem(item.title, item.url, null, item.fileName) {
                    val intent = Intent(context, PdfActivity::class.java).apply {
                        putExtra("title", item.title)
                        putExtra("data", item.fileName)
                    }
                    context.startActivity(intent)
                }
                is GridItemData.DrawableItem -> GridItem(item.title, null, item.drawableRes, "") {

                    if (item.title.contains("তাসবিহ")){
                        val intent = Intent(context, TasbihActivity::class.java)
                        context.startActivity(intent)
                    }else if (item.title.contains("ইনবক্স")){

                        //val uri = Uri.parse("https://www.facebook.com/messages/e2ee/107393557700933")
                        val uri = Uri.parse("https://m.me/107393557700933")

                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        try {
                            context.startActivity(intent)
                        }catch (e : Exception){
                            val playStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.facebook.orca"))
                                context.startActivity(playStoreIntent)
                        }
                    }else if (item.title.contains("কমিউনিটি")){

                        val uri = Uri.parse("https://www.facebook.com/groups/3948835938704869/")
                        val intent = Intent(Intent.ACTION_VIEW, uri)

                        try {
                            context.startActivity(intent)
                        }catch (e : Exception){
                            val playStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.facebook.orca"))
                            context.startActivity(playStoreIntent)
                        }
                    }else if (item.title.contains("যোগাযোগ")){
                        val phoneNumber = "+8801832430112"
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                            context.startActivity(intent)
                    }

                }
            }
        }
    }

}

sealed class GridItemData {
    data class UrlItem(val title: String, val url: String, val fileName: String) : GridItemData()
    data class DrawableItem(val title: String, @DrawableRes val drawableRes: Int) : GridItemData()
}

@Composable
fun GridItem(title: String, url: String?, drawableRes: Int?, fileName: String, onClick: (String) -> Unit) {
    val context = LocalContext.current
    val bitmapState = remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(fileName) {
        if (url != null) {
            getFirstPageAsBitmapFromUrl(url, fileName, context) { bitmap ->
                bitmapState.value = bitmap
            }
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

            when {
                bitmapState.value != null -> {
                    Image(
                        bitmap = bitmapState.value!!.asImageBitmap(),
                        contentDescription = title,
                        modifier = Modifier.size(80.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                drawableRes != null -> {
                    Image(
                        painter = painterResource(id = drawableRes),
                        contentDescription = title,
                        modifier = Modifier.size(80.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                else -> CircularProgressIndicator()
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