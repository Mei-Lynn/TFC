package com.utad.tfg.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.utad.tfg.local.daos.CampaignDao
import com.utad.tfg.local.daos.CharacterDao
import com.utad.tfg.local.daos.EnemyDao
import com.utad.tfg.model.Campaign
import com.utad.tfg.model.Character
import com.utad.tfg.model.Enemy

@Database(
    entities = [Campaign::class, Character::class, Enemy::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TfgDatabase : RoomDatabase() {
    abstract fun campaignDao(): CampaignDao
    abstract fun characterDao(): CharacterDao
    abstract fun enemyDao(): EnemyDao
}
