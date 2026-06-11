package com.utad.tfg.model.classes.fighter

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class EldritchKnight : Subclass {
    override val subclassName: String = "Eldritch Knight"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Weapon Bond",
            description = "At 3rd level, you learn a ritual that creates a magical bond between yourself and one weapon.",
            levelRequired = 3,
            trigger = Trigger.OnTurnStart,
            effect = { /* Weapon recall logic */ }
        ),
        ClassFeature(
            name = "War Magic",
            description = "Beginning at 7th level, when you use your action to cast a cantrip, you can make one weapon attack as a bonus action.",
            levelRequired = 7,
            trigger = Trigger.OnSpellCast,
            effect = { /* Bonus attack logic */ }
        ),
        ClassFeature(
            name = "Eldritch Strike",
            description = "At 10th level, you learn how to make your weapon strikes undercut a creature's resistance to your spells.",
            levelRequired = 10,
            trigger = Trigger.OnAttackRoll,
            effect = { /* Save penalty logic */ }
        ),
        ClassFeature(
            name = "Arcane Charge",
            description = "At 15th level, you gain the ability to teleport up to 30 feet to an unoccupied space you can see when you use your Action Surge.",
            levelRequired = 15,
            trigger = Trigger.OnTurnStart,
            effect = { /* Teleport logic */ }
        ),
        ClassFeature(
            name = "Improved War Magic",
            description = "Starting at 18th level, when you use your action to cast a spell, you can make one weapon attack as a bonus action.",
            levelRequired = 18,
            trigger = Trigger.OnSpellCast,
            effect = { /* Bonus attack logic */ }
        )
    )
}
