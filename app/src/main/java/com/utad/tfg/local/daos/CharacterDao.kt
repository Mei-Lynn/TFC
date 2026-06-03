package com.utad.tfg.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.utad.tfg.model.Character
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Query("SELECT * FROM characters WHERE campaignId = :campaignId")
    fun getCharactersByCampaign(campaignId: String): Flow<List<Character>>

    @Query("SELECT * FROM characters WHERE campaignId IS NULL")
    fun getUnassignedCharacters(): Flow<List<Character>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: Character)

    @Update
    suspend fun updateCharacter(character: Character)

    @Delete
    suspend fun deleteCharacter(character: Character)
}
