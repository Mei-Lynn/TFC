package com.utad.tfg.model.classes.wizard

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class Divination : Subclass {
    override val subclassName: String = "School of Divination"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Divination Savant",
            description = "The gold and time you must spend to copy a divination spell into your spellbook is halved.",
            levelRequired = 2,
            trigger = Trigger.OnTurnStart,
            effect = { /* Copying logic */ }
        ),
        ClassFeature(
            name = "Portent",
            description = "Starting at 2nd level when you choose this school, glimpses of the future begin to press in on your awareness. When you finish a long rest, roll two d20s and record the numbers rolled.",
            levelRequired = 2,
            trigger = Trigger.OnTurnStart,
            effect = { /* Dice storage logic */ }
        ),
        ClassFeature(
            name = "Expert Divination",
            description = "Beginning at 6th level, casting divination spells comes so easily to you that it expends only a fraction of your spellcasting efforts. When you cast a divination spell of 2nd level or higher using a spell slot, you regain one expended spell slot.",
            levelRequired = 6,
            trigger = Trigger.OnSpellCast,
            effect = { /* Slot recovery logic */ }
        ),
        ClassFeature(
            name = "The Third Eye",
            description = "Starting at 10th level, you can use your action to increase your powers of perception.",
            levelRequired = 10,
            trigger = Trigger.OnTurnStart,
            effect = { /* Vision logic */ }
        ),
        ClassFeature(
            name = "Greater Portent",
            description = "Starting at 14th level, the visions in your dreams intensify and paint a more complete picture of what is to come. You roll three d20s for your Portent feature, rather than two.",
            levelRequired = 14,
            trigger = Trigger.OnTurnStart,
            effect = { /* Triple dice logic */ }
        )
    )
}
