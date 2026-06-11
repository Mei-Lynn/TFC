package com.utad.tfg.model.classes.fighter

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class BattleMaster : Subclass {
    override val subclassName: String = "Battle Master"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Combat Superiority",
            description = "When you choose this archetype at 3rd level, you learn maneuvers that are fueled by special dice called superiority dice.",
            levelRequired = 3,
            trigger = Trigger.OnAttackRoll,
            effect = { /* Maneuver logic */ }
        ),
        ClassFeature(
            name = "Student of War",
            description = "At 3rd level, you gain proficiency with one type of artisan's tools of your choice.",
            levelRequired = 3,
            trigger = Trigger.OnTurnStart,
            effect = { /* Tool proficiency logic */ }
        ),
        ClassFeature(
            name = "Know Your Enemy",
            description = "Starting at 7th level, if you spend at least 1 minute observing or interacting with another creature outside combat, you can learn certain information about its capabilities compared to your own.",
            levelRequired = 7,
            trigger = Trigger.OnTurnStart,
            effect = { /* Intel logic */ }
        ),
        ClassFeature(
            name = "Improved Combat Superiority",
            description = "At 10th level, your superiority dice turn into d10s. At 18th level, they turn into d12s.",
            levelRequired = 10,
            trigger = Trigger.OnDamageCalculation,
            effect = { /* Dice upgrade logic */ }
        ),
        ClassFeature(
            name = "Relentless",
            description = "Starting at 15th level, when you roll initiative and have no superiority dice remaining, you regain one superiority die.",
            levelRequired = 15,
            trigger = Trigger.OnTurnStart,
            effect = { /* Resource recovery logic */ }
        )
    )
}
