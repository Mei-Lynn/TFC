package com.utad.tfg.model.classes.paladin

import com.utad.tfg.model.Ability
import com.utad.tfg.model.ArmorType
import com.utad.tfg.model.WeaponType
import com.utad.tfg.model.classes.*

class Paladin(override var level: Int = 1) : Class {
    override val className: String = "Paladin"
    override val classIndex: String = "paladin"
    override val primaryAbility: Ability = Ability.Strength
    override val savingThrows: List<Ability> = listOf(Ability.Wisdom, Ability.Charisma)
    override val armorProficiencies: List<ArmorType> = listOf(ArmorType.Light, ArmorType.Medium, ArmorType.Heavy, ArmorType.Shields)
    override val weaponProficiencies: List<WeaponType> = listOf(WeaponType.Simple, WeaponType.Martial)
    override val hitDie: Int = 10

    override val cantrips: List<Int> = List(21) { 0 }
    override val spells: List<List<Int>> = List(21) { emptyList() }
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

    override val uniqueResources: List<ClassResource> = listOf(
        ClassResource.SimplePool(
            name = "Lay on Hands",
            amount = List(21) { it * 5 }
        )
    )

    override val baseFeatures: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Divine Smite",
            description = "Starting at 2nd level, when you hit a creature with a melee weapon attack, you can expend one spell slot to deal radiant damage to the target.",
            levelRequired = 2,
            trigger = Trigger.OnDamageCalculation,
            effect = { }
        ),
        ClassFeature(
            name = "Aura of Protection",
            description = "Starting at 6th level, whenever you or a friendly creature within 10 feet of you must make a saving throw, the creature gains a bonus to the saving throw equal to your Charisma modifier.",
            levelRequired = 6,
            trigger = Trigger.OnSaveThrow,
            effect = { }
        )
    )

    override var selectedSubclass: Subclass? = null

    override var actionsPerTurn: Int = 1
    override var bonusActionsPerTurn: Int = 1
    override val subclassProgression: List<Int> = listOf(3, 7, 15, 20)
    override var attacksPerAction: Int = if (level >= 5) 2 else 1
}
