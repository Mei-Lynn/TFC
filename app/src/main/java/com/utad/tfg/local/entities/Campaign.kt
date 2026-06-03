package com.utad.tfg.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "campaigns")
data class Campaign(
    @PrimaryKey
    val id: String, // Manually set for sharing via Google Nearby
    val name: String,
    val isDm: Boolean
)
