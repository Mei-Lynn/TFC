package com.utad.tfg.model.classes.monk

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class Shadow : Subclass {
    override val subclassName: String = "Way of Shadow"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Shadow Arts",
            description = "Starting when you choose this tradition at 3rd level, you can use your ki to duplicate the effects of certain spells.",
            levelRequired = 3,
            trigger = Trigger.OnTurnStart,
            effect = { /* Ki spells logic */ }
        ),
        ClassFeature(
            name = "Shadow Step",
            description = "At 6th level, you gain the ability to step from one shadow into another.",
            levelRequired = 6,
            trigger = Trigger.OnTurnStart,
            effect = { /* Teleport logic */ }
        ),
        ClassFeature(
            name = "Cloak of Shadows",
            description = "By 11th level, you have learned to become one with the shadows. When you are in an area of dim light or darkness, you can use your action to become invisible.",
            levelRequired = 11,
            trigger = Trigger.OnTurnStart,
            effect = { /* Invisibility logic */ }
        ),
        ClassFeature(
            name = "Opportunist",
            description = "At 17th level, you can exploit a creature's momentary distraction when it is hit by an attack.",
            levelRequired = 17,
            trigger = Trigger.OnTurnStart,
            effect = { /* Reaction attack logic */ }
        )
    )
}
