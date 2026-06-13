package com.utad.tfg.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.utad.tfg.model.CharState

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
    val raceIndex: String,
    val subraceIndex: String? = null,
    val classIndex: String,
    val subclassIndex: String? = null,
    val level: Int,
    val maxHp: Int,
    val currentHp: Int,
    val armorClass: Int,
    val initiative: Int = 0,
    val strength: Int = 10,
    val dexterity: Int = 10,
    val constitution: Int = 10,
    val intelligence: Int = 10,
    val wisdom: Int = 10,
    val charisma: Int = 10,
    val imgUri: String? = null,
    val state: CharState = CharState.noCampaign,
    val cantrips: List<String> = emptyList(),
    val preparedSpells: List<String> = emptyList(),
    val mainHandIndex: String? = null,
    val offHandIndex: String? = null,
    val armorIndex: String? = null
)
