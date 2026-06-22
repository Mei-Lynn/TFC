package com.utad.tfg.model.classes.ranger

import com.utad.tfg.local.entities.Character
import com.utad.tfg.model.Ability
import com.utad.tfg.model.ArmorType
import com.utad.tfg.model.WeaponType
import com.utad.tfg.model.classes.*

class Ranger(override var level: Int = 1) : Class {
    override val className: String = "Ranger"
    override val classIndex: String = "ranger"
    override val imgUri: String? = ""
    override val primaryAbility: Ability = Ability.Dexterity
    override val savingThrows: List<Ability> = listOf(Ability.Strength, Ability.Dexterity)
    override val armorProficiencies: List<ArmorType> = listOf(ArmorType.Light, ArmorType.Medium, ArmorType.Shields)
    override val weaponProficiencies: List<WeaponType> = listOf(WeaponType.Simple, WeaponType.Martial)
    override val hitDie: Int = 10

    override val cantrips: List<Int> = List(21) { 0 }
    override val spellSlots: List<List<Int>> = listOf(
        emptyList(),
        emptyList(), // 1
        listOf(2), // 2
        listOf(3), // 3
        listOf(3), // 4
        listOf(4, 2), // 5
        listOf(4, 2), // 6
        listOf(4, 3), // 7
        listOf(4, 3), // 8
        listOf(4, 3, 2), // 9
        listOf(4, 3, 2), // 10
        listOf(4, 3, 3), // 11
        listOf(4, 3, 3), // 12
        listOf(4, 3, 3, 1), // 13
        listOf(4, 3, 3, 1), // 14
        listOf(4, 3, 3, 2), // 15
        listOf(4, 3, 3, 2), // 16
        listOf(4, 3, 3, 3, 1), // 17
        listOf(4, 3, 3, 3, 1), // 18
        listOf(4, 3, 3, 3, 2), // 19
        listOf(4, 3, 3, 3, 2)  // 20
    ).let { list -> List(21) { if (it < list.size) list[it] else list.last() } }

    override fun getPreparedSpellsLimit(char: Character): Int {
        val spellsKnown = listOf(0, 2, 3, 4, 5, 6, 6, 7, 7, 9, 9, 10, 10, 11, 11, 12, 12, 14, 14, 15, 15)
        return spellsKnown[char.level]
    }

    override val uniqueResources: List<ClassResource> = emptyList()

    override val asiLevels = listOf(4, 8, 12, 16, 19)

    override val baseFeatures: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Favored Enemy",
            description = "Beginning at 1st level, you have significant experience studying, tracking, hunting, and even talking to a certain type of enemy.",
            levelRequired = 1,
            trigger = Trigger.OnTurnStart,
            effect = { }
        ),
        ClassFeature(
            name = "Natural Explorer",
            description = "You are particularly familiar with one type of natural environment and are adept at traveling and surviving in such regions.",
            levelRequired = 1,
            trigger = Trigger.OnTurnStart,
            effect = { }
        )
    )

    override var selectedSubclass: Subclass? = null

    override var actionsPerTurn: Int = 1
    override var bonusActionsPerTurn: Int = 1
    override val subclassProgression: List<Int> = listOf(3, 7, 11, 15)
    override var attacksPerAction: Int = if (level >= 5) 2 else 1
}
