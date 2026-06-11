package com.utad.tfg.model.classes

sealed class Trigger {
    data object OnAttackRoll : Trigger()
    data object OnDamageCalculation : Trigger()
    data object OnSaveThrow : Trigger()
    data object OnTurnStart : Trigger()
    data object OnTurnEnd : Trigger()
    data object OnSpellCast : Trigger()
    data object OnTakeDamage : Trigger()
}

/**
 * Placeholder for the data needed during a battle trigger.
 * This will be expanded as the battle simulator is developed.
 */
data class BattleContext(
    val characterId: Long,
    val targetId: Long? = null,
    val extraData: Map<String, Any> = emptyMap()
)

data class ClassFeature(
    val name: String,
    val description: String,
    val levelRequired: Int,
    val trigger: Trigger,
    val effect: (BattleContext) -> Unit
)

interface Subclass {
    val subclassName: String
    val features: List<ClassFeature>
}
