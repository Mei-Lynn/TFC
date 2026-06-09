package com.utad.tfg.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "enemies",
)
data class Enemy(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val index: String,
    val campaignId: String,
    val name: String,
    val type: String,
    val size: String,
    val alignment: String,
    val maxHp: Int,
    val currentHp: Int,
    val armorClass: Int,
    val speed: String,
    val strength: Int,
    val dexterity: Int,
    val constitution: Int,
    val intelligence: Int,
    val wisdom: Int,
    val charisma: Int,
    val challengeRating: Float,
    val xp: Int,
    val actions: List<com.utad.tfg.remote.MonsterAction>? = null,
    val initiative: Int = 0,
    val imgUri: String? = null
)
