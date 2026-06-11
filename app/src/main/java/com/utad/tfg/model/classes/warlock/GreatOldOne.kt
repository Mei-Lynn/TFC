package com.utad.tfg.model.classes.warlock

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class GreatOldOne : Subclass {
    override val subclassName: String = "The Great Old One"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Awakened Mind",
            description = "Starting at 1st level, your alien knowledge gives you the ability to touch the minds of other creatures. You can communicate telepathically with any creature you can see within 30 feet of you.",
            levelRequired = 1,
            trigger = Trigger.OnTurnStart,
            effect = { /* Telepathy logic */ }
        ),
        ClassFeature(
            name = "Entropic Ward",
            description = "At 6th level, you learn to magically ward yourself against attack and turn an enemy's failed strike into good fortune for yourself. When a creature makes an attack roll against you, you can use your reaction to impose disadvantage on that roll.",
            levelRequired = 6,
            trigger = Trigger.OnAttackRoll,
            effect = { /* Disadvantage logic */ }
        ),
        ClassFeature(
            name = "Thought Shield",
            description = "Starting at 10th level, your thoughts can't be read by telepathy or other means unless you allow it. You also have resistance to psychic damage.",
            levelRequired = 10,
            trigger = Trigger.OnTakeDamage,
            effect = { /* Resistance logic */ }
        ),
        ClassFeature(
            name = "Create Thrall",
            description = "At 14th level, you gain the ability to infect a humanoid's mind with the alien magic of your patron. You can use your action to touch an incapacitated humanoid. That creature is then charmed by you until a remove curse spell is cast on it.",
            levelRequired = 14,
            trigger = Trigger.OnTurnStart,
            effect = { /* Charm logic */ }
        )
    )
}
