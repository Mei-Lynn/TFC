package com.utad.tfg.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "characters",
    foreignKeys = [
        ForeignKey(
            entity = Campaign::class,
            parentColumns = ["id"],
            childColumns = ["campaignId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["campaignId"])]
)
data class Character(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val campaignId: String? = null,
    val name: String,
    val race: String,
    val className: String,
    val subclass: String?,
    val level: Int,
    val maxHp: Int,
    val currentHp: Int,
    val armorClass: Int,
    val initiative: Int = 0,
    val imgUri: String? = null,
    val state: CharState = CharState.noCampaign
)
