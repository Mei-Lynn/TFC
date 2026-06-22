package com.utad.tfg.model.classes.wizard

import com.utad.tfg.local.entities.Character
import com.utad.tfg.model.Ability
import com.utad.tfg.model.ArmorType
import com.utad.tfg.model.WeaponType
import com.utad.tfg.model.classes.*

class Wizard(override var level: Int = 1) : Class {
    override val className: String = "Wizard"
    override val classIndex: String = "wizard"
    override val imgUri: String? = ""
    override val primaryAbility: Ability = Ability.Intelligence
    override val savingThrows: List<Ability> = listOf(Ability.Intelligence, Ability.Wisdom)
    override val armorProficiencies: List<ArmorType> = emptyList()
    override val weaponProficiencies: List<WeaponType> = listOf(WeaponType.Simple)
    override val hitDie: Int = 6

    override val cantrips: List<Int> = listOf(0, 3, 3, 3, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5)
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
        return char.level + Ability.calculateModifier(char.intelligence)
    }

    override val uniqueResources: List<ClassResource> = emptyList()

    override val asiLevels = listOf(4, 8, 12, 16, 19)

    override val baseFeatures: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Arcane Recovery",
            description = "You have learned to regain some of your magical energy by studying your spellbook. Once per day when you finish a short rest, you can choose expended spell slots to recover.",
            levelRequired = 1,
            trigger = Trigger.OnTurnStart,
            effect = { }
        )
    )

    override var selectedSubclass: Subclass? = null

    override var actionsPerTurn: Int = 1
    override var bonusActionsPerTurn: Int = 1
    override val subclassProgression: List<Int> = listOf(2, 6, 10, 14)
    override var attacksPerAction: Int = 1
}
