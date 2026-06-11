package com.utad.tfg.model.classes.warlock

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class Archfey : Subclass {
    override val subclassName: String = "The Archfey"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Fey Presence",
            description = "Starting at 1st level, your patron bestows upon you the ability to project the beguiling and fearsome presence of the fey.",
            levelRequired = 1,
            trigger = Trigger.OnTurnStart,
            effect = { /* Charm/Frighten logic */ }
        ),
        ClassFeature(
            name = "Misty Escape",
            description = "Starting at 6th level, you can vanish in a puff of mist when you take damage. When you take damage, you can use your reaction to become invisible and teleport up to 60 feet to an unoccupied space you can see.",
            levelRequired = 6,
            trigger = Trigger.OnTakeDamage,
            effect = { /* Teleport and Invisibility logic */ }
        ),
        ClassFeature(
            name = "Beguiling Defenses",
            description = "Beginning at 10th level, your patron teaches you how to turn the mind-affecting magic of your enemies against them. You are immune to being charmed, and when another creature attempts to charm you, you can use your reaction to attempt to turn the charm back on that creature.",
            levelRequired = 10,
            trigger = Trigger.OnTurnStart,
            effect = { /* Charm immunity/reflect logic */ }
        ),
        ClassFeature(
            name = "Dark Delirium",
            description = "Starting at 14th level, you can plunge a creature into an illusory realm.",
            levelRequired = 14,
            trigger = Trigger.OnTurnStart,
            effect = { /* Illusion logic */ }
        )
    )
}
