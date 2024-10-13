package fi.example.parliamentmpapp.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing Comment data operations.
 * It acts as a mediator between the CommentDao and the rest of the application.
 *
 * @date Anish - 2112913 - 12/10/2024
 */
class CommentRepository (private val commentDao: CommentDao) {
    // Insert a comment into the database
    suspend fun insert(comment: Comment) {
        try {
            commentDao.insert(comment)
        } catch (e: Exception) {
            // Handle the error, log it, or rethrow it
            e.printStackTrace()
        }
    }

    // Retrieve comments for a specific MP as a Flow
    fun getCommentsForMP(mpHetekaId: Int): Flow<List<Comment>> {
        return commentDao.getCommentsForMP(mpHetekaId)
    }

    // Insert a comment for a specific MP
    suspend fun insertComment(comment: Comment) {
        commentDao.insert(comment)
    }

}