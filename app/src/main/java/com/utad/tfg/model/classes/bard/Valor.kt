package com.utad.tfg.model.classes.bard

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class Valor : Subclass {
    override val subclassName: String = "College of Valor"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Bonus Proficiencies",
            description = "When you join the College of Valor at 3rd level, you gain proficiency with medium armor, shields, and martial weapons.",
            levelRequired = 3,
            trigger = Trigger.OnTurnStart,
            effect = { /* Proficiency logic */ }
        ),
        ClassFeature(
            name = "Combat Inspiration",
            description = "Also at 3rd level, you learn to inspire others in battle. A creature that has a Bardic Inspiration die from you can roll that die and add the number rolled to a weapon damage roll it just made.",
            levelRequired = 3,
            trigger = Trigger.OnDamageCalculation,
            effect = { /* Damage boost logic */ }
        ),
        ClassFeature(
            name = "Extra Attack",
            description = "Starting at 6th level, you can attack twice, instead of once, whenever you take the Attack action on your turn.",
            levelRequired = 6,
            trigger = Trigger.OnTurnStart,
            effect = { /* Attack count logic */ }
        ),
        ClassFeature(
            name = "Battle Magic",
            description = "At 14th level, you have mastered the art of weaving spellcasting and weapon use into a single harmonious act. When you use your action to cast a bard spell, you can make one weapon attack as a bonus action.",
            levelRequired = 14,
            trigger = Trigger.OnSpellCast,
            effect = { /* Bonus attack logic */ }
        )
    )
}
