package com.utad.tfg.model.classes.wizard

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class Necromancy : Subclass {
    override val subclassName: String = "School of Necromancy"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Necromancy Savant",
            description = "The gold and time you must spend to copy a necromancy spell into your spellbook is halved.",
            levelRequired = 2,
            trigger = Trigger.OnTurnStart,
            effect = { /* Copying logic */ }
        ),
        ClassFeature(
            name = "Grim Harvest",
            description = "At 2nd level, you gain the ability to reap life energy from creatures you kill with your spells.",
            levelRequired = 2,
            trigger = Trigger.OnTurnStart, // In sim: on kill
            effect = { /* Healing logic */ }
        ),
        ClassFeature(
            name = "Undead Thralls",
            description = "At 6th level, you add the animate dead spell to your spellbook if it, is not there already. When you cast animate dead, you can target one additional corpse or pile of bones, creating another zombie or skeleton, as appropriate.",
            levelRequired = 6,
            trigger = Trigger.OnSpellCast,
            effect = { /* Summon buff logic */ }
        ),
        ClassFeature(
            name = "Inured to Undead",
            description = "Beginning at 10th level, you have resistance to necrotic damage, and your hit point maximum can't be reduced.",
            levelRequired = 10,
            trigger = Trigger.OnTakeDamage,
            effect = { /* Resistance logic */ }
        ),
        ClassFeature(
            name = "Command Undead",
            description = "Starting at 14th level, you can use magic to bring undead into your control, even those created by other wizards.",
            levelRequired = 14,
            trigger = Trigger.OnTurnStart,
            effect = { /* Control logic */ }
        )
    )
}
