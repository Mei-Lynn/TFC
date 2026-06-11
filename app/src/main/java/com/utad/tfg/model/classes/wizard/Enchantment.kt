package com.utad.tfg.model.classes.wizard

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class Enchantment : Subclass {
    override val subclassName: String = "School of Enchantment"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Enchantment Savant",
            description = "The gold and time you must spend to copy an enchantment spell into your spellbook is halved.",
            levelRequired = 2,
            trigger = Trigger.OnTurnStart,
            effect = { /* Copying logic */ }
        ),
        ClassFeature(
            name = "Hypnotic Gaze",
            description = "Starting at 2nd level when you choose this school, your soft words and enchanting gaze can bind a creature into a magical lethargy.",
            levelRequired = 2,
            trigger = Trigger.OnTurnStart,
            effect = { /* Incapacitate logic */ }
        ),
        ClassFeature(
            name = "Instinctive Charm",
            description = "Beginning at 6th level, when a creature you can see within 30 feet of you makes an attack roll against you, you can use your reaction to potentially divert the attack.",
            levelRequired = 6,
            trigger = Trigger.OnAttackRoll,
            effect = { /* Redirect attack logic */ }
        ),
        ClassFeature(
            name = "Split Enchantment",
            description = "Starting at 10th level, when you cast an enchantment spell of 1st level or higher that targets only one creature, you can have it target a second creature.",
            levelRequired = 10,
            trigger = Trigger.OnSpellCast,
            effect = { /* Double target logic */ }
        ),
        ClassFeature(
            name = "Alter Memories",
            description = "At 14th level, you gain the ability to make a creature unaware of your magical influence on it.",
            levelRequired = 14,
            trigger = Trigger.OnTurnStart,
            effect = { /* Memory logic */ }
        )
    )
}
