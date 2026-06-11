package com.utad.tfg.model.classes.rogue

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class Assassin : Subclass {
    override val subclassName: String = "Assassin"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Bonus Proficiencies",
            description = "When you choose this archetype at 3rd level, you gain proficiency with the disguise kit and the poisoner's kit.",
            levelRequired = 3,
            trigger = Trigger.OnTurnStart,
            effect = { /* Tool proficiency logic */ }
        ),
        ClassFeature(
            name = "Assassinate",
            description = "Starting at 3rd level, you are at your deadliest when you get the drop on your enemies. You have advantage on attack rolls against any creature that hasn't taken a turn in the combat yet. In addition, any hit you score against a creature that is surprised is a critical hit.",
            levelRequired = 3,
            trigger = Trigger.OnAttackRoll,
            effect = { /* Advantage and Auto-crit logic */ }
        ),
        ClassFeature(
            name = "Infiltration Expertise",
            description = "Starting at 9th level, you can unfailingly create false identities for yourself.",
            levelRequired = 9,
            trigger = Trigger.OnTurnStart,
            effect = { /* Identity logic */ }
        ),
        ClassFeature(
            name = "Impostor",
            description = "At 13th level, you gain the ability to unerringly mimic another person's speech, writing, and behavior.",
            levelRequired = 13,
            trigger = Trigger.OnTurnStart,
            effect = { /* Mimicry logic */ }
        ),
        ClassFeature(
            name = "Death Strike",
            description = "Starting at 17th level, you become a master of instant death. When you attack and hit a creature that is surprised, it must make a Constitution saving throw. On a failed save, double the damage of your attack against the creature.",
            levelRequired = 17,
            trigger = Trigger.OnDamageCalculation,
            effect = { /* Double damage logic */ }
        )
    )
}
