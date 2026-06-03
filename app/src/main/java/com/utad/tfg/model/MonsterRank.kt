package com.utad.tfg.model

sealed class MonsterRank(val rankName: String) {
    data object Standard : MonsterRank("Standard")
    data object Minion : MonsterRank("Minion")
    data object Elite : MonsterRank("Elite")
    data object Boss : MonsterRank("Boss")
    data object Legendary : MonsterRank("Legendary")
    data object Mythic : MonsterRank("Mythic")
    data object Swarm : MonsterRank("Swarm")

    // Homebrew
    data class Homebrew(val customRank: String) : MonsterRank(customRank)
}
