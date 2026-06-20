package com.utad.tfg.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "spells")
data class SpellEntity(
    @PrimaryKey
    val index: String,
    val name: String,
    val level: Int,
    val classes: List<String>,
    val damageDice: String? = null,
    val damageType: String? = null,
    val range: String? = null,
    val castingTime: String? = null,
    val description: String? = null,
    val isConcentration: Boolean = false,
    val school: String? = null,
    val ritual: Boolean = false,
    val components: String? = null,
    val material: String? = null,
    val duration: String? = null,
    val higherLevel: String? = null,
    val isSynced: Boolean = false
)
