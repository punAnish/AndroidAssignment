
package fi.example.parliamentmpapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fi.example.parliamentmpapp.data.MP
import fi.example.parliamentmpapp.data.MPRepository
import fi.example.parliamentmpapp.data.CommentRepository
import fi.example.parliamentmpapp.data.MPService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing UI-related data for Members of Parliament (MPs).
 *
 * This ViewModel interacts with repositories to fetch and insert MP data.
 *
 * @date Anish - 2112913 - 12/10/2024
 */
class MPViewModel(
    private val mpRepository: MPRepository,
    private val commentRepository: CommentRepository,
    private val mpService: MPService
) : ViewModel() {

    // Flow that provides a list of all MPs from the repository
    val allMPs: Flow<List<MP>> = mpRepository.getAllMPs()

    // State to track loading status
    private val _isLoading = MutableStateFlow(true)
    val isLoading: Flow<Boolean> get() = _isLoading

    init {
        // Load the MPs when ViewModel is initialized
        loadMPs()
    }

    private fun loadMPs() {
        viewModelScope.launch {
            _isLoading.value = true
            mpRepository.loadMPs()
            _isLoading.value = false
        }
    }


    // Inserts a new MP into the repository
    fun insert(mp: MP) {
        viewModelScope.launch {
            mpRepository.insert(mp)
        }
    }
}


