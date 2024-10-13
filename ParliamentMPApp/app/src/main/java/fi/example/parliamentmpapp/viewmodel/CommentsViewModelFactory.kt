package fi.example.parliamentmpapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fi.example.parliamentmpapp.data.CommentRepository

/**
 * Factory class for creating instances of CommentsViewModel.
 * * @date Anish - 2112913 - 12/10/2024
 */
class CommentsViewModelFactory(private val commentRepository: CommentRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommentsViewModel::class.java)) {
            return CommentsViewModel(commentRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
