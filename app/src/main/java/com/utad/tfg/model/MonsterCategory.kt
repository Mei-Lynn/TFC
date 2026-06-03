package com.utad.tfg.model

sealed class MonsterCategory(val categoryName: String) {
    data object Aberration : MonsterCategory("Aberration")
    data object Beast : MonsterCategory("Beast")
    data object Celestial : MonsterCategory("Celestial")
    data object Construct : MonsterCategory("Construct")
    data object Dragon : MonsterCategory("Dragon")
    data object Elemental : MonsterCategory("Elemental")
    data object Fey : MonsterCategory("Fey")
    data object Fiend : MonsterCategory("Fiend")
    data object Giant : MonsterCategory("Giant")
    data object Humanoid : MonsterCategory("Humanoid")
    data object Monstrosity : MonsterCategory("Monstrosity")
    data object Ooze : MonsterCategory("Ooze")
    data object Plant : MonsterCategory("Plant")
    data object Undead : MonsterCategory("Undead")

    // Homebrew
    data class Homebrew(val customCategory: String) : MonsterCategory(customCategory)
}
