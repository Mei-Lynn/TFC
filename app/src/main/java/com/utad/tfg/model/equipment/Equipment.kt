package com.utad.tfg.model.equipment

import com.utad.tfg.model.ArmorType
import com.utad.tfg.model.CombatAction
import com.utad.tfg.model.WeaponType

sealed class Equipment(
    val name: String,
    val index: String,
    open val imgUri: String? = null
)

data class Weapon(
    val weaponName: String,
    val weaponIndex: String,
    val damageDice: String,
    val damageType: String,
    val weaponType: WeaponType,
    val properties: List<WeaponProperty>,
    val reach: Int = 5,
    override val imgUri: String? = null
) : Equipment(weaponName, weaponIndex, imgUri) {
    fun isProficient(proficiencies: List<WeaponType>): Boolean {
        return proficiencies.contains(weaponType)
    }

    fun toCombatAction(isOffHand: Boolean = false): CombatAction.WeaponAttack {
        return CombatAction.WeaponAttack(
            weaponName = weaponName,
            weaponDescription = "A $weaponName that deals $damageDice $damageType damage.",
            weaponRange = if (properties.contains(WeaponProperty.REACH)) "10 ft" else "5 ft",
            damageDice = damageDice,
            damageType = damageType,
            isOffHand = isOffHand,
            isVersatile = properties.contains(WeaponProperty.VERSATILE)
        )
    }
}

data class Armor(
    val armorName: String,
    val armorIndex: String,
    val baseAc: Int,
    val armorType: ArmorType,
    val strengthRequired: Int = 0,
    val stealthDisadvantage: Boolean = false,
    override val imgUri: String? = null
) : Equipment(armorName, armorIndex, imgUri) {
    fun isProficient(proficiencies: List<ArmorType>): Boolean {
        return proficiencies.contains(armorType)
    }
}

enum class WeaponProperty(val description: String) {
    FINESSE("Use Strength or Dexterity for attack and damage rolls"),
    LIGHT("Small and easy to handle, can be used for two-weapon fighting"),
    HEAVY("Small creatures have disadvantage on attack rolls"),
    REACH("Adds 5 feet to the distance you can strike"),
    TWO_HANDED("Must be used with two hands"),
    VERSATILE("Can be used with one or two hands"),
    THROWN("Can be thrown for a ranged attack"),
    LOADING("Can fire only one piece of ammunition per action/bonus action"),
    AMMUNITION("Requires ammunition to use")
}
