package com.utad.tfg.remote

import com.utad.tfg.local.daos.SpellDao
import com.utad.tfg.local.entities.SpellEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpellRepository @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val spellDao: SpellDao
) {

    suspend fun observeSpells() {
        try {
            firestoreRepository.getSpellsFlow().collect { spells ->
                spellDao.insertSpells(spells)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun fetchSpellDetails(index: String): SpellEntity? {
        val existing = spellDao.getSpellByIndex(index)
        if (existing?.isSynced == true) return existing

        // Fetch from Firestore and update local DB
        return try {
            val spell = firestoreRepository.fetchSpellByKey(index)
            if (spell != null) {
                spellDao.insertSpell(spell)
            }
            spell
        } catch (e: Exception) {
            e.printStackTrace()
            existing // Return local version if Firestore fails
        }
    }

    fun getFilteredSpells(level: Int, className: String): Flow<List<SpellEntity>> {
        return spellDao.getSpellsByLevelAndClass(level, className)
    }

    fun getFilteredSpells(levels: List<Int>, className: String): Flow<List<SpellEntity>> {
        return spellDao.getSpellsByLevelsAndClass(levels, className)
    }
}
