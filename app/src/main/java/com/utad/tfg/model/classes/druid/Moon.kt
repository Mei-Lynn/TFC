package com.utad.tfg.model.classes.druid

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class Moon : Subclass {
    override val subclassName: String = "Circle of the Moon"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Combat Wild Shape",
            description = "When you choose this circle at 2nd level, you gain the ability to use Wild Shape on your turn as a bonus action, rather than as an action.",
            levelRequired = 2,
            trigger = Trigger.OnTurnStart,
            effect = { /* Bonus action wild shape logic */ }
        ),
        ClassFeature(
            name = "Circle Forms",
            description = "The rites of your circle grant you the ability to transform into more dangerous animal forms.",
            levelRequired = 2,
            trigger = Trigger.OnTurnStart,
            effect = { /* Higher CR beasts logic */ }
        ),
        ClassFeature(
            name = "Primal Strike",
            description = "Starting at 6th level, your attacks in beast form count as magical for the purpose of overcoming resistance and immunity to nonmagical attacks and damage.",
            levelRequired = 6,
            trigger = Trigger.OnAttackRoll,
            effect = { /* Magical damage logic */ }
        ),
        ClassFeature(
            name = "Elemental Wild Shape",
            description = "At 10th level, you can expend two uses of Wild Shape at the same time to transform into an air elemental, an earth elemental, a fire elemental, or a water elemental.",
            levelRequired = 10,
            trigger = Trigger.OnTurnStart,
            effect = { /* Elemental forms logic */ }
        ),
        ClassFeature(
            name = "Thousand Forms",
            description = "By 14th level, you have learned to use magic to alter your physical form in more subtle ways. You can cast the alter self spell at will.",
            levelRequired = 14,
            trigger = Trigger.OnTurnStart,
            effect = { /* Alter self logic */ }
        )
    )
}
