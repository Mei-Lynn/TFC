package com.utad.tfg.model.classes.artificer

import com.utad.tfg.model.Ability
import com.utad.tfg.model.ArmorType
import com.utad.tfg.model.WeaponType
import com.utad.tfg.model.classes.*

class Artificer(override var level: Int = 1) : Class {
    override val className: String = "Artificer"
    override val classIndex: String = "artificer"
    override val primaryAbility: Ability = Ability.Intelligence
    override val savingThrows: List<Ability> = listOf(Ability.Constitution, Ability.Intelligence)
    override val armorProficiencies: List<ArmorType> = listOf(ArmorType.Light, ArmorType.Medium, ArmorType.Shields)
    override val weaponProficiencies: List<WeaponType> = listOf(WeaponType.Simple)
    override val hitDie: Int = 8

    override val cantrips: List<Int> = listOf(0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4)
    override val spellSlots: List<List<Int>> = listOf(
        emptyList(),
        listOf(2), // 1
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

    override val spells: List<List<Int>> = listOf(
        0, 3, 4, 4, 6, 6, 7, 7, 9, 9, 10, 10, 11, 11, 12, 12, 13, 13, 14, 14, 15
    ).mapIndexed { level, total ->
        if (level == 0) emptyList()
        else {
            val maxSpellLevel = spellSlots[level].size
            List(maxSpellLevel + 1) { if (it == 0) 0 else total }
        }
    }

    override val uniqueResources: List<ClassResource> = listOf(
        ClassResource.SimplePool(
            name = "Infusions Known",
            amount = listOf(0, 0, 4, 4, 4, 4, 6, 6, 6, 6, 8, 8, 8, 8, 10, 10, 10, 10, 12, 12, 12)
        ),
        ClassResource.SimplePool(
            name = "Infused Items",
            amount = listOf(0, 0, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6)
        )
    )

    override val baseFeatures: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Magical Tinkering",
            description = "You learn how to invest a spark of magic into mundane objects.",
            levelRequired = 1,
            trigger = Trigger.OnTurnStart,
            effect = { }
        ),
        ClassFeature(
            name = "Infuse Item",
            description = "Whenever you finish a long rest, you can touch a nonmagical object and imbue it with one of your artificer infusions.",
            levelRequired = 2,
            trigger = Trigger.OnTurnStart,
            effect = { }
        )
    )

    override var selectedSubclass: Subclass? = null

    override var actionsPerTurn: Int = 1
    override var bonusActionsPerTurn: Int = 1
    override val subclassProgression: List<Int> = listOf(3, 5, 9, 15)
    override var attacksPerAction: Int = 1
}
