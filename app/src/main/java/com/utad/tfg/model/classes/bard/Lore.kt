package com.utad.tfg.model.classes.bard

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class Lore : Subclass {
    override val subclassName: String = "College of Lore"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Bonus Proficiencies",
            description = "When you join the College of Lore at 3rd level, you gain proficiency with three skills of your choice.",
            levelRequired = 3,
            trigger = Trigger.OnTurnStart,
            effect = { /* Proficiency logic */ }
        ),
        ClassFeature(
            name = "Cutting Words",
            description = "Also at 3rd level, you learn how to use your wit to distract, confuse, and otherwise sap the confidence and competence of others.",
            levelRequired = 3,
            trigger = Trigger.OnAttackRoll,
            effect = { /* Penalty logic */ }
        ),
        ClassFeature(
            name = "Additional Magical Secrets",
            description = "At 6th level, you learn two spells of your choice from any class.",
            levelRequired = 6,
            trigger = Trigger.OnTurnStart,
            effect = { /* Extra spells logic */ }
        ),
        ClassFeature(
            name = "Peerless Skill",
            description = "Starting at 14th level, when you make an ability check, you can expend one use of Bardic Inspiration. Roll a Bardic Inspiration die and add the number rolled to your ability check.",
            levelRequired = 14,
            trigger = Trigger.OnTurnStart,
            effect = { /* Skill boost logic */ }
        )
    )
}
