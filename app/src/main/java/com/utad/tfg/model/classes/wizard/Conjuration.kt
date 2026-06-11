package com.utad.tfg.model.classes.wizard

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class Conjuration : Subclass {
    override val subclassName: String = "School of Conjuration"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Conjuration Savant",
            description = "The gold and time you must spend to copy a conjuration spell into your spellbook is halved.",
            levelRequired = 2,
            trigger = Trigger.OnTurnStart,
            effect = { /* Copying logic */ }
        ),
        ClassFeature(
            name = "Minor Conjuration",
            description = "Starting at 2nd level when you select this school, you can use your action to conjure up an inanimate object in your hand or on the ground in an unoccupied space that you can see within 10 feet of you.",
            levelRequired = 2,
            trigger = Trigger.OnTurnStart,
            effect = { /* Object creation logic */ }
        ),
        ClassFeature(
            name = "Benign Transposition",
            description = "Starting at 6th level, you can use your action to teleport up to 30 feet to an unoccupied space that you can see. Alternatively, you can choose a space within range that is occupied by a Small or Medium creature. If that creature is willing, you both teleport, swapping places.",
            levelRequired = 6,
            trigger = Trigger.OnTurnStart,
            effect = { /* Teleport logic */ }
        ),
        ClassFeature(
            name = "Focused Conjuration",
            description = "Beginning at 10th level, while you are concentrating on a conjuration spell, your concentration can't be broken as a result of taking damage.",
            levelRequired = 10,
            trigger = Trigger.OnTakeDamage,
            effect = { /* Concentration logic */ }
        ),
        ClassFeature(
            name = "Durable Summons",
            description = "Starting at 14th level, any creature that you summon or create with a conjuration spell has 30 temporary hit points.",
            levelRequired = 14,
            trigger = Trigger.OnSpellCast,
            effect = { /* Summon buff logic */ }
        )
    )
}
