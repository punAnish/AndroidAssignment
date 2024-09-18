package fi.example.lab08

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import fi.example.lab08.ui.theme.Lab08Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab08Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ImageLoader(modifier = Modifier.padding(innerPadding))

                }
            }
        }
    }
}

@Composable
fun ImageLoader(modifier: Modifier = Modifier) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Launch a coroutine to download the image
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            bitmap = downloadImage("https://users.metropolia.fi/~jarkkov/folderimage.jpg")
        }
    }

    // If bitmap is not null, display the image, otherwise show a greeting text
    if (bitmap != null) {
        Image(
            bitmap = bitmap!!.asImageBitmap(),
            contentDescription = null,
            modifier = modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    } else {
        Text(
            text = "Loading image...",
            modifier = modifier
        )
    }
}

// Function to download the image using coroutines
suspend fun downloadImage(url: String): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val imageUrl = URL(url)
            BitmapFactory.decodeStream(imageUrl.openStream())
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Lab08Theme {
        ImageLoader()
    }
}