package com.utad.tfg.model.classes.ranger

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class BeastMaster : Subclass {
    override val subclassName: String = "Beast Master"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Ranger's Companion",
            description = "At 3rd level, you gain a beast companion that accompanies you on your adventures and fights alongside you.",
            levelRequired = 3,
            trigger = Trigger.OnTurnStart,
            effect = { /* Companion logic */ }
        ),
        ClassFeature(
            name = "Exceptional Training",
            description = "Beginning at 7th level, on any of your turns when your beast companion doesn't attack, you can use a bonus action to command the beast to take the Dash, Disengage, or Help action on its turn.",
            levelRequired = 7,
            trigger = Trigger.OnTurnStart,
            effect = { /* Companion bonus logic */ }
        ),
        ClassFeature(
            name = "Bestial Fury",
            description = "Starting at 11th level, when you command your beast companion to take the Attack action, the beast can make two attacks, or it can take the Multiattack action if it has that action.",
            levelRequired = 11,
            trigger = Trigger.OnTurnStart,
            effect = { /* Companion multiattack */ }
        ),
        ClassFeature(
            name = "Share Spells",
            description = "Beginning at 15th level, when you cast a spell targeting yourself, you can also affect your beast companion with the spell if the beast is within 30 feet of you.",
            levelRequired = 15,
            trigger = Trigger.OnSpellCast,
            effect = { /* Spell share logic */ }
        )
    )
}
