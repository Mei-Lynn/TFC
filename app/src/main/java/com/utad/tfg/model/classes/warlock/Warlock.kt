package com.utad.tfg.model.classes.warlock

import com.utad.tfg.model.Ability
import com.utad.tfg.model.ArmorType
import com.utad.tfg.model.WeaponType
import com.utad.tfg.model.classes.*

class Warlock(override var level: Int = 1) : Class {
    override val className: String = "Warlock"
    override val classIndex: String = "warlock"
    override val primaryAbility: Ability = Ability.Charisma
    override val savingThrows: List<Ability> = listOf(Ability.Wisdom, Ability.Charisma)
    override val armorProficiencies: List<ArmorType> = listOf(ArmorType.Light)
    override val weaponProficiencies: List<WeaponType> = listOf(WeaponType.Simple)
    override val hitDie: Int = 8

    override val cantrips: List<Int> = listOf(0, 2, 2, 2, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4)
    override val spells: List<List<Int>> = listOf(
        0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 11, 11, 12, 12, 13, 13, 14, 14, 15, 15
    ).mapIndexed { level, total ->
        if (level == 0) emptyList()
        else {
            val maxSpellLevel = when {
                level >= 9 -> 5
                level >= 7 -> 4
                level >= 5 -> 3
                level >= 3 -> 2
                else -> 1
            }
            List(maxSpellLevel + 1) { if (it == 0) 0 else total }
        }
    }
    override val spellSlots: List<List<Int>> = List(21) { emptyList() }

    override val uniqueResources: List<ClassResource> = listOf(
        ClassResource.LevelledSlots(
            name = "Pact Magic Slots",
            count = listOf(0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4),
            level = listOf(0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5)
        )
    )

    override val baseFeatures: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Pact Magic",
            description = "Your arcane research and the magic bestowed on you by your patron have given you facility with spells.",
            levelRequired = 1,
            trigger = Trigger.OnSpellCast,
            effect = { }
        ),
        ClassFeature(
            name = "Eldritch Invocations",
            description = "In your study of occult lore, you have unearthed eldritch invocations, fragments of forbidden knowledge that imbue you with an abiding magical ability.",
            levelRequired = 2,
            trigger = Trigger.OnTurnStart,
            effect = { }
        )
    )

    override var selectedSubclass: Subclass? = null

    override var actionsPerTurn: Int = 1
    override var bonusActionsPerTurn: Int = 1
    override val subclassProgression: List<Int> = listOf(1, 6, 10, 14)
    override var attacksPerAction: Int = 1
}
