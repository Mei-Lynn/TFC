package com.utad.tfg.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utad.tfg.local.daos.EnemyDao
import com.utad.tfg.local.entities.Enemy
import com.utad.tfg.model.Ability
import com.utad.tfg.model.DndRace
import com.utad.tfg.model.RaceRegistry
import com.utad.tfg.model.classes.ClassRegistry
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Class as DndClass
import com.utad.tfg.remote.DndApiService
import com.utad.tfg.remote.DndMonsterResponse
import com.utad.tfg.remote.JsonResource
import com.utad.tfg.remote.NetworkUtils
import com.utad.tfg.remote.toEnemy
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
    private val enemyDao: EnemyDao,
    private val characterDao: com.utad.tfg.local.daos.CharacterDao
) : ViewModel() {

    private val _races = MutableStateFlow<List<DndRace>>(emptyList())
    val races = _races.asStateFlow()

    private val _classes = MutableStateFlow<List<DndClass>>(emptyList())
    val classes = _classes.asStateFlow()

    private val _backgrounds = MutableStateFlow<List<JsonResource>>(emptyList())
    val backgrounds = _backgrounds.asStateFlow()

    private val _subraces = MutableStateFlow<List<DndRace>>(emptyList())
    val subraces = _subraces.asStateFlow()

    private val _subclasses = MutableStateFlow<List<Subclass>>(emptyList())
    val subclasses = _subclasses.asStateFlow()

    private val _spells = MutableStateFlow<List<JsonResource>>(emptyList())
    val spells = _spells.asStateFlow()

    private val _monsters = MutableStateFlow<List<JsonResource>>(emptyList())
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
        _races.value = RaceRegistry.races
        _classes.value = ClassRegistry.classes
        fetchBackgrounds()
        fetchMonsters()
    }


    //======================= Database =======================
    fun downloadMonster(index: String) {
        viewModelScope.launch {
            try {
                val details = dndApiService.getMonsterDetails(index)
                val enemy = details.toEnemy()
                enemyDao.insertEnemy(enemy)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteSavedMonster(index: String) {
        viewModelScope.launch {
            enemyDao.deleteEnemyByIndex(index)
        }
    }

    fun saveCharacter(
        name: String,
        race: DndRace,
        subrace: DndRace?,
        background: JsonResource?,
        dndClass: DndClass,
        subclass: Subclass?,
        abilityScores: Map<Ability, Int>
    ) {
        viewModelScope.launch {
            val dex = abilityScores[Ability.Dexterity] ?: 10
            val newChar = com.utad.tfg.local.entities.Character(
                name = name,
                raceIndex = race.raceName.lowercase(), // Or use a proper mapping
                subraceIndex = subrace?.subraceName?.lowercase(),
                classIndex = dndClass.classIndex,
                subclassIndex = subclass?.subclassName?.lowercase()?.replace(" ", "-"),
                backgroundIndex = background?.index,
                level = 1,
                maxHp = dndClass.hitDie + Ability.calculateModifier(abilityScores[Ability.Constitution] ?: 10),
                currentHp = dndClass.hitDie + Ability.calculateModifier(abilityScores[Ability.Constitution] ?: 10),
                armorClass = 10 + Ability.calculateModifier(dex),
                strength = abilityScores[Ability.Strength] ?: 10,
                dexterity = dex,
                constitution = abilityScores[Ability.Constitution] ?: 10,
                intelligence = abilityScores[Ability.Intelligence] ?: 10,
                wisdom = abilityScores[Ability.Wisdom] ?: 10,
                charisma = abilityScores[Ability.Charisma] ?: 10
            )
            characterDao.insertCharacter(newChar)
        }
    }

    //========================= Local Data ==========================
    fun fetchSubraces(raceName: String) {
        _subraces.value = RaceRegistry.getSubraces(raceName)
    }

    fun fetchSubclasses(classIndex: String) {
        _subclasses.value = ClassRegistry.getSubclasses(classIndex)
    }

    //========================= API ==========================
    fun fetchBackgrounds() {
        viewModelScope.launch {
            try {
                val response = dndApiService.getBackgrounds()
                _backgrounds.value = response.results
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchSpells(classIndex: String) {
        viewModelScope.launch {
            try {
                val response = dndApiService.getSpells()
                _spells.value = response.results
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
            val local = enemyDao.getEnemyByIndex(index)
            if (local != null) {
                _monsterDetails.value = DndMonsterResponse(
                    index = local.index,
                    name = local.name,
                    size = local.size,
                    type = local.type,
                    alignment = local.alignment,
                    hitPoints = local.maxHp,
                    armorClass = listOf(com.utad.tfg.remote.ArmorClass("base", local.armorClass)),
                    strength = local.strength,
                    dexterity = local.dexterity,
                    constitution = local.constitution,
                    intelligence = local.intelligence,
                    wisdom = local.wisdom,
                    charisma = local.charisma,
                    speed = com.utad.tfg.remote.MonsterSpeed(walk = local.speed),
                    challengeRating = local.challengeRating,
                    xp = local.xp,
                    actions = local.actions,
                    image = local.imgUri
                )
            } else if (isNetworkAvailable.value) {
                try {
                    _monsterDetails.value = dndApiService.getMonsterDetails(index)
                } catch (e: Exception) {
                    e.printStackTrace()
                    _monsterDetails.value = null
                }
            }
        }
    }
}
