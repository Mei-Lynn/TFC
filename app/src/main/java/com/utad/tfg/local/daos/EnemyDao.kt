package com.utad.tfg.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.utad.tfg.local.entities.Enemy
import com.utad.tfg.remote.DndMonsterResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface EnemyDao {
    @Query("SELECT * FROM enemies")
    fun getDownloadedEnemies(): Flow<List<Enemy>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnemy(enemy: Enemy)

    @Update
    suspend fun updateEnemy(enemy: Enemy)

    @Delete
    suspend fun deleteEnemy(enemy: Enemy)
}
