package com.utad.tfg.model.classes.wizard

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class Evocation : Subclass {
    override val subclassName: String = "School of Evocation"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Evocation Savant",
            description = "The gold and time you must spend to copy an evocation spell into your spellbook is halved.",
            levelRequired = 2,
            trigger = Trigger.OnTurnStart,
            effect = { /* Logic for copying spells would go here */ }
        ),
        ClassFeature(
            name = "Sculpt Spells",
            description = "You can create pockets of relative safety within the effects of your evocation spells.",
            levelRequired = 2,
            trigger = Trigger.OnSpellCast,
            effect = { /* Logic to exclude allies from AOE damage */ }
        ),
        ClassFeature(
            name = "Potent Cantrip",
            description = "Your damaging cantrips affect even creatures that avoid the full brunt of the effect.",
            levelRequired = 6,
            trigger = Trigger.OnDamageCalculation,
            effect = { /* Logic for half damage on save */ }
        ),
        ClassFeature(
            name = "Empowered Evocation",
            description = "You can add your Intelligence modifier to one damage roll of any wizard evocation spell you cast.",
            levelRequired = 10,
            trigger = Trigger.OnDamageCalculation,
            effect = { /* Logic to add INT mod to damage */ }
        ),
        ClassFeature(
            name = "Overchannel",
            description = "You can increase the power of your simpler spells. When you cast a wizard spell of 1st through 5th level that deals damage, you can deal maximum damage with that spell.",
            levelRequired = 14,
            trigger = Trigger.OnDamageCalculation,
            effect = { /* Logic to maximize damage dice */ }
        )
    )
}
