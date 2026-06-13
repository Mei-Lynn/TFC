package com.utad.tfg.model.classes.fighter

import com.utad.tfg.local.entities.Character
import com.utad.tfg.model.Ability
import com.utad.tfg.model.ArmorType
import com.utad.tfg.model.WeaponType
import com.utad.tfg.model.classes.*

class Fighter(override var level: Int = 1) : Class {
    override val className: String = "Fighter"
    override val classIndex: String = "fighter"
    override val primaryAbility: Ability = Ability.Strength
    override val savingThrows: List<Ability> = listOf(Ability.Strength, Ability.Constitution)
    override val armorProficiencies: List<ArmorType> = listOf(ArmorType.Light, ArmorType.Medium, ArmorType.Heavy, ArmorType.Shields)
    override val weaponProficiencies: List<WeaponType> = listOf(WeaponType.Simple, WeaponType.Martial)
    override val hitDie: Int = 10

    override val cantrips: List<Int> = List(21) { 0 }
    override fun getPreparedSpellsLimit(char: Character): Int {
        return 0
    }
    override val spellSlots: List<List<Int>> = List(21) { emptyList() }

    override val uniqueResources: List<ClassResource> = emptyList()

    override val asiLevels = listOf(4, 6, 8, 12, 14, 16, 19)

    override val baseFeatures: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Second Wind",
            description = "You have a limited well of stamina that you can draw on to protect yourself from harm. On your turn, you can use a bonus action to regain hit points equal to 1d10 + your fighter level.",
            levelRequired = 1,
            trigger = Trigger.OnTurnStart,
            effect = { /* Heal logic */ }
        ),
        ClassFeature(
            name = "Action Surge",
            description = "Starting at 2nd level, you can push yourself beyond your normal limits for a moment. On your turn, you can take one additional action on top of your regular action and a bonus action.",
            levelRequired = 2,
            trigger = Trigger.OnTurnStart,
            effect = { /* Action gain logic */ }
        ),
        ClassFeature(
            name = "Indomitable",
            description = "Beginning at 9th level, you can reroll a saving throw that you fail. If you do so, you must use the new roll, and you can't use this feature again until you finish a long rest.",
            levelRequired = 9,
            trigger = Trigger.OnSaveThrow,
            effect = { /* Reroll logic */ }
        )
    )

    override var selectedSubclass: Subclass? = null

    override var actionsPerTurn: Int = 1
    override var bonusActionsPerTurn: Int = 1
    override val subclassProgression: List<Int> = listOf(3, 7, 10, 15, 18)
    override var attacksPerAction: Int = when {
        level >= 20 -> 4
        level >= 11 -> 3
        level >= 5 -> 2
        else -> 1
    }
}
