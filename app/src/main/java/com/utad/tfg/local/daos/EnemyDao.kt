package com.utad.tfg.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.utad.tfg.local.entities.Enemy
import kotlinx.coroutines.flow.Flow

@Dao
interface EnemyDao {
    @Query("SELECT * FROM enemies")
    fun getDownloadedEnemies(): Flow<List<Enemy>>

    @Query("SELECT * FROM enemies WHERE `index` = :index LIMIT 1")
    suspend fun getEnemyByIndex(index: String): Enemy?

    @Query("DELETE FROM enemies WHERE `index` = :index")
    suspend fun deleteEnemyByIndex(index: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnemy(enemy: Enemy)

    @Update
    suspend fun updateEnemy(enemy: Enemy)

    @Delete
    suspend fun deleteEnemy(enemy: Enemy)
}
