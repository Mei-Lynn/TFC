package com.utad.tfg.model.classes.barbarian

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class TotemWarrior : Subclass {
    override val subclassName: String = "Path of the Totem Warrior"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Spirit Seeker",
            description = "Yours is a path that seeks attunement with the natural world, giving you a kinship with beasts.",
            levelRequired = 3,
            trigger = Trigger.OnTurnStart,
            effect = { /* Ritual logic */ }
        ),
        ClassFeature(
            name = "Totem Spirit",
            description = "At 3rd level, when you adopt this path, you choose a totem spirit and gain its feature.",
            levelRequired = 3,
            trigger = Trigger.OnTakeDamage,
            effect = { /* Resistance/Utility logic */ }
        ),
        ClassFeature(
            name = "Aspect of the Beast",
            description = "At 6th level, you gain a magical benefit based on the totem animal of your choice.",
            levelRequired = 6,
            trigger = Trigger.OnTurnStart,
            effect = { /* Utility bonus logic */ }
        ),
        ClassFeature(
            name = "Spirit Walker",
            description = "At 10th level, you can cast the commune with nature spell as a ritual.",
            levelRequired = 10,
            trigger = Trigger.OnTurnStart,
            effect = { /* Ritual logic */ }
        ),
        ClassFeature(
            name = "Totemic Attunement",
            description = "At 14th level, you gain a magical benefit based on a totem animal of your choice.",
            levelRequired = 14,
            trigger = Trigger.OnTurnStart,
            effect = { /* Combat bonus logic */ }
        )
    )
}
