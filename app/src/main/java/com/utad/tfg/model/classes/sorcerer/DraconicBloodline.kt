package com.utad.tfg.model.classes.sorcerer

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class DraconicBloodline : Subclass {
    override val subclassName: String = "Draconic Bloodline"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Draconic Resilience",
            description = "As magic flows through your body, it causes physical traits of your dragon ancestors to emerge. At 1st level, your hit point maximum increases by 1 and increases by 1 again whenever you gain a level in this class.",
            levelRequired = 1,
            trigger = Trigger.OnTurnStart,
            effect = { /* HP and AC boost logic */ }
        ),
        ClassFeature(
            name = "Elemental Affinity",
            description = "Starting at 6th level, when you cast a spell that deals damage of the type associated with your draconic ancestry, you can add your Charisma modifier to one damage roll of that spell.",
            levelRequired = 6,
            trigger = Trigger.OnDamageCalculation,
            effect = { /* Damage boost logic */ }
        ),
        ClassFeature(
            name = "Dragon Wings",
            description = "At 14th level, you gain the ability to sprout a pair of dragon wings from your back, gaining a flying speed equal to your current speed.",
            levelRequired = 14,
            trigger = Trigger.OnTurnStart,
            effect = { /* Flying logic */ }
        ),
        ClassFeature(
            name = "Draconic Presence",
            description = "Beginning at 18th level, you can channel the dread presence of your dragon ancestor, causing creatures around you to become awestruck or frightened.",
            levelRequired = 18,
            trigger = Trigger.OnTurnStart,
            effect = { /* Aura logic */ }
        )
    )
}
