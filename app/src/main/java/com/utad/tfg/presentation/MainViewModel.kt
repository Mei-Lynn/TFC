package com.utad.tfg.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utad.tfg.remote.DndApiService
import com.utad.tfg.remote.DndResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dndApiService: DndApiService
) : ViewModel() {

    private val _races = MutableStateFlow<List<DndResource>>(emptyList())
    val races = _races.asStateFlow()

    private val _classes = MutableStateFlow<List<DndResource>>(emptyList())
    val classes = _classes.asStateFlow()

    private val _monsters = MutableStateFlow<List<DndResource>>(emptyList())
    val monsters = _monsters.asStateFlow()

    private val _characters = MutableStateFlow<List<Character>>(emptyList())
    val characters = _characters.asStateFlow()

    init {
        fetchRaces()
        fetchClasses()
        fetchMonsters()
    }


    //======================= Database =======================




    //========================= API ==========================
    private fun fetchRaces() {
        viewModelScope.launch {
            try {
                val response = dndApiService.getRaces()
                _races.value = response.results
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun fetchClasses() {
        viewModelScope.launch {
            try {
                val response = dndApiService.getClasses()
                _classes.value = response.results
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun fetchMonsters() {
        viewModelScope.launch {
            try {
                val response = dndApiService.getMonsters()
                _monsters.value = response.results
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
