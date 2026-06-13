package com.utad.tfg.model.classes.barbarian

import com.utad.tfg.local.entities.Character
import com.utad.tfg.model.Ability
import com.utad.tfg.model.ArmorType
import com.utad.tfg.model.WeaponType
import com.utad.tfg.model.classes.*

class Barbarian(override var level: Int = 1) : Class {
    override val className: String = "Barbarian"
    override val classIndex: String = "barbarian"
    override val primaryAbility: Ability = Ability.Strength
    override val savingThrows: List<Ability> = listOf(Ability.Strength, Ability.Constitution)
    override val armorProficiencies: List<ArmorType> = listOf(ArmorType.Light, ArmorType.Medium, ArmorType.Shields)
    override val weaponProficiencies: List<WeaponType> = listOf(WeaponType.Simple, WeaponType.Martial)
    override val hitDie: Int = 12

    override val cantrips: List<Int> = List(21) { 0 }
    override fun getPreparedSpellsLimit(char: Character): Int {
       return 0
    }
    override val spellSlots: List<List<Int>> = List(21) { emptyList() }

    override val uniqueResources: List<ClassResource> = listOf(
        ClassResource.SimplePool(
            name = "Rages",
            amount = listOf(0, 2, 2, 3, 3, 3, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 6, 6, 6, 999)
        ),
        ClassResource.SimplePool(
            name = "Rage Damage",
            amount = listOf(0, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4)
        )
    )

    override val asiLevels = listOf(4, 8, 12, 16, 19)

    override val baseFeatures: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Rage",
            description = "In battle, you fight with primal ferocity. On your turn, you can enter a rage as a bonus action.",
            levelRequired = 1,
            trigger = Trigger.OnTurnStart,
            effect = { /* Rage logic */ }
        ),
        ClassFeature(
            name = "Unarmored Defense",
            description = "While you are not wearing any armor, your Armor Class equals 10 + your Dexterity modifier + your Constitution modifier. You can use a shield and still gain this benefit.",
            levelRequired = 1,
            trigger = Trigger.OnTurnStart,
            effect = { /* AC adjustment logic */ }
        ),
        ClassFeature(
            name = "Reckless Attack",
            description = "Starting at 2nd level, you can throw aside all concern for defense to attack with fierce desperation.",
            levelRequired = 2,
            trigger = Trigger.OnAttackRoll,
            effect = { /* Advantage logic */ }
        )
    )
    
    override var selectedSubclass: Subclass? = null

    override var actionsPerTurn: Int = 1
    override var bonusActionsPerTurn: Int = 1
    override val subclassProgression: List<Int> = listOf(3, 6, 10, 14)
    override var attacksPerAction: Int = if (level >= 5) 2 else 1
}
