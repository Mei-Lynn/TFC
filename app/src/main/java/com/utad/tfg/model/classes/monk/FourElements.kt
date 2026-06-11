package com.utad.tfg.model.classes.monk

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class FourElements : Subclass {
    override val subclassName: String = "Way of the Four Elements"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Disciple of the Elements",
            description = "When you choose this tradition at 3rd level, you learn magical disciplines that harness the power of the four elements.",
            levelRequired = 3,
            trigger = Trigger.OnTurnStart,
            effect = { /* Elemental disciplines logic */ }
        ),
        ClassFeature(
            name = "Elemental Disciplines",
            description = "You can spend ki points to cast certain spells or activate elemental effects.",
            levelRequired = 3,
            trigger = Trigger.OnSpellCast,
            effect = { /* Ki spending logic */ }
        )
    )
}
