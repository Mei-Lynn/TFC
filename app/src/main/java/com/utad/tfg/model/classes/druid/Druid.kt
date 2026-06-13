package com.utad.tfg.model.classes.druid

import com.utad.tfg.local.entities.Character
import com.utad.tfg.model.Ability
import com.utad.tfg.model.ArmorType
import com.utad.tfg.model.WeaponType
import com.utad.tfg.model.classes.*

class Druid(override var level: Int = 1) : Class {
    override val className: String = "Druid"
    override val classIndex: String = "druid"
    override val primaryAbility: Ability = Ability.Wisdom
    override val savingThrows: List<Ability> = listOf(Ability.Intelligence, Ability.Wisdom)
    override val armorProficiencies: List<ArmorType> = listOf(ArmorType.Light, ArmorType.Medium, ArmorType.Shields)
    override val weaponProficiencies: List<WeaponType> = listOf(WeaponType.Simple)
    override val hitDie: Int = 8

    override val cantrips: List<Int> = listOf(0, 2, 2, 2, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4)
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
        return char.level + Ability.calculateModifier(char.wisdom)
    }

    override val uniqueResources: List<ClassResource> = listOf(
        ClassResource.SimplePool(
            name = "Wild Shape",
            amount = listOf(0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 999)
        )
    )

    override val baseFeatures: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Wild Shape",
            description = "Starting at 2nd level, you can use your action to magically assume the shape of a beast that you have seen before.",
            levelRequired = 2,
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
