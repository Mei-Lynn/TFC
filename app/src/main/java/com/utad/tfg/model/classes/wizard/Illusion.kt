package com.utad.tfg.model.classes.wizard

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class Illusion : Subclass {
    override val subclassName: String = "School of Illusion"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Illusion Savant",
            description = "The gold and time you must spend to copy an illusion spell into your spellbook is halved.",
            levelRequired = 2,
            trigger = Trigger.OnTurnStart,
            effect = { /* Copying logic */ }
        ),
        ClassFeature(
            name = "Improved Minor Illusion",
            description = "When you choose this school at 2nd level, you learn the minor illusion cantrip. If you already know this cantrip, you learn a different wizard cantrip of your choice.",
            levelRequired = 2,
            trigger = Trigger.OnTurnStart,
            effect = { /* Cantrip buff logic */ }
        ),
        ClassFeature(
            name = "Malleable Illusions",
            description = "Starting at 6th level, when you cast an illusion spell that has a duration of 1 minute or longer, you can use your action to change the nature of that illusion.",
            levelRequired = 6,
            trigger = Trigger.OnTurnStart,
            effect = { /* Illusion change logic */ }
        ),
        ClassFeature(
            name = "Illusory Self",
            description = "Beginning at 10th level, you can create an illusory duplicate of yourself as an instant, almost instinctive reaction to danger.",
            levelRequired = 10,
            trigger = Trigger.OnAttackRoll,
            effect = { /* Miss attack logic */ }
        ),
        ClassFeature(
            name = "Illusory Reality",
            description = "By 14th level, you have learned the secret of weaving shadow magic into your illusions to give them a semitangible substance.",
            levelRequired = 14,
            trigger = Trigger.OnTurnStart,
            effect = { /* Real illusion logic */ }
        )
    )
}
