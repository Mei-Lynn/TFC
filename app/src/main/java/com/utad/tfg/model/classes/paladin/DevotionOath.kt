package com.utad.tfg.model.classes.paladin

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class DevotionOath : Subclass {
    override val subclassName: String = "Oath of Devotion"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Channel Divinity: Sacred Weapon",
            description = "As an action, you can imbue one weapon that you are holding with positive energy, using your Channel Divinity.",
            levelRequired = 3,
            trigger = Trigger.OnTurnStart,
            effect = { /* Attack bonus logic */ }
        ),
        ClassFeature(
            name = "Aura of Devotion",
            description = "Starting at 7th level, you and friendly creatures within 10 feet of you can't be charmed while you are conscious.",
            levelRequired = 7,
            trigger = Trigger.OnTurnStart,
            effect = { /* Charm immunity logic */ }
        ),
        ClassFeature(
            name = "Purity of Spirit",
            description = "Beginning at 15th level, you are always under the effects of a protection from evil and good spell.",
            levelRequired = 15,
            trigger = Trigger.OnTurnStart,
            effect = { /* Protection logic */ }
        ),
        ClassFeature(
            name = "Holy Nimbus",
            description = "At 20th level, as an action, you can emanate an aura of sunlight.",
            levelRequired = 20,
            trigger = Trigger.OnTurnStart,
            effect = { /* Radiant aura logic */ }
        )
    )
}
