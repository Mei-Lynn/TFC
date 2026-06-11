package com.utad.tfg.model.classes

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
        val count: List<Int>,
        val diceSize: List<Int>
    ) : ClassResource()
}
