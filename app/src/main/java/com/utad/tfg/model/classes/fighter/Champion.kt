package com.utad.tfg.model.classes.fighter

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class Champion : Subclass {
    override val subclassName: String = "Champion"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Improved Critical",
            description = "Beginning when you choose this archetype at 3rd level, your weapon attacks score a critical hit on a roll of 19 or 20.",
            levelRequired = 3,
            trigger = Trigger.OnAttackRoll,
            effect = { /* Crit range logic */ }
        ),
        ClassFeature(
            name = "Remarkable Athlete",
            description = "Starting at 7th level, you can add half your proficiency bonus (rounded up) to any Strength, Dexterity, or Constitution check you make that doesn't already use your proficiency bonus.",
            levelRequired = 7,
            trigger = Trigger.OnTurnStart,
            effect = { /* Check bonus logic */ }
        ),
        ClassFeature(
            name = "Additional Fighting Style",
            description = "At 10th level, you can choose a second option from the Fighting Style class feature.",
            levelRequired = 10,
            trigger = Trigger.OnTurnStart,
            effect = { /* Extra style logic */ }
        ),
        ClassFeature(
            name = "Superior Critical",
            description = "Starting at 15th level, your weapon attacks score a critical hit on a roll of 18-20.",
            levelRequired = 15,
            trigger = Trigger.OnAttackRoll,
            effect = { /* Crit range logic */ }
        ),
        ClassFeature(
            name = "Survivor",
            description = "At 18th level, you attain the pinnacle of resilience in battle. At the start of each of your turns, you regain hit points equal to 5 + your Constitution modifier if you have no more than half of your hit points left.",
            levelRequired = 18,
            trigger = Trigger.OnTurnStart,
            effect = { /* Regen logic */ }
        )
    )
}
