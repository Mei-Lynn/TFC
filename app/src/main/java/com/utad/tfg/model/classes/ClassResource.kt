package com.utad.tfg.model.classes

import com.utad.tfg.local.entities.Character
sealed class ClassResource {
    abstract val name: String

    data class SimplePool(
        override val name: String,
        val amount: List<Int>
    ) : ClassResource()

    data class LevelledSlots(
        override val name: String,
        val count: List<Int>,
        val level: List<Int>
    ) : ClassResource()

    data class DicePool(
        override val name: String,
        val count: (character: Character) -> Int,
        val diceSize: (character: Character) -> Int
    ) : ClassResource()
}
