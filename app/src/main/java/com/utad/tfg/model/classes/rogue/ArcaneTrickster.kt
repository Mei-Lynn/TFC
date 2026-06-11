package com.utad.tfg.model.classes.rogue

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class ArcaneTrickster : Subclass {
    override val subclassName: String = "Arcane Trickster"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Mage Hand Legerdemain",
            description = "Starting at 3rd level, when you cast mage hand, you can make the spectral hand invisible, and you can perform additional tasks with it.",
            levelRequired = 3,
            trigger = Trigger.OnTurnStart,
            effect = { /* Mage hand logic */ }
        ),
        ClassFeature(
            name = "Magical Ambush",
            description = "Starting at 9th level, if you are hidden from a creature when you cast a spell on it, the creature has disadvantage on any saving throw it makes against the spell this turn.",
            levelRequired = 9,
            trigger = Trigger.OnSpellCast,
            effect = { /* Save disadvantage logic */ }
        ),
        ClassFeature(
            name = "Versatile Trickster",
            description = "At 13th level, you gain the ability to distract targets with your mage hand. As a bonus action on your turn, you can designate a creature within 5 feet of the spectral hand created by the spell. Until the end of the turn, you have advantage on attack rolls against that creature.",
            levelRequired = 13,
            trigger = Trigger.OnTurnStart,
            effect = { /* Advantage logic */ }
        ),
        ClassFeature(
            name = "Spell Thief",
            description = "At 17th level, you gain the ability to magically steal the knowledge of how to cast a spell from another spellcaster.",
            levelRequired = 17,
            trigger = Trigger.OnTurnStart,
            effect = { /* Spell stealing logic */ }
        )
    )
}
