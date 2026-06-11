package com.utad.tfg.model.classes.wizard

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class Abjuration : Subclass {
    override val subclassName: String = "School of Abjuration"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Abjuration Savant",
            description = "The gold and time you must spend to copy an abjuration spell into your spellbook is halved.",
            levelRequired = 2,
            trigger = Trigger.OnTurnStart,
            effect = { /* Copying logic */ }
        ),
        ClassFeature(
            name = "Arcane Ward",
            description = "Starting at 2nd level, you can weave magic around yourself for protection. When you cast an abjuration spell of 1st level or higher, you can simultaneously use a strand of the spell's magic to create a magical ward on yourself.",
            levelRequired = 2,
            trigger = Trigger.OnSpellCast,
            effect = { /* Ward HP logic */ }
        ),
        ClassFeature(
            name = "Projected Ward",
            description = "Starting at 6th level, when a creature that you can see within 30 feet of you takes damage, you can use your reaction to cause your Arcane Ward to absorb that damage.",
            levelRequired = 6,
            trigger = Trigger.OnTakeDamage,
            effect = { /* Protect ally logic */ }
        ),
        ClassFeature(
            name = "Improved Abjuration",
            description = "Beginning at 10th level, when you cast an abjuration spell that requires you to make an ability check as a part of casting that spell (as in counterspell and dispel magic), you add your proficiency bonus to that ability check.",
            levelRequired = 10,
            trigger = Trigger.OnTurnStart,
            effect = { /* Counterspell bonus logic */ }
        ),
        ClassFeature(
            name = "Spell Resistance",
            description = "Starting at 14th level, you have advantage on saving throws against spells. Furthermore, you have resistance against the damage of spells.",
            levelRequired = 14,
            trigger = Trigger.OnTakeDamage,
            effect = { /* Resistance logic */ }
        )
    )
}
