package com.utad.tfg.model.classes.cleric

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class WarDomain : Subclass {
    override val subclassName: String = "War Domain"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "War Priest",
            description = "From 1st level, your god delivers glimpses of inspiration to you while you are engaged in battle. When you use the Attack action, you can make one weapon attack as a bonus action.",
            levelRequired = 1,
            trigger = Trigger.OnTurnStart,
            effect = { /* Bonus attack logic */ }
        ),
        ClassFeature(
            name = "Channel Divinity: Guided Strike",
            description = "Starting at 2nd level, you can use your Channel Divinity to strike with supernatural accuracy. When you make an attack roll, you can use your Channel Divinity to gain a +10 bonus to the roll.",
            levelRequired = 2,
            trigger = Trigger.OnAttackRoll,
            effect = { /* Attack bonus logic */ }
        ),
        ClassFeature(
            name = "Channel Divinity: War God's Blessing",
            description = "At 6th level, when a creature within 30 feet of you makes an attack roll, you can use your reaction to grant that creature a +10 bonus to the roll, using your Channel Divinity.",
            levelRequired = 6,
            trigger = Trigger.OnAttackRoll,
            effect = { /* Ally attack bonus logic */ }
        ),
        ClassFeature(
            name = "Divine Strike",
            description = "At 8th level, you gain the ability to infuse your weapon strikes with divine energy.",
            levelRequired = 8,
            trigger = Trigger.OnDamageCalculation,
            effect = { /* Extra weapon damage */ }
        ),
        ClassFeature(
            name = "Avatar of Battle",
            description = "At 17th level, you gain resistance to bludgeoning, piercing, and slashing damage from nonmagical weapons.",
            levelRequired = 17,
            trigger = Trigger.OnTakeDamage,
            effect = { /* Resistance logic */ }
        )
    )
}
