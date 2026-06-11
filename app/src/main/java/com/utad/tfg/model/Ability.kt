package com.utad.tfg.model

sealed class Ability(val name: String, val abbreviation: String) {
    data object Strength : Ability("Strength", "STR")
    data object Dexterity : Ability("Dexterity", "DEX")
    data object Constitution : Ability("Constitution", "CON")
    data object Intelligence : Ability("Intelligence", "INT")
    data object Wisdom : Ability("Wisdom", "WIS")
    data object Charisma : Ability("Charisma", "CHA")

    companion object {
        val allAbilities: List<Ability>
            get() = listOf(
                Strength,
                Dexterity,
                Constitution,
                Intelligence,
                Wisdom,
                Charisma
            )

        fun fromAbbreviation(abb: String): Ability? {
            return allAbilities.find { it.abbreviation.equals(abb, ignoreCase = true) }
        }

        fun calculateModifier(score: Int): Int = (score - 10) / 2
    }
}
