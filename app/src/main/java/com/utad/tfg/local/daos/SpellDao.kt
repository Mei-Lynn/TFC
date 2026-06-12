package com.utad.tfg.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.utad.tfg.local.entities.SpellEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SpellDao {
    @Query("SELECT * FROM spells WHERE level = :level AND classes LIKE '%' || :className || '%'")
    fun getSpellsByLevelAndClass(level: Int, className: String): Flow<List<SpellEntity>>

    @Query("SELECT * FROM spells WHERE level IN (:levels) AND classes LIKE '%' || :className || '%'")
    fun getSpellsByLevelsAndClass(levels: List<Int>, className: String): Flow<List<SpellEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpells(spells: List<SpellEntity>)

    @Query("SELECT COUNT(*) FROM spells")
    suspend fun getSpellCount(): Int

    @Query("SELECT * FROM spells")
    fun getAllSpells(): Flow<List<SpellEntity>>
}
