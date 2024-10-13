package fi.example.parliamentmpapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.example.parliamentmpapp.data.MP
import fi.example.parliamentmpapp.ui.components.MPImage
import fi.example.parliamentmpapp.viewmodel.CommentsViewModel
/**
 * Composable function representing the detailed view of a Member of Parliament (MP).
 * This screen displays the MP's information, existing comments, and a field to add new comments.
 * @date Anish - 2112913 - 12/10/2024
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MPDetailScreen(
    mp: MP,
    onBack: () -> Unit,
    commentsViewModel: CommentsViewModel,
    modifier: Modifier = Modifier
) {
    // Collect comments for the specific MP from the ViewModel
    val comments by commentsViewModel.getCommentsForMP(mp.hetekaId).collectAsState(initial = emptyList())
    var commentText by remember { mutableStateOf("") }

    Column(modifier = modifier.padding(16.dp)) {

        // Top App Bar with Back Button
        TopAppBar(
            title = { Text("MP Details") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
        )
        // Display MP information
        Text(text = "${mp.firstname} ${mp.lastname}", style = MaterialTheme.typography.headlineLarge)
        MPImage(mp = mp)

        Spacer(modifier = Modifier.height(16.dp))

        // Display existing comments
        Text(text = "Comments:", style = MaterialTheme.typography.titleMedium)
        if (comments.isEmpty()) {
            Text(text = "No comments available.", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn {
                items(comments) { comment ->
                    Text(text = comment.text, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TextField to add a new comment
        OutlinedTextField(
            value = commentText,
            onValueChange = { commentText = it },
            label = { Text("Add a comment") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Button to submit the comment
        Button(
            onClick = {
                if (commentText.isNotEmpty()) {
                    commentsViewModel.insertComment(mp.hetekaId, commentText)
                    commentText = ""
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Submit")
        }
    }
}