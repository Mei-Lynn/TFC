package com.utad.tfg.model

import com.utad.tfg.model.equipment.Weapon

sealed class CombatAction(
    val name: String,
    val description: String,
    val actionType: ActionType,
    val range: String,
    val targetType: TargetType
) {
    data class WeaponAttack(
        val weaponName: String,
        val weaponDescription: String,
        val weaponRange: String,
        val damageDice: String,
        val damageType: String,
        val isOffHand: Boolean = false,
        val isVersatile: Boolean = false
    ) : CombatAction(
        name = if (isOffHand) "$weaponName (Off-hand)" else weaponName,
        description = weaponDescription,
        actionType = if (isOffHand) ActionType.BONUS_ACTION else ActionType.ACTION,
        range = weaponRange,
        targetType = TargetType.SINGLE
    )

    data class SpellCast(
        val spellName: String,
        val spellDescription: String,
        val spellRange: String,
        val spellDamageDice: String?,
        val spellDamageType: String?,
        val spellLevel: Int,
        val isConcentration: Boolean,
        val castingTime: String
    ) : CombatAction(
        name = spellName,
        description = spellDescription,
        actionType = parseActionType(castingTime),
        range = spellRange,
        targetType = TargetType.SINGLE // Simplification for now
    )

    data class ClassAbility(
        val abilityName: String,
        val abilityDescription: String,
        val abilityActionType: ActionType,
        val abilityRange: String = "Self",
        val abilityTarget: TargetType = TargetType.SELF
    ) : CombatAction(
        name = abilityName,
        description = abilityDescription,
        actionType = abilityActionType,
        range = abilityRange,
        targetType = abilityTarget
    )

    companion object {
        private fun parseActionType(castingTime: String): ActionType {
            return when {
                castingTime.contains("bonus action", ignoreCase = true) -> ActionType.BONUS_ACTION
                castingTime.contains("reaction", ignoreCase = true) -> ActionType.REACTION
                else -> ActionType.ACTION
            }
        }
    }
}

enum class ActionType {
    ACTION,
    BONUS_ACTION,
    REACTION,
    OTHER
}

enum class TargetType {
    SELF,
    SINGLE,
    AREA
}
