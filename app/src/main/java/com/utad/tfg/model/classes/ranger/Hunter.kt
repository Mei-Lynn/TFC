package com.utad.tfg.model.classes.ranger

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class Hunter : Subclass {
    override val subclassName: String = "Hunter"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Hunter's Prey",
            description = "At 3rd level, you gain one of the following features of your choice: Colossus Slayer, Giant Killer, or Horde Breaker.",
            levelRequired = 3,
            trigger = Trigger.OnDamageCalculation,
            effect = { /* Extra damage logic */ }
        ),
        ClassFeature(
            name = "Defensive Tactics",
            description = "At 7th level, you gain one of the following features of your choice: Escape the Horde, Multiattack Defense, or Steel Will.",
            levelRequired = 7,
            trigger = Trigger.OnTakeDamage,
            effect = { /* AC/Save bonus logic */ }
        ),
        ClassFeature(
            name = "Multiattack",
            description = "At 11th level, you gain one of the following features of your choice: Volley or Whirlwind Attack.",
            levelRequired = 11,
            trigger = Trigger.OnTurnStart,
            effect = { /* AoE attack logic */ }
        ),
        ClassFeature(
            name = "Superior Hunter's Defense",
            description = "At 15th level, you gain one of the following features of your choice: Evasion, Stand Against the Tide, or Uncanny Dodge.",
            levelRequired = 15,
            trigger = Trigger.OnTakeDamage,
            effect = { /* Damage reduction logic */ }
        )
    )
}
