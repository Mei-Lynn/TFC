package com.utad.tfg.model.classes.rogue

import com.utad.tfg.local.entities.Character
import com.utad.tfg.model.Ability
import com.utad.tfg.model.ArmorType
import com.utad.tfg.model.WeaponType
import com.utad.tfg.model.classes.*

class Rogue(override var level: Int = 1) : Class {
    override val className: String = "Rogue"
    override val classIndex: String = "rogue"
    override val primaryAbility: Ability = Ability.Dexterity
    override val savingThrows: List<Ability> = if (level >= 15) listOf(Ability.Dexterity, Ability.Intelligence, Ability.Wisdom) else listOf(Ability.Dexterity, Ability.Intelligence)
    override val armorProficiencies: List<ArmorType> = listOf(ArmorType.Light)
    override val weaponProficiencies: List<WeaponType> = listOf(WeaponType.Simple)
    override val hitDie: Int = 8

    override val cantrips: List<Int> = List(21) { 0 }
    override fun getPreparedSpellsLimit(char: Character): Int {
        return 0
    }
    override val spellSlots: List<List<Int>> = List(21) { emptyList() }

    override val uniqueResources: List<ClassResource> = listOf(
        ClassResource.DicePool(
            name = "Sneak Attack",
            count = {char -> when {
                char.level >= 19 -> 10
                char.level >= 17 -> 9
                char.level >= 15 -> 8
                char.level >= 13 -> 7
                char.level >= 11 -> 6
                char.level >= 9 -> 5
                char.level >= 7 -> 4
                char.level >= 5 -> 3
                char.level >= 3 -> 2
                else -> 1
            }},
            diceSize = { 6 }
        )
    )

    override val asiLevels = listOf(4, 8, 12, 16, 19)

    override val baseFeatures: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Sneak Attack",
            description = "Beginning at 1st level, you know how to strike subtly and exploit a foe's distraction.",
            levelRequired = 1,
            trigger = Trigger.OnDamageCalculation,
            effect = { /* Damage logic */ }
        ),
        ClassFeature(
            name = "Cunning Action",
            description = "Starting at 2nd level, your quick thinking and agility allow you to move and act quickly.",
            levelRequired = 2,
            trigger = Trigger.OnTurnStart,
            effect = { /* Bonus action utility */ }
        ),
        ClassFeature(
            name = "Uncanny Dodge",
            description = "Starting at 5th level, when an attacker that you can see hits you with an attack, you can use your reaction to halve the attack's damage against you.",
            levelRequired = 5,
            trigger = Trigger.OnTakeDamage,
            effect = { /* Reaction logic */ }
        ),
        ClassFeature(
            name = "Evasion",
            description = "Beginning at 7th level, you can nimbly dodge out of the way of certain area effects.",
            levelRequired = 7,
            trigger = Trigger.OnSaveThrow,
            effect = { /* Save logic */ }
        )
    )

    override var selectedSubclass: Subclass? = null

    override var actionsPerTurn: Int = 1
    override var bonusActionsPerTurn: Int = 1
    override val subclassProgression: List<Int> = listOf(3, 9, 13, 17)
    override var attacksPerAction: Int = 1
}
