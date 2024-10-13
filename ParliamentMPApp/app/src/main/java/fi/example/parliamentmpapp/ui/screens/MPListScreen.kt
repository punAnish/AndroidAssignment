package fi.example.parliamentmpapp.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import fi.example.parliamentmpapp.data.MP
import fi.example.parliamentmpapp.viewmodel.MPViewModel
import fi.example.parliamentmpapp.ui.components.MPListItem

/**
 * Composable function that displays a list of parliament members.
 *
 * The list is populated with data from the MPViewModel and allows selection of an MP item.
 *
 * @author Anish
 * @date Anish - 2112913 - 12/10/2024
 */
@Composable
fun MPListScreen(mpViewModel: MPViewModel, onMPSelected: (MP) -> Unit) {
    // Collect the list of MPs as a state
    val mps by mpViewModel.allMPs.collectAsState(initial = emptyList())
    val isLoading by mpViewModel.isLoading.collectAsState(initial = true)

    /// Check if the list is empty and display appropriate UI
    if (isLoading) {
        CircularProgressIndicator()
    } else if (mps.isEmpty()) {
        Text(text = "No MPs available")
    } else {
        LazyColumn {
            items(mps) { mp ->
                MPListItem(mp = mp, onItemClick = { onMPSelected(mp) })
            }
        }
    }

}


