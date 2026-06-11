package com.utad.tfg.model.classes.paladin

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class AncientsOath : Subclass {
    override val subclassName: String = "Oath of the Ancients"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Channel Divinity: Nature's Wrath",
            description = "You can use your Channel Divinity to invoke primeval forces to ensnare a foe.",
            levelRequired = 3,
            trigger = Trigger.OnTurnStart,
            effect = { /* Restrain logic */ }
        ),
        ClassFeature(
            name = "Aura of Warding",
            description = "Beginning at 7th level, ancient magic lies so heavily upon you that it forms an eldritch ward. You and friendly creatures within 10 feet of you have resistance to damage from spells.",
            levelRequired = 7,
            trigger = Trigger.OnTakeDamage,
            effect = { /* Spell resistance logic */ }
        ),
        ClassFeature(
            name = "Undying Sentinel",
            description = "Starting at 15th level, when you are reduced to 0 hit points and are not killed outright, you can choose to drop to 1 hit point instead.",
            levelRequired = 15,
            trigger = Trigger.OnTakeDamage,
            effect = { /* Death save logic */ }
        ),
        ClassFeature(
            name = "Elder Champion",
            description = "At 20th level, you can assume the form of an ancient force of nature.",
            levelRequired = 20,
            trigger = Trigger.OnTurnStart,
            effect = { /* Transformation logic */ }
        )
    )
}
