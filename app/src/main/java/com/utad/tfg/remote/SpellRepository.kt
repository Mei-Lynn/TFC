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

    fun getFilteredSpells(level: Int, className: String): Flow<List<SpellEntity>> {
        return spellDao.getSpellsByLevelAndClass(level, className)
    }

    fun getFilteredSpells(levels: List<Int>, className: String): Flow<List<SpellEntity>> {
        return spellDao.getSpellsByLevelsAndClass(levels, className)
    }
}
