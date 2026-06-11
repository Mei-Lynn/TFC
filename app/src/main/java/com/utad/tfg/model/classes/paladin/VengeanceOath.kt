package com.utad.tfg.model.classes.paladin

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class VengeanceOath : Subclass {
    override val subclassName: String = "Oath of Vengeance"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Channel Divinity: Vow of Enmity",
            description = "As a bonus action, you can utter a vow of enmity against a creature you can see within 10 feet of you, using your Channel Divinity. You gain advantage on attack rolls against the creature for 1 minute.",
            levelRequired = 3,
            trigger = Trigger.OnTurnStart,
            effect = { /* Advantage logic */ }
        ),
        ClassFeature(
            name = "Relentless Avenger",
            description = "By 7th level, your supernatural focus helps you close off a foe's retreat. When you hit a creature with an opportunity attack, you can move up to half your speed as part of the same reaction.",
            levelRequired = 7,
            trigger = Trigger.OnAttackRoll,
            effect = { /* Movement logic */ }
        ),
        ClassFeature(
            name = "Soul of Vengeance",
            description = "Starting at 15th level, the authority with which you speak your Vow of Enmity gives you greater power over your foe. When a creature under the effect of your Vow of Enmity makes an attack, you can use your reaction to make a melee weapon attack against that creature.",
            levelRequired = 15,
            trigger = Trigger.OnTurnStart,
            effect = { /* Reaction attack logic */ }
        ),
        ClassFeature(
            name = "Avenging Angel",
            description = "At 20th level, you can assume the form of an angelic avenger.",
            levelRequired = 20,
            trigger = Trigger.OnTurnStart,
            effect = { /* Transformation logic */ }
        )
    )
}
