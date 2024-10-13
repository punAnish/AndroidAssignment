package fi.example.parliamentmpapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import fi.example.parliamentmpapp.data.MP

/**
 * Composable function to display a list item for a parliament member.
 *
 * Displays the member's name and image. The item is clickable to perform an action when tapped.
 *
 * @author Anish
 * @date Anish - 2112913 - 12/10/2024
 */

@Composable
fun MPListItem(mp: MP, onItemClick: (MP) -> Unit) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .clickable { onItemClick(mp) }
    ) {
        Text(
            text = "${mp.firstname} ${mp.lastname}",
            style = MaterialTheme.typography.bodyLarge
        )
        // Display the image of the MP
        MPImage(mp = mp)
    }
}
