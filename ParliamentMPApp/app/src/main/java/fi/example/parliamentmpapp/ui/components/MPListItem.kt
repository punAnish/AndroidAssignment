package fi.example.parliamentmpapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import fi.example.parliamentmpapp.data.MP

/**
 * Composable function to display a list item for a parliament member.
 *
 * Displays the member's name and image. The item is clickable to perform an action when tapped.
 * @date Anish - 2112913 - 12/10/2024
 */

@Composable
fun MPListItem(mp: MP, onItemClick: (MP) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onItemClick(mp) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Display the image of the MP using your existing MPImage composable
        MPImage(mp = mp, size = 64.dp)

        Spacer(modifier = Modifier.width(16.dp))

        // Display the MP's name and party
        Column {
            Text(
                text = "${mp.firstname} ${mp.lastname}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = mp.party,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

