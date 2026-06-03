package com.utad.tfg.di

import android.content.Context
import androidx.room.Room
import com.utad.tfg.local.TfgDatabase
import com.utad.tfg.local.daos.CampaignDao
import com.utad.tfg.local.daos.CharacterDao
import com.utad.tfg.local.daos.EnemyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TfgDatabase {
        return Room.databaseBuilder(
            context,
            TfgDatabase::class.java,
            "tfg_database"
        ).build()
    }

    @Provides
    fun provideCampaignDao(db: TfgDatabase): CampaignDao = db.campaignDao()

    @Provides
    fun provideCharacterDao(db: TfgDatabase): CharacterDao = db.characterDao()

    @Provides
    fun provideEnemyDao(db: TfgDatabase): EnemyDao = db.enemyDao()
}
