package com.utad.tfg.model.classes.barbarian

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class Berserker : Subclass {
    override val subclassName: String = "Path of the Berserker"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Frenzy",
            description = "Starting when you choose this path at 3rd level, you can go into a frenzy when you rage.",
            levelRequired = 3,
            trigger = Trigger.OnTurnStart,
            effect = { /* Frenzy logic */ }
        ),
        ClassFeature(
            name = "Mindless Rage",
            description = "Beginning at 6th level, you can't be charmed or frightened while raging.",
            levelRequired = 6,
            trigger = Trigger.OnTurnStart,
            effect = { /* Immunity logic */ }
        ),
        ClassFeature(
            name = "Intimidating Presence",
            description = "Beginning at 10th level, you can use your action to frighten someone with your menacing presence.",
            levelRequired = 10,
            trigger = Trigger.OnTurnStart,
            effect = { /* Frighten logic */ }
        ),
        ClassFeature(
            name = "Retaliation",
            description = "Starting at 14th level, when you take damage from a creature that is within 5 feet of you, you can use your reaction to make a melee weapon attack against that creature.",
            levelRequired = 14,
            trigger = Trigger.OnTakeDamage,
            effect = { /* Reaction attack logic */ }
        )
    )
}
