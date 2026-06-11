package com.utad.tfg.model.classes.cleric

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class LightDomain : Subclass {
    override val subclassName: String = "Light Domain"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Warding Flare",
            description = "Also at 1st level, you can interpose divine light between yourself and an attacking enemy.",
            levelRequired = 1,
            trigger = Trigger.OnAttackRoll,
            effect = { /* Disadvantage logic */ }
        ),
        ClassFeature(
            name = "Channel Divinity: Radiance of the Dawn",
            description = "Starting at 2nd level, you can use your Channel Divinity to harness sunlight, banishing darkness and dealing radiant damage to your foes.",
            levelRequired = 2,
            trigger = Trigger.OnTurnStart,
            effect = { /* AoE Radiant damage */ }
        ),
        ClassFeature(
            name = "Improved Flare",
            description = "Starting at 6th level, you can also use your Warding Flare when a creature that you can see within 30 feet of you attacks a creature other than you.",
            levelRequired = 6,
            trigger = Trigger.OnAttackRoll,
            effect = { /* Protect ally logic */ }
        ),
        ClassFeature(
            name = "Potent Spellcasting",
            description = "Starting at 8th level, you add your Wisdom modifier to the damage you deal with any cleric cantrip.",
            levelRequired = 8,
            trigger = Trigger.OnDamageCalculation,
            effect = { /* Cantrip damage boost */ }
        ),
        ClassFeature(
            name = "Corona of Light",
            description = "Starting at 17th level, you can use your action to activate an aura of sunlight that lasts for 1 minute or until you dismiss it using a bonus action.",
            levelRequired = 17,
            trigger = Trigger.OnTurnStart,
            effect = { /* Sunlight aura logic */ }
        )
    )
}
