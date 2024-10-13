package fi.example.parliamentmpapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.example.parliamentmpapp.data.MP
import fi.example.parliamentmpapp.viewmodel.MPViewModel
import fi.example.parliamentmpapp.viewmodel.CommentsViewModel

/**
 * MPListScreen displays a list of Members of Parliament (MPs) using Jetpack Compose.
 * It observes the list of MPs from the MPViewModel and renders each item in a lazy column.
 *
 * @date Anish - 2112913 - 12/10/2024
 */
@Composable
fun MainScreen(mpViewModel: MPViewModel, commentsViewModel: CommentsViewModel) {
    var selectedMP by remember { mutableStateOf<MP?>(null) }

    Scaffold { innerPadding ->
        val modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        if (selectedMP == null) {
            MPListScreen(
                mpViewModel = mpViewModel,
                onMPSelected = { mp ->
                    selectedMP = mp
                }
            )
        } else {
            CommentScreen(
                selectedMP = selectedMP!!,
                onBack = { selectedMP = null },
                modifier = modifier,
                commentsViewModel = commentsViewModel
            )
        }
    }
}

@Composable
fun CommentScreen(
    selectedMP: MP,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    commentsViewModel: CommentsViewModel
) {
    // State to manage loading state
    var isLoading by remember { mutableStateOf(true) }

    // Collect comments for the selected MP as a state, and remember the state to avoid re-triggering recomposition
    val commentsFlow = remember { commentsViewModel.getCommentsForMP(selectedMP.hetekaId) }
    val comments by commentsFlow.collectAsState(initial = emptyList())

    LaunchedEffect(comments) {
        // When comments are loaded (even if empty), stop loading
        isLoading = false
    }

    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "Comments for ${selectedMP.firstname} ${selectedMP.lastname}",
            style = MaterialTheme.typography.bodyLarge
        )

        // Display a loading indicator only if the app is still loading comments
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            if (comments.isEmpty()) {
                Text("No comments available.")
            } else {
                LazyColumn {
                    items(comments) { comment ->
                        Text(text = comment.text)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Back button to return to the MP list
        Button(onClick = { onBack() }) {
            Text("Back to MP List")
        }
    }
}



