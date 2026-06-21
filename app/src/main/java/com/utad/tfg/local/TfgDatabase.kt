package com.utad.tfg.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.utad.tfg.local.daos.CampaignDao
import com.utad.tfg.local.daos.CharacterDao
import com.utad.tfg.local.daos.EnemyDao
import com.utad.tfg.local.daos.SpellDao
import com.utad.tfg.local.entities.Campaign
import com.utad.tfg.local.entities.Character
import com.utad.tfg.local.entities.Enemy
import com.utad.tfg.local.entities.SpellEntity

@Database(
    entities = [Campaign::class, Character::class, Enemy::class, SpellEntity::class],
    version = 13,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TfgDatabase : RoomDatabase() {
    abstract fun campaignDao(): CampaignDao
    abstract fun characterDao(): CharacterDao
    abstract fun enemyDao(): EnemyDao
    abstract fun spellDao(): SpellDao
}
