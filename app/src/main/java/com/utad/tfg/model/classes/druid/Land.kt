package com.utad.tfg.model.classes.druid

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class Land : Subclass {
    override val subclassName: String = "Circle of the Land"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Bonus Cantrip",
            description = "At 2nd level, you learn one additional druid cantrip of your choice.",
            levelRequired = 2,
            trigger = Trigger.OnTurnStart,
            effect = { /* Extra cantrip logic */ }
        ),
        ClassFeature(
            name = "Natural Recovery",
            description = "Starting at 2nd level, you can regain some of your magical energy by sitting in meditation and communing with nature.",
            levelRequired = 2,
            trigger = Trigger.OnTurnStart,
            effect = { /* Slot recovery logic */ }
        ),
        ClassFeature(
            name = "Land's Stride",
            description = "Starting at 6th level, moving through nonmagical difficult terrain costs you no extra movement.",
            levelRequired = 6,
            trigger = Trigger.OnTurnStart,
            effect = { /* Movement logic */ }
        ),
        ClassFeature(
            name = "Nature's Ward",
            description = "When you reach 10th level, you can't be charmed or frightened by elementals or fey, and you are immune to poison and disease.",
            levelRequired = 10,
            trigger = Trigger.OnTurnStart,
            effect = { /* Immunity logic */ }
        ),
        ClassFeature(
            name = "Nature's Sanctuary",
            description = "When you reach 14th level, creatures of the natural world can sense your connection to nature and become hesitant to attack you.",
            levelRequired = 14,
            trigger = Trigger.OnTurnStart,
            effect = { /* Sanctuary logic */ }
        )
    )
}
