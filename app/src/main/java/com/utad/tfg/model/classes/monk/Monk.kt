package com.utad.tfg.model.classes.monk

import com.utad.tfg.local.entities.Character
import com.utad.tfg.model.Ability
import com.utad.tfg.model.ArmorType
import com.utad.tfg.model.WeaponType
import com.utad.tfg.model.classes.*

class Monk(override var level: Int = 1) : Class {
    override val className: String = "Monk"
    override val classIndex: String = "monk"
    override val primaryAbility: Ability = Ability.Dexterity
    override val savingThrows: List<Ability> = if (level >= 14) Ability.allAbilities else listOf(Ability.Strength, Ability.Dexterity)
    override val armorProficiencies: List<ArmorType> = emptyList()
    override val weaponProficiencies: List<WeaponType> = listOf(WeaponType.Simple)
    override val hitDie: Int = 8

    override val cantrips: List<Int> = List(21) { 0 }
    override fun getPreparedSpellsLimit(char: Character): Int {
        return 0
    }
    override val spellSlots: List<List<Int>> = List(21) { emptyList() }

    override val uniqueResources: List<ClassResource> = listOf(
        ClassResource.SimplePool(
            name = "Ki Points",
            amount = List(21) { it }
        ),
        ClassResource.DicePool(
            name = "Martial Arts",
            count = { 1 },
            diceSize = {char ->
                when {
                    char.level >= 18 -> 10
                    char.level >= 11 -> 8
                    char.level >= 5 -> 6
                    else -> 4
                }
            }
        )
    )

    override val baseFeatures: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Unarmored Defense",
            description = "Beginning at 1st level, while you are wearing no armor and not wielding a shield, your AC equals 10 + your Dexterity modifier + your Wisdom modifier.",
            levelRequired = 1,
            trigger = Trigger.OnTurnStart,
            effect = { }
        ),
        ClassFeature(
            name = "Martial Arts",
            description = "Your practice of martial arts gives you mastery of combat styles that use unarmed strikes and monk weapons.",
            levelRequired = 1,
            trigger = Trigger.OnTurnStart,
            effect = { }
        ),
        ClassFeature(
            name = "Stunning Strike",
            description = "Starting at 5th level, you can interfere with the flow of ki in an opponent's body.",
            levelRequired = 5,
            trigger = Trigger.OnAttackRoll,
            effect = { }
        )
    )

    override var selectedSubclass: Subclass? = null

    override var actionsPerTurn: Int = 1
    override var bonusActionsPerTurn: Int = 1
    override val subclassProgression: List<Int> = listOf(3, 6, 11, 17)
    override var attacksPerAction: Int = if (level >= 5) 2 else 1
}
