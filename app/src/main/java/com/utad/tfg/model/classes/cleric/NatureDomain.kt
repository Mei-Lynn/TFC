package com.utad.tfg.model.classes.cleric

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class NatureDomain : Subclass {
    override val subclassName: String = "Nature Domain"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Acolyte of Nature",
            description = "At 1st level, you learn one druid cantrip of your choice. You also gain proficiency in one skill of your choice from Animal Handling, Nature, or Survival.",
            levelRequired = 1,
            trigger = Trigger.OnTurnStart,
            effect = { /* Cantrip/Skill logic */ }
        ),
        ClassFeature(
            name = "Channel Divinity: Charm Animals and Plants",
            description = "Starting at 2nd level, you can use your Channel Divinity to charm animals and plants.",
            levelRequired = 2,
            trigger = Trigger.OnTurnStart,
            effect = { /* Charm logic */ }
        ),
        ClassFeature(
            name = "Dampen Elements",
            description = "Starting at 6th level, when you or a creature within 30 feet of you takes acid, cold, fire, lightning, or thunder damage, you can use your reaction to grant resistance to the creature against that instance of the damage.",
            levelRequired = 6,
            trigger = Trigger.OnTakeDamage,
            effect = { /* Resistance logic */ }
        ),
        ClassFeature(
            name = "Divine Strike",
            description = "At 8th level, you gain the ability to infuse your weapon strikes with divine energy.",
            levelRequired = 8,
            trigger = Trigger.OnDamageCalculation,
            effect = { /* Elemental damage boost */ }
        ),
        ClassFeature(
            name = "Master of Nature",
            description = "At 17th level, you gain the ability to command animals and plants. While creatures are charmed by your Charm Animals and Plants feature, you can take a bonus action on your turn to give them verbal commands.",
            levelRequired = 17,
            trigger = Trigger.OnTurnStart,
            effect = { /* Command logic */ }
        )
    )
}
