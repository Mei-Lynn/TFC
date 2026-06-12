package com.utad.tfg.model.classes.cleric

import com.utad.tfg.model.Ability
import com.utad.tfg.model.ArmorType
import com.utad.tfg.model.WeaponType
import com.utad.tfg.model.classes.*

class Cleric(override var level: Int = 1) : Class {
    override val className: String = "Cleric"
    override val classIndex: String = "cleric"
    override val primaryAbility: Ability = Ability.Wisdom
    override val savingThrows: List<Ability> = listOf(Ability.Wisdom, Ability.Charisma)
    override val armorProficiencies: List<ArmorType> = listOf(ArmorType.Light, ArmorType.Medium, ArmorType.Shields)
    override val weaponProficiencies: List<WeaponType> = listOf(WeaponType.Simple)
    override val hitDie: Int = 8

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

    override val spells: List<List<Int>> = listOf(
        0, 4, 5, 6, 8, 9, 10, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25
    ).mapIndexed { level, total ->
        if (level == 0) emptyList()
        else {
            val maxSpellLevel = spellSlots[level].size
            List(maxSpellLevel + 1) { if (it == 0) 0 else total }
        }
    }

    override val uniqueResources: List<ClassResource> = listOf(
        ClassResource.SimplePool(
            name = "Channel Divinity",
            amount = listOf(0, 0, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3)
        )
    )

    override val baseFeatures: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Channel Divinity",
            description = "At 2nd level, you gain the ability to channel divine energy directly from your deity, using that energy to fuel magical effects.",
            levelRequired = 2,
            trigger = Trigger.OnTurnStart,
            effect = { }
        ),
        ClassFeature(
            name = "Divine Intervention",
            description = "Beginning at 10th level, you can call on your deity to intervene on your behalf when your need is great.",
            levelRequired = 10,
            trigger = Trigger.OnTurnStart,
            effect = { }
        )
    )

    override var selectedSubclass: Subclass? = null

    override var actionsPerTurn: Int = 1
    override var bonusActionsPerTurn: Int = 1
    override val subclassProgression: List<Int> = listOf(1, 2, 6, 8, 17)
    override var attacksPerAction: Int = 1
}
