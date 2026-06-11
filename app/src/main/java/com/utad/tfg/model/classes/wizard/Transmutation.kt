package com.utad.tfg.model.classes.wizard

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class Transmutation : Subclass {
    override val subclassName: String = "School of Transmutation"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Transmutation Savant",
            description = "The gold and time you must spend to copy a transmutation spell into your spellbook is halved.",
            levelRequired = 2,
            trigger = Trigger.OnTurnStart,
            effect = { /* Copying logic */ }
        ),
        ClassFeature(
            name = "Minor Alchemy",
            description = "Starting at 2nd level when you select this school, you can temporarily alter the physical properties of one nonmagical object.",
            levelRequired = 2,
            trigger = Trigger.OnTurnStart,
            effect = { /* Material change logic */ }
        ),
        ClassFeature(
            name = "Transmuter's Stone",
            description = "Starting at 6th level, you can spend 8 hours creating a transmuter's stone that stores transmutation magic.",
            levelRequired = 6,
            trigger = Trigger.OnTurnStart,
            effect = { /* Buff stone logic */ }
        ),
        ClassFeature(
            name = "Shapechanger",
            description = "At 10th level, you add the polymorph spell to your spellbook, if it is not there already. You can cast polymorph without expending a spell slot.",
            levelRequired = 10,
            trigger = Trigger.OnTurnStart,
            effect = { /* Free polymorph logic */ }
        ),
        ClassFeature(
            name = "Master Transmuter",
            description = "Starting at 14th level, you can use your action to consume the reserve of transmutation magic stored within your transmuter's stone in a single burst.",
            levelRequired = 14,
            trigger = Trigger.OnTurnStart,
            effect = { /* Major magic logic */ }
        )
    )
}
