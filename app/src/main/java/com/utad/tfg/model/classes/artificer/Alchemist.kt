package com.utad.tfg.model.classes.artificer

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class Alchemist : Subclass {
    override val subclassName: String = "Alchemist"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Experimental Elixir",
            description = "Whenever you finish a long rest, you can magically produce an experimental elixir in an empty flask you are touching.",
            levelRequired = 3,
            trigger = Trigger.OnTurnStart,
            effect = { /* Random buff logic */ }
        ),
        ClassFeature(
            name = "Alchemical Savant",
            description = "At 5th level, you develop masterful command of magical chemicals, enhancing the healing and damage you create through them.",
            levelRequired = 5,
            trigger = Trigger.OnSpellCast,
            effect = { /* Spell boost logic */ }
        ),
        ClassFeature(
            name = "Restorative Greater Elixirs",
            description = "At 9th level, your experimental elixirs become even more potent. When a creature drinks an elixir you produced, it gains temporary hit points equal to 2d6 + your Intelligence modifier.",
            levelRequired = 9,
            trigger = Trigger.OnTurnStart,
            effect = { /* Temp HP logic */ }
        ),
        ClassFeature(
            name = "Chemical Mastery",
            description = "By 15th level, you have been exposed to so many chemicals that they pose little risk to you, and you can use them to quickly end certain ailments.",
            levelRequired = 15,
            trigger = Trigger.OnTurnStart,
            effect = { /* Resistance/Heal logic */ }
        )
    )
}
