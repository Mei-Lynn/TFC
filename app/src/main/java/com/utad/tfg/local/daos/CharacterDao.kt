package com.utad.tfg.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.utad.tfg.local.entities.Character
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Query("SELECT * FROM characters WHERE campaignId = :campaignId")
    fun getCharactersByCampaign(campaignId: String): Flow<List<Character>>

    @Query("SELECT * FROM characters WHERE campaignId IS NULL")
    fun getUnassignedCharacters(): Flow<List<Character>>

    @Query("SELECT * FROM characters")
    fun getAllCharacters(): Flow<List<Character>>

    @Query("SELECT * FROM characters WHERE id = :characterId")
    suspend fun getCharacterById(characterId: Long) : Character?

    @Query("DELETE FROM characters WHERE id IN (:ids)")
    suspend fun deleteCharactersByID(ids: List<Long>)

    @Query("SELECT remoteId FROM characters WHERE id IN (:ids)")
    suspend fun getRemoteIdByID(ids : List<Long>): List<String?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: Character): Long

    @Update
    suspend fun updateCharacter(character: Character)

    @Delete
    suspend fun deleteCharacter(character: Character)

    @Query("DELETE FROM characters")
    suspend fun deleteAllCharacters()
}
