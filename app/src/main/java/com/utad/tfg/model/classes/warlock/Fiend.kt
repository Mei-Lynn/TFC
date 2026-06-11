package com.utad.tfg.model.classes.warlock

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class Fiend : Subclass {
    override val subclassName: String = "The Fiend"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Dark One's Blessing",
            description = "Starting at 1st level, when you reduce a hostile creature to 0 hit points, you gain temporary hit points equal to your Charisma modifier + your warlock level (minimum of 1).",
            levelRequired = 1,
            trigger = Trigger.OnTurnStart, // In a real sim, this would trigger on kill
            effect = { /* Temp HP logic */ }
        ),
        ClassFeature(
            name = "Dark One's Own Luck",
            description = "Starting at 6th level, you can call on your patron to alter fate in your favor. When you make an ability check or a saving throw, you can add a d10 to the roll.",
            levelRequired = 6,
            trigger = Trigger.OnSaveThrow,
            effect = { /* Roll bonus logic */ }
        ),
        ClassFeature(
            name = "Fiendish Resilience",
            description = "Starting at 10th level, you can choose one damage type when you finish a short or long rest. You gain resistance to that damage type until you choose a different one with this feature.",
            levelRequired = 10,
            trigger = Trigger.OnTakeDamage,
            effect = { /* Resistance logic */ }
        ),
        ClassFeature(
            name = "Hurl Through Hell",
            description = "Starting at 14th level, when you hit a creature with an attack, you can use this feature to instantly transport the target through the lower planes.",
            levelRequired = 14,
            trigger = Trigger.OnAttackRoll,
            effect = { /* Massive damage logic */ }
        )
    )
}
