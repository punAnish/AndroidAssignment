package fi.example.parliamentmpapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import fi.example.parliamentmpapp.R
import fi.example.parliamentmpapp.data.MP

/**
 * Composable function to display the image of a parliament member.
 *
 * Uses Coil to load images from a URL and displays them in a specified size.
 *
 * @author Anish
 * @date Anish - 2112913 - 12/10/2024
 */
@Composable
fun MPImage(mp: MP, size: Dp = 128.dp) {
    // Construct the image URL using the MP's pictureUrl
    val imageUrl = "https://avoindata.eduskunta.fi/${mp.pictureUrl}"

    Image(
        painter = rememberImagePainter(data = imageUrl),
        contentDescription = "${mp.firstname} ${mp.lastname}",
        modifier = Modifier.size(size)
    )
}

