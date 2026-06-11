package com.utad.tfg.model.classes.cleric

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class KnowledgeDomain : Subclass {
    override val subclassName: String = "Knowledge Domain"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Blessings of Knowledge",
            description = "At 1st level, you learn two languages of your choice. You also become proficient in your choice of two skills.",
            levelRequired = 1,
            trigger = Trigger.OnTurnStart,
            effect = { /* Skill/Language logic */ }
        ),
        ClassFeature(
            name = "Channel Divinity: Knowledge of the Ages",
            description = "Starting at 2nd level, you can use your Channel Divinity to tap into a divine well of knowledge. As an action, you choose one skill or tool and have proficiency with it for 10 minutes.",
            levelRequired = 2,
            trigger = Trigger.OnTurnStart,
            effect = { /* Temp proficiency logic */ }
        ),
        ClassFeature(
            name = "Channel Divinity: Read Thoughts",
            description = "At 6th level, you can use your Channel Divinity to read a creature's thoughts.",
            levelRequired = 6,
            trigger = Trigger.OnTurnStart,
            effect = { /* Mind reading logic */ }
        ),
        ClassFeature(
            name = "Potent Spellcasting",
            description = "Starting at 8th level, you add your Wisdom modifier to the damage you deal with any cleric cantrip.",
            levelRequired = 8,
            trigger = Trigger.OnDamageCalculation,
            effect = { /* Cantrip damage boost */ }
        ),
        ClassFeature(
            name = "Visions of the Past",
            description = "Starting at 17th level, you can call up visions of the past that relate to an object you hold or your immediate surroundings.",
            levelRequired = 17,
            trigger = Trigger.OnTurnStart,
            effect = { /* Divination logic */ }
        )
    )
}
