package com.utad.tfg.remote

import com.utad.tfg.local.daos.SpellDao
import com.utad.tfg.local.entities.SpellEntity
import kotlinx.coroutines.flow.Flow
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpellRepository @Inject constructor(
    private val dndApiService: DndApiService,
    private val spellDao: SpellDao
) {
    val languageTag : String = Locale.getDefault().toLanguageTag()

    suspend fun syncSpellsIfNeeded() {
        if (spellDao.getSpellCount() == 0) {
            syncSpells()
        }
    }

    private suspend fun syncSpells() {
        val spellMap = mutableMapOf<String, SpellEntity>()

        // 1. Fetch by level (0 to 9)
        for (level in 0..9) {
            val response = dndApiService.getSpells(level, lang = languageTag)
            response.results.forEach { res ->
                spellMap[res.index] = SpellEntity(
                    index = res.index,
                    name = res.name,
                    level = level,
                    classes = emptyList()
                )
            }
        }

        // 2. Fetch by class
        val classes = listOf("bard", "cleric", "druid", "paladin", "ranger", "sorcerer", "warlock", "wizard")
        for (className in classes) {
            val response = dndApiService.getClassSpells(className, lang = languageTag)
            response.results.forEach { res ->
                val existing = spellMap[res.index]
                if (existing != null) {
                    spellMap[res.index] = existing.copy(classes = existing.classes + className)
                }
            }
        }

        // 3. Save to DB
        spellDao.insertSpells(spellMap.values.toList())
    }

    suspend fun fetchSpellDetails(index: String): SpellEntity? {
        val existing = spellDao.getSpellByIndex(index)
        if (existing?.isSynced == true) return existing

        return try {
            val response = dndApiService.getSpellDetails(index, lang = languageTag)
            val updated = (existing ?: SpellEntity(
                index = response.index,
                name = response.name,
                level = response.level,
                classes = response.classes.map { it.index }
            )).copy(
                damageDice = response.damage?.damageAtSlotLevel?.get(response.level.toString()) 
                    ?: response.damage?.damageAtCharacterLevel?.get("1"), // Default for cantrips or simple damage
                damageType = response.damage?.damageType?.name,
                range = response.range,
                castingTime = response.castingTime,
                description = response.desc.joinToString("\n"),
                isConcentration = response.concentration,
                isSynced = true
            )
            spellDao.insertSpell(updated)
            updated
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getFilteredSpells(level: Int, className: String): Flow<List<SpellEntity>> {
        return spellDao.getSpellsByLevelAndClass(level, className)
    }

    fun getFilteredSpells(levels: List<Int>, className: String): Flow<List<SpellEntity>> {
        return spellDao.getSpellsByLevelsAndClass(levels, className)
    }
}
