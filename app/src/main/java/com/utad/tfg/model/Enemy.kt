package com.utad.tfg.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "enemies",
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
data class Enemy(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val campaignId: String,
    val name: String,
    val type: String,
    val maxHp: Int,
    val currentHp: Int,
    val armorClass: Int,
    val initiative: Int = 0,
    val imgUri: String? = null
)
