package com.utad.tfg.model.classes.bard

import com.utad.tfg.model.Ability
import com.utad.tfg.model.ArmorType
import com.utad.tfg.model.WeaponType
import com.utad.tfg.model.classes.*

class Bard(override var level: Int = 1) : Class {
    override val className: String = "Bard"
    override val classIndex: String = "bard"
    override val primaryAbility: Ability = Ability.Charisma
    override val savingThrows: List<Ability> = listOf(Ability.Dexterity, Ability.Charisma)
    override val armorProficiencies: List<ArmorType> = listOf(ArmorType.Light)
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

    override val spells: List<List<Int>> = listOf(
        0, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 15, 15, 16, 18, 19, 19, 20, 22, 22, 22
    ).mapIndexed { level, total ->
        if (level == 0) emptyList()
        else {
            val maxSpellLevel = spellSlots[level].size
            List(maxSpellLevel + 1) { if (it == 0) 0 else total }
        }
    }

    override val uniqueResources: List<ClassResource> = listOf(
        ClassResource.DicePool(
            name = "Bardic Inspiration",
            count = List(21) { 0 },
            diceSize = listOf(0, 6, 6, 6, 6, 8, 8, 8, 8, 8, 10, 10, 10, 10, 10, 12, 12, 12, 12, 12, 12)
        )
    )

    override val baseFeatures: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Bardic Inspiration",
            description = "You can inspire others through stirring words or music.",
            levelRequired = 1,
            trigger = Trigger.OnTurnStart,
            effect = { }
        ),
        ClassFeature(
            name = "Jack of All Trades",
            description = "Starting at 2nd level, you can add half your proficiency bonus, rounded down, to any ability check you make that doesn't already include your proficiency bonus.",
            levelRequired = 2,
            trigger = Trigger.OnTurnStart,
            effect = { }
        ),
        ClassFeature(
            name = "Font of Inspiration",
            description = "Beginning at 5th level, you regain all of your expended uses of Bardic Inspiration when you finish a short or long rest.",
            levelRequired = 5,
            trigger = Trigger.OnTurnStart,
            effect = { }
        )
    )

    override var selectedSubclass: Subclass? = null

    override var actionsPerTurn: Int = 1
    override var bonusActionsPerTurn: Int = 1
    override val subclassProgression: List<Int> = listOf(3, 6, 14)
    override var attacksPerAction: Int = 1
}
