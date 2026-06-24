package com.utad.tfg.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "campaigns")
data class Campaign(
    @PrimaryKey
    val id: String,
    val name: String,
    val ownerId: String,
    val playersIds: List<String>,
    val charactersIds: List<String>,
    val monstersIds: List<String>,
    val imgUri: String? = null
)
