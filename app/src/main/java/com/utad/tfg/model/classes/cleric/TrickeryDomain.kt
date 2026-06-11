package com.utad.tfg.model.classes.cleric

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class TrickeryDomain : Subclass {
    override val subclassName: String = "Trickery Domain"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Blessing of the Trickster",
            description = "Starting when you choose this domain at 1st level, you can use your action to touch a willing creature other than yourself to give it advantage on Dexterity (Stealth) checks.",
            levelRequired = 1,
            trigger = Trigger.OnTurnStart,
            effect = { /* Stealth advantage logic */ }
        ),
        ClassFeature(
            name = "Channel Divinity: Invoke Duplicity",
            description = "Starting at 2nd level, you can use your Channel Divinity to create an illusory duplicate of yourself.",
            levelRequired = 2,
            trigger = Trigger.OnTurnStart,
            effect = { /* Illusion logic */ }
        ),
        ClassFeature(
            name = "Channel Divinity: Cloak of Shadows",
            description = "Starting at 6th level, you can use your Channel Divinity to vanish. As an action, you become invisible until the end of your next turn.",
            levelRequired = 6,
            trigger = Trigger.OnTurnStart,
            effect = { /* Invisibility logic */ }
        ),
        ClassFeature(
            name = "Divine Strike",
            description = "At 8th level, you gain the ability to infuse your weapon strikes with divine energy.",
            levelRequired = 8,
            trigger = Trigger.OnDamageCalculation,
            effect = { /* Extra poison damage */ }
        ),
        ClassFeature(
            name = "Improved Duplicity",
            description = "At 17th level, you can create up to four duplicates of yourself, instead of one, when you use Invoke Duplicity.",
            levelRequired = 17,
            trigger = Trigger.OnTurnStart,
            effect = { /* More illusions logic */ }
        )
    )
}
