package fi.example.parliamentmpapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fi.example.parliamentmpapp.data.CommentRepository
import fi.example.parliamentmpapp.data.Comment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing comments related to Members of Parliament (MPs).
 *  @date Anish - 2112913 - 12/10/2024
 */
class CommentsViewModel(private val commentRepository: CommentRepository) : ViewModel() {

    // Fetches comments for a specific MP by their ID.
    fun getCommentsForMP(mpId: Int): Flow<List<Comment>> {
        return commentRepository.getCommentsForMP(mpId)
    }

    // Inserts a new comment for a specific MP.
    fun insertComment(mpId: Int, commentText: String) {
        val comment = Comment(mpHetekaID = mpId, text = commentText)
        viewModelScope.launch {
            commentRepository.insertComment(comment)
        }
    }
}
