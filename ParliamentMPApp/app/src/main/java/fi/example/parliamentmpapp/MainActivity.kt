package fi.example.parliamentmpapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import fi.example.parliamentmpapp.data.AppDatabase
import fi.example.parliamentmpapp.data.CommentRepository
import fi.example.parliamentmpapp.data.MPRepository
import fi.example.parliamentmpapp.data.MPServiceImpl
import fi.example.parliamentmpapp.ui.screens.MainScreen
import fi.example.parliamentmpapp.ui.theme.ParliamentMPAppTheme
import fi.example.parliamentmpapp.viewmodel.CommentsViewModel
import fi.example.parliamentmpapp.viewmodel.CommentsViewModelFactory
import fi.example.parliamentmpapp.viewmodel.MPViewModel
import fi.example.parliamentmpapp.viewmodel.MPViewModelFactory

/**
 * MainActivity serves as the entry point of the Parliament MP application.
 * It sets up the main content view using Jetpack Compose.
 *
 * @date Anish - 2112913 - 12/10/2024
 */

class MainActivity : ComponentActivity() {
    private val mpViewModel: MPViewModel by viewModels {
        val db = AppDatabase.getDatabase(applicationContext)
        val mpService = MPServiceImpl.service

        // Initialize the MPRepository with the correct parameters
        val mpRepository = MPRepository(db.mpDao(), db.commentDao(), mpService)

        // Initialize the CommentRepository with the correct CommentDao
        val commentRepository = CommentRepository(db.commentDao())

        // Return the ViewModelFactory with the correct parameters
        MPViewModelFactory(mpRepository, commentRepository, mpService)
    }

    private val commentsViewModel: CommentsViewModel by viewModels {
        val db = AppDatabase.getDatabase(applicationContext)
        val commentRepository = CommentRepository(db.commentDao()) // Make sure you have a CommentDao
        CommentsViewModelFactory(commentRepository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParliamentMPAppTheme {
                MainScreen(mpViewModel, commentsViewModel)
            }
        }
    }
}



