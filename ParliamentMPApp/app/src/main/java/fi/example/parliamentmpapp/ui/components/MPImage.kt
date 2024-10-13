package fi.example.parliamentmpapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import fi.example.parliamentmpapp.R
import fi.example.parliamentmpapp.data.MP

/**
 * Composable function to display the image of a parliament member.
 *
 * Uses Coil to load images from a URL and displays them in a specified size.
 *
 * @date Anish - 2112913 - 12/10/2024
 */
@OptIn(ExperimentalCoilApi::class)
@Composable
fun MPImage(mp: MP, size: Dp = 128.dp) {
    // Construct the image URL using the MP's pictureUrl
    val imageUrl = "https://avoindata.eduskunta.fi/${mp.pictureUrl}"

    // Load the image with Coil and show a placeholder or error image if it fails to load
    Image(
        painter = rememberImagePainter(
            data = imageUrl,
            builder = {
                crossfade(true)
                placeholder(R.drawable.placeholder)
                error(R.drawable.error)
            }
        ),
        contentDescription = "${mp.firstname} ${mp.lastname}",
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
    )
}


