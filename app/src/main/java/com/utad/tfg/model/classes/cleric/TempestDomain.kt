package com.utad.tfg.model.classes.cleric

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class TempestDomain : Subclass {
    override val subclassName: String = "Tempest Domain"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Wrath of the Storm",
            description = "Also at 1st level, you can thunderously rebuke attackers. When a creature within 5 feet of you that you can see hits you with an attack, you can use your reaction to cause the creature to make a Dexterity saving throw.",
            levelRequired = 1,
            trigger = Trigger.OnTakeDamage,
            effect = { /* Lightning/Thunder reaction damage */ }
        ),
        ClassFeature(
            name = "Channel Divinity: Destructive Wrath",
            description = "Starting at 2nd level, you can use your Channel Divinity to wield the power of the storm with unchecked ferocity. When you roll lightning or thunder damage, you can use your Channel Divinity to deal maximum damage.",
            levelRequired = 2,
            trigger = Trigger.OnDamageCalculation,
            effect = { /* Maximize damage logic */ }
        ),
        ClassFeature(
            name = "Thunderous Strike",
            description = "At 6th level, when you deal lightning damage to a Large or smaller creature, you can also push it up to 10 feet away from you.",
            levelRequired = 6,
            trigger = Trigger.OnDamageCalculation,
            effect = { /* Push logic */ }
        ),
        ClassFeature(
            name = "Divine Strike",
            description = "At 8th level, you gain the ability to infuse your weapon strikes with divine energy.",
            levelRequired = 8,
            trigger = Trigger.OnDamageCalculation,
            effect = { /* Extra thunder damage */ }
        ),
        ClassFeature(
            name = "Stormborn",
            description = "At 17th level, you have a flying speed equal to your current walking speed whenever you are not underground or indoors.",
            levelRequired = 17,
            trigger = Trigger.OnTurnStart,
            effect = { /* Flying logic */ }
        )
    )
}
