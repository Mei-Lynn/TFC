package com.utad.tfg.model.classes.sorcerer

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class WildMagic : Subclass {
    override val subclassName: String = "Wild Magic"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Wild Magic Surge",
            description = "Starting when you choose this origin at 1st level, your spellcasting can unleash surges of untamed magic.",
            levelRequired = 1,
            trigger = Trigger.OnSpellCast,
            effect = { /* Random effect logic */ }
        ),
        ClassFeature(
            name = "Tides of Chaos",
            description = "Starting at 1st level, you can manipulate the forces of chance and chaos to gain advantage on one attack roll, ability check, or saving throw.",
            levelRequired = 1,
            trigger = Trigger.OnTurnStart,
            effect = { /* Advantage logic */ }
        ),
        ClassFeature(
            name = "Bend Luck",
            description = "Starting at 6th level, you have the ability to twist fate using your wild magic. When another creature you can see makes an attack roll, an ability check, or a saving throw, you can use your reaction and spend 2 sorcery points to roll 1d4 and apply the number rolled as a bonus or penalty to the creature's roll.",
            levelRequired = 6,
            trigger = Trigger.OnTurnStart,
            effect = { /* Luck manipulation logic */ }
        ),
        ClassFeature(
            name = "Controlled Chaos",
            description = "At 14th level, you gain a modicum of control over the surges of your wild magic. Whenever you roll on the Wild Magic Surge table, you can roll twice and use either number.",
            levelRequired = 14,
            trigger = Trigger.OnSpellCast,
            effect = { /* Surge control logic */ }
        ),
        ClassFeature(
            name = "Spell Bombardment",
            description = "Beginning at 18th level, the harmful energy of your spells intensifies. When you roll damage for a spell and roll the highest number possible on any of the dice, choose one of those dice, roll it again and add that roll to the damage.",
            levelRequired = 18,
            trigger = Trigger.OnDamageCalculation,
            effect = { /* Damage exploding logic */ }
        )
    )
}
