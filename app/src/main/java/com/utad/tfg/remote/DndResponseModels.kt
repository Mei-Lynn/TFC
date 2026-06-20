package com.utad.tfg.remote

import com.utad.tfg.local.entities.Enemy

/**
 * Lightweight reference for displaying item lists (creatures, spells, etc.)
 */
data class JsonResource(
    val index: String,
    val name: String,
    val url: String
)

/**
 * Represents a monster/creature for display in the detail dialog.
 */
data class DndMonsterResponse(
    val index: String,
    val name: String,
    val size: String,
    val type: String,
    val alignment: String,
    val hitPoints: Int,
    val armorClass: List<ArmorClass>,
    val strength: Int,
    val dexterity: Int,
    val constitution: Int,
    val intelligence: Int,
    val wisdom: Int,
    val charisma: Int,
    val speed: MonsterSpeed,
    val challengeRating: Float,
    val xp: Int,
    val actions: List<MonsterAction>? = null,
    val image: String? = null
)

fun DndMonsterResponse.toEnemy(): Enemy {
    return Enemy(
        index = this.index,
        name = this.name,
        type = this.type,
        size = this.size,
        alignment = this.alignment,
        maxHp = this.hitPoints,
        currentHp = this.hitPoints,
        armorClass = this.armorClass.firstOrNull()?.value ?: 10,
        speed = this.speed.walk ?: "0 ft.",
        strength = this.strength,
        dexterity = this.dexterity,
        constitution = this.constitution,
        intelligence = this.intelligence,
        wisdom = this.wisdom,
        charisma = this.charisma,
        challengeRating = this.challengeRating,
        xp = this.xp,
        actions = this.actions,
        imgUri = this.image
    )
}

data class MonsterSpeed(
    val walk: String? = null,
    val fly: String? = null,
    val swim: String? = null,
    val climb: String? = null
)

data class MonsterAction(
    val name: String,
    val desc: String
)

data class ArmorClass(
    val type: String,
    val value: Int
)
