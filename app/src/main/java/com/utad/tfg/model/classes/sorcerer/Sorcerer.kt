package com.utad.tfg.model.classes.sorcerer

import com.utad.tfg.local.entities.Character
import com.utad.tfg.model.Ability
import com.utad.tfg.model.ArmorType
import com.utad.tfg.model.WeaponType
import com.utad.tfg.model.classes.*

class Sorcerer(override var level: Int = 1) : Class {
    override val className: String = "Sorcerer"
    override val classIndex: String = "sorcerer"
    override val primaryAbility: Ability = Ability.Charisma
    override val savingThrows: List<Ability> = listOf(Ability.Constitution, Ability.Charisma)
    override val armorProficiencies: List<ArmorType> = emptyList()
    override val weaponProficiencies: List<WeaponType> = listOf(WeaponType.Simple)
    override val hitDie: Int = 6

    override val cantrips: List<Int> = listOf(0, 4, 4, 4, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6)
    override val spellSlots: List<List<Int>> = listOf(
        emptyList(),
        listOf(2), // 1
        listOf(3), // 2
        listOf(4, 2), // 3
        listOf(4, 3), // 4
        listOf(4, 3, 2), // 5
        listOf(4, 3, 3), // 6
        listOf(4, 3, 3, 1), // 7
        listOf(4, 3, 3, 2), // 8
        listOf(4, 3, 3, 3, 1), // 9
        listOf(4, 3, 3, 3, 2), // 10
        listOf(4, 3, 3, 3, 2, 1), // 11
        listOf(4, 3, 3, 3, 2, 1), // 12
        listOf(4, 3, 3, 3, 2, 1, 1), // 13
        listOf(4, 3, 3, 3, 2, 1, 1), // 14
        listOf(4, 3, 3, 3, 2, 1, 1, 1), // 15
        listOf(4, 3, 3, 3, 2, 1, 1, 1), // 16
        listOf(4, 3, 3, 3, 2, 1, 1, 1, 1), // 17
        listOf(4, 3, 3, 3, 3, 1, 1, 1, 1), // 18
        listOf(4, 3, 3, 3, 3, 2, 1, 1, 1), // 19
        listOf(4, 3, 3, 3, 3, 2, 2, 1, 1)  // 20
    ).let { list -> List(21) { if (it < list.size) list[it] else list.last() } }

    override fun getPreparedSpellsLimit(char: Character): Int {
        val spellsKnown = listOf(0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 12, 13, 13, 14, 14, 15, 15, 15, 15)
        return spellsKnown[char.level]
    }

    override val uniqueResources: List<ClassResource> = listOf(
        ClassResource.SimplePool(
            name = "Sorcery Points",
            amount = List(21) { it }
        )
    )

    override val baseFeatures: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Font of Magic",
            description = "At 2nd level, you tap into a deep wellspring of magic within yourself. This wellspring is represented by sorcery points.",
            levelRequired = 2,
            trigger = Trigger.OnTurnStart,
            effect = { }
        ),
        ClassFeature(
            name = "Metamagic",
            description = "At 3rd level, you gain the ability to twist your spells to suit your needs.",
            levelRequired = 3,
            trigger = Trigger.OnSpellCast,
            effect = { }
        )
    )

    override var selectedSubclass: Subclass? = null

    override var actionsPerTurn: Int = 1
    override var bonusActionsPerTurn: Int = 1
    override val subclassProgression: List<Int> = listOf(1, 6, 14, 18)
    override var attacksPerAction: Int = 1
}
