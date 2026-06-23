package com.utad.tfg.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utad.tfg.remote.AdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val adminRepository: AdminRepository
) : ViewModel() {

    val collections = listOf("weapons", "armor", "spells", "creatures")

    private val _selectedCollection = MutableStateFlow<String?>(null)
    val selectedCollection: StateFlow<String?> = _selectedCollection.asStateFlow()

    private val _documents = MutableStateFlow<List<Map<String, Any>>>(emptyList())
    val documents: StateFlow<List<Map<String, Any>>> = _documents.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun selectCollection(collection: String) {
        _selectedCollection.value = collection
        loadDocuments()
    }

    fun loadDocuments() {
        val collection = _selectedCollection.value ?: return
        viewModelScope.launch {
            _isLoading.value = true
            _documents.value = adminRepository.getDocuments(collection)
            _isLoading.value = false
        }
    }

    fun saveDocument(docId: String?, data: Map<String, Any>) {
        val collection = _selectedCollection.value ?: return
        viewModelScope.launch {
            _isLoading.value = true
            if (docId == null) {
                adminRepository.addDocument(collection, data)
            } else {
                adminRepository.updateDocument(collection, docId, data)
            }
            loadDocuments()
        }
    }

    fun deleteDocument(docId: String) {
        val collection = _selectedCollection.value ?: return
        viewModelScope.launch {
            _isLoading.value = true
            adminRepository.deleteDocument(collection, docId)
            loadDocuments()
        }
    }
}
