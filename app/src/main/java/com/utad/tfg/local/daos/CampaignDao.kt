package com.utad.tfg.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.utad.tfg.model.Campaign
import kotlinx.coroutines.flow.Flow

@Dao
interface CampaignDao {
    @Query("SELECT * FROM campaigns")
    fun getAllCampaigns(): Flow<List<Campaign>>

    @Query("SELECT * FROM campaigns WHERE id = :id")
    suspend fun getCampaignById(id: String): Campaign?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCampaign(campaign: Campaign)

    @Update
    suspend fun updateCampaign(campaign: Campaign)

    @Delete
    suspend fun deleteCampaign(campaign: Campaign)
}
