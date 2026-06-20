package com.utad.tfg.presentation

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utad.tfg.local.daos.CharacterDao
import com.utad.tfg.local.daos.EnemyDao
import com.utad.tfg.local.entities.Enemy
import com.utad.tfg.local.entities.Character
import com.utad.tfg.local.entities.SpellEntity
import com.utad.tfg.model.Ability
import com.utad.tfg.model.DndRace
import com.utad.tfg.model.RaceRegistry
import com.utad.tfg.model.classes.ClassRegistry
import com.utad.tfg.model.classes.ClassResource
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.remote.ArmorClass
import com.utad.tfg.model.classes.Class as DndClass
import com.utad.tfg.remote.DndMonsterResponse
import com.utad.tfg.remote.FirestoreRepository
import com.utad.tfg.remote.JsonResource
import com.utad.tfg.remote.MonsterSpeed
import com.utad.tfg.remote.SpellRepository
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
    private val firestoreRepository: FirestoreRepository,
    private val enemyDao: EnemyDao,
    private val characterDao: CharacterDao,
    private val spellRepository: SpellRepository
) : ViewModel() {
    private val _races = MutableStateFlow<List<DndRace>>(emptyList())
    val races = _races.asStateFlow()

    private val _classes = MutableStateFlow<List<DndClass>>(emptyList())
    val classes = _classes.asStateFlow()

    private val _subraces = MutableStateFlow<List<DndRace>>(emptyList())
    val subraces = _subraces.asStateFlow()

    private val _subclasses = MutableStateFlow<List<Subclass>>(emptyList())
    val subclasses = _subclasses.asStateFlow()

    private val _monsters = MutableStateFlow<List<JsonResource>>(emptyList())
    val monsters = _monsters.asStateFlow()

    // Bestiario descargado localmente
    val localEnemies: StateFlow<List<Enemy>> = enemyDao.getDownloadedEnemies()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val localCharacters: StateFlow<List<Character>> = characterDao.getAllCharacters()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        _races.value = RaceRegistry.races
        _classes.value = ClassRegistry.classes
        fetchMonsters()
        viewModelScope.launch {
            spellRepository.syncSpellsIfNeeded()
        }
    }

    private val _filteredSpells = MutableStateFlow<List<SpellEntity>>(emptyList())
    val filteredSpells = _filteredSpells.asStateFlow()

    fun filterSpells(levels: IntRange, className: String) {
        viewModelScope.launch {
            spellRepository.getFilteredSpells(levels.toList(), className).collect {
                _filteredSpells.value = it
            }
        }
    }

    fun filterSpells(level: Int, className: String) {
        viewModelScope.launch {
            spellRepository.getFilteredSpells(listOf(level), className).collect {
                _filteredSpells.value = it
            }
        }
    }


    //======================= Database =======================
    fun downloadMonster(index: String) {
        viewModelScope.launch {
            try {
                val details = firestoreRepository.fetchCreatureByKey(index)
                if (details != null) {
                    val enemy = details.toEnemy()
                    enemyDao.insertEnemy(enemy)
                }
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
        dndClass: DndClass,
        subclass: Subclass?,
        abilityScores: Map<Ability, Int>,
        cantrips: List<SpellEntity> = emptyList(),
        spells: List<SpellEntity> = emptyList(),
        mainHandIndex: String? = null,
        offHandIndex: String? = null,
        armorIndex: String? = null
    ) {
        viewModelScope.launch {
            // Lazy sync spell details before saving character
            val allSpellsToSync = cantrips + spells
            allSpellsToSync.forEach { spell ->
                if (!spell.isSynced) {
                    spellRepository.fetchSpellDetails(spell.index)
                }
            }

            val dex = abilityScores[Ability.Dexterity] ?: 10
            val newChar = Character(
                name = name,
                raceIndex = race.raceName.lowercase(), // Or use a proper mapping
                subraceIndex = subrace?.subraceName?.lowercase(),
                classIndex = dndClass.classIndex,
                subclassIndex = subclass?.subclassName?.lowercase()?.replace(" ", "-"),
                level = 1,
                maxHp = dndClass.hitDie + Ability.calculateModifier(abilityScores[Ability.Constitution] ?: 10),
                currentHp = dndClass.hitDie + Ability.calculateModifier(abilityScores[Ability.Constitution] ?: 10),
                armorClass = 10 + Ability.calculateModifier(dex),
                strength = abilityScores[Ability.Strength] ?: 10,
                dexterity = dex,
                constitution = abilityScores[Ability.Constitution] ?: 10,
                intelligence = abilityScores[Ability.Intelligence] ?: 10,
                wisdom = abilityScores[Ability.Wisdom] ?: 10,
                charisma = abilityScores[Ability.Charisma] ?: 10,
                cantrips = cantrips.map { it.index },
                preparedSpells = spells.map { it.index },
                mainHandIndex = mainHandIndex,
                offHandIndex = offHandIndex,
                armorIndex = armorIndex
            )
            characterDao.insertCharacter(newChar)
        }
    }

    fun deleteCharactersByID(selectedIDs: SnapshotStateList<Long>, afterDeletion: () -> Unit) {
        viewModelScope.launch {
            characterDao.deleteCharactersByID(selectedIDs)
            afterDeletion()
        }
    }

    private val _selectedCharacter = MutableStateFlow<Character?>(null)
    val selectedCharacter = _selectedCharacter.asStateFlow()

    fun setSelectedCharacter(character: Character) {
        viewModelScope.launch {
            _selectedCharacter.value = character
        }
    }

    private val _fullSelectedCharacterSpells = MutableStateFlow<List<SpellEntity>>(emptyList())
    val fullSelectedCharacterSpells = _fullSelectedCharacterSpells.asStateFlow()

    fun loadSelectedCharacterSpells() {
        val char = _selectedCharacter.value ?: return
        viewModelScope.launch {
            val allIndices = char.cantrips + char.preparedSpells
            val entities = mutableListOf<SpellEntity>()
            allIndices.forEach { index ->
                val entity = spellRepository.fetchSpellDetails(index)
                if (entity != null) entities.add(entity)
            }
            _fullSelectedCharacterSpells.value = entities
        }
    }

    // Spell details — now uses SpellEntity directly
    private val _spellDetails = MutableStateFlow<SpellEntity?>(null)
    val spellDetails = _spellDetails.asStateFlow()

    fun fetchSpellDetails(index: String) {
        viewModelScope.launch {
            try {
                _spellDetails.value = spellRepository.fetchSpellDetails(index)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun clearSpellDetails() {
        viewModelScope.launch {
            _spellDetails.value = null
        }
    }

    fun updateCharacterSpells(character: Character, newCantrips: List<String>, newPreparedSpells: List<String>) {
        viewModelScope.launch {
            val updatedChar = character.copy(
                cantrips = newCantrips,
                preparedSpells = newPreparedSpells
            )
            characterDao.updateCharacter(updatedChar)
            _selectedCharacter.value = updatedChar
            loadSelectedCharacterSpells()
        }
    }

    //========================= Local Data ==========================
    fun getSubraces(raceName: String) {
        _subraces.value = RaceRegistry.getSubraces(raceName)
    }

    fun getSubclasses(classIndex: String) {
        _subclasses.value = ClassRegistry.getSubclasses(classIndex)
    }

    //========================= Firestore ==========================

    fun fetchMonsters() {
        viewModelScope.launch {
            try {
                val creatures = firestoreRepository.fetchCreatureList()
                _monsters.value = creatures
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val _monsterDetails = MutableStateFlow<DndMonsterResponse?>(null)
    val monsterDetails = _monsterDetails.asStateFlow()

    fun fetchMonsterDetails(index: String) {
        viewModelScope.launch {
            // Check local Room DB first
            val local = enemyDao.getEnemyByIndex(index)
            if (local != null) {
                _monsterDetails.value = DndMonsterResponse(
                    index = local.index,
                    name = local.name,
                    size = local.size,
                    type = local.type,
                    alignment = local.alignment,
                    hitPoints = local.maxHp,
                    armorClass = listOf(ArmorClass("base", local.armorClass)),
                    strength = local.strength,
                    dexterity = local.dexterity,
                    constitution = local.constitution,
                    intelligence = local.intelligence,
                    wisdom = local.wisdom,
                    charisma = local.charisma,
                    speed = MonsterSpeed(walk = local.speed),
                    challengeRating = local.challengeRating,
                    xp = local.xp,
                    actions = local.actions,
                    image = local.imgUri
                )
            } else {
                try {
                    _monsterDetails.value = firestoreRepository.fetchCreatureByKey(index)
                } catch (e: Exception) {
                    e.printStackTrace()
                    _monsterDetails.value = null
                }
            }
        }
    }

    fun emptyMonsterDetails() {
        viewModelScope.launch {
            _monsterDetails.value = null
        }
    }

    //========================= Level Up ==========================

    fun levelUp(
        character: Character,
        hpGain: Int,
        abilityIncreases: Map<Ability, Int> = emptyMap(),
        newSubclassIndex: String? = null,
        newCantrips: List<String>,
        newPreparedSpells: List<String>
    ) {
        viewModelScope.launch {
            val updatedChar = character.copy(
                level = character.level + 1,
                maxHp = character.maxHp + hpGain,
                currentHp = character.currentHp + hpGain,
                strength = character.strength + (abilityIncreases[Ability.Strength] ?: 0),
                dexterity = character.dexterity + (abilityIncreases[Ability.Dexterity] ?: 0),
                constitution = character.constitution + (abilityIncreases[Ability.Constitution] ?: 0),
                intelligence = character.intelligence + (abilityIncreases[Ability.Intelligence] ?: 0),
                wisdom = character.wisdom + (abilityIncreases[Ability.Wisdom] ?: 0),
                charisma = character.charisma + (abilityIncreases[Ability.Charisma] ?: 0),
                subclassIndex = newSubclassIndex ?: character.subclassIndex,
                cantrips = newCantrips,
                preparedSpells = newPreparedSpells
            )
            characterDao.updateCharacter(updatedChar)
            _selectedCharacter.value = updatedChar
            loadSelectedCharacterSpells()
        }
    }

    /**
     * Returns the highest spell level with slots > 0 for a given class at a given level.
     * Checks both regular spellSlots and LevelledSlots (e.g., Warlock Pact Magic).
     * Returns 0 if the class has no spell slots at that level.
     */
    fun getMaxSpellLevelForClass(classIndex: String, level: Int): Int {
        val dndClass = ClassRegistry.createClass(classIndex, level) ?: return 0

        // Check regular spell slots
        val slots = dndClass.spellSlots.getOrElse(level) { emptyList() }
        var maxFromSlots = 0
        for (i in slots.indices.reversed()) {
            if (slots[i] > 0) { maxFromSlots = i + 1; break }
        }

        // Check LevelledSlots in uniqueResources (e.g., Warlock Pact Magic)
        var maxFromPact = 0
        dndClass.uniqueResources.forEach { resource ->
            if (resource is ClassResource.LevelledSlots) {
                val pactLevel = resource.level.getOrElse(level) { 0 }
                if (pactLevel > maxFromPact) maxFromPact = pactLevel
            }
        }

        return maxOf(maxFromSlots, maxFromPact)
    }
}
