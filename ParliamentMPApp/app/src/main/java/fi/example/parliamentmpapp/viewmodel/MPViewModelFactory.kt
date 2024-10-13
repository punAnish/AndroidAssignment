package fi.example.parliamentmpapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fi.example.parliamentmpapp.data.CommentRepository
import fi.example.parliamentmpapp.data.MPRepository
import fi.example.parliamentmpapp.data.MPService

/**
 * Factory class for creating instances of MPViewModel.
 *
 * This factory is responsible for providing the necessary dependencies to the ViewModel.
 *
 * @date Anish - 2112913 - 12/10/2024
 */
class MPViewModelFactory(
    private val mpRepository: MPRepository,
    private val commentRepository: CommentRepository,
    private val mpService: MPService
) : ViewModelProvider.Factory {
    // Creates a ViewModel of the specified class type
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MPViewModel::class.java)) {
            return MPViewModel(mpRepository, commentRepository, mpService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
