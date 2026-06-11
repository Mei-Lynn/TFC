package com.utad.tfg.model.classes.monk

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class OpenHand : Subclass {
    override val subclassName: String = "Way of the Open Hand"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Open Hand Technique",
            description = "Starting when you choose this tradition at 3rd level, you can manipulate your enemy's ki when you harness your own.",
            levelRequired = 3,
            trigger = Trigger.OnAttackRoll,
            effect = { /* Knockdown/Push logic */ }
        ),
        ClassFeature(
            name = "Wholeness of Body",
            description = "At 6th level, you gain the ability to heal yourself. As an action, you can regain hit points equal to three times your monk level.",
            levelRequired = 6,
            trigger = Trigger.OnTurnStart,
            effect = { /* Self heal logic */ }
        ),
        ClassFeature(
            name = "Tranquility",
            description = "Beginning at 11th level, you can enter a special meditation that surrounds you with an aura of peace.",
            levelRequired = 11,
            trigger = Trigger.OnTurnStart,
            effect = { /* Sanctuary logic */ }
        ),
        ClassFeature(
            name = "Quivering Palm",
            description = "At 17th level, you gain the ability to set up lethal vibrations in someone's body.",
            levelRequired = 17,
            trigger = Trigger.OnAttackRoll,
            effect = { /* Lethal damage logic */ }
        )
    )
}
