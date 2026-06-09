package com.utad.tfg.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utad.tfg.local.daos.EnemyDao
import com.utad.tfg.local.entities.Enemy
import com.utad.tfg.remote.DndApiService
import com.utad.tfg.remote.DndMonsterResponse
import com.utad.tfg.remote.DndResource
import com.utad.tfg.remote.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dndApiService: DndApiService,
    private val networkUtils: NetworkUtils,
    private val enemyDao: EnemyDao
) : ViewModel() {

    private val _races = MutableStateFlow<List<DndResource>>(emptyList())
    val races = _races.asStateFlow()

    private val _classes = MutableStateFlow<List<DndResource>>(emptyList())
    val classes = _classes.asStateFlow()

    private val _monsters = MutableStateFlow<List<DndResource>>(emptyList())
    val monsters = _monsters.asStateFlow()

    // Local enemies for the "Downloads" or "Local Bestiary" view
    val localEnemies: StateFlow<List<Enemy>> = enemyDao.getDownloadedEnemies()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Indica si hay conexión de red disponible. */
    val isNetworkAvailable: StateFlow<Boolean> = networkUtils.observeNetworkStatus()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = networkUtils.isNetworkAvailable()
        )

    init {
        fetchRaces()
        fetchClasses()
        fetchMonsters()
    }


    //======================= Database =======================
    fun downloadMonster(monsterIndex: String) {
        viewModelScope.launch {
            try {
                val details = dndApiService.getMonsterDetails(monsterIndex)
                val enemy = Enemy(
                    name = details.name,
                    type = details.type,
                    size = details.size,
                    alignment = details.alignment,
                    maxHp = details.hitPoints,
                    currentHp = details.hitPoints,
                    armorClass = details.armorClass.firstOrNull()?.value ?: 10,
                    speed = details.speed.walk ?: "0 ft.",
                    strength = details.strength,
                    dexterity = details.dexterity,
                    constitution = details.constitution,
                    intelligence = details.intelligence,
                    wisdom = details.wisdom,
                    charisma = details.charisma,
                    challengeRating = details.challengeRating,
                    xp = details.xp,
                    imgUri = details.image?.let { "https://www.dnd5eapi.co$it" }
                )
                enemyDao.insertEnemy(enemy)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteLocalEnemy(enemy: Enemy) {
        viewModelScope.launch {
            enemyDao.deleteEnemy(enemy)
        }
    }


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

    fun fetchMonsters() {
        viewModelScope.launch {
            try {
                val response = dndApiService.getMonsters()
                _monsters.value = response.results
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val _monsterDetails = MutableStateFlow<DndMonsterResponse?>(null)
    val monsterDetails = _monsterDetails.asStateFlow()

    fun fetchMonsterDetails(index: String) {
        viewModelScope.launch {
            try {
                _monsterDetails.value = dndApiService.getMonsterDetails(index)
            } catch (e: Exception) {
                e.printStackTrace()
                _monsterDetails.value = null
            }
        }
    }
}
