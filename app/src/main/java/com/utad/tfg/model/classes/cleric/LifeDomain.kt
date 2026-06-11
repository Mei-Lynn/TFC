package com.utad.tfg.model.classes.cleric

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class LifeDomain : Subclass {
    override val subclassName: String = "Life Domain"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Disciple of Life",
            description = "Also starting at 1st level, your healing spells are more effective.",
            levelRequired = 1,
            trigger = Trigger.OnSpellCast,
            effect = { /* Healing boost logic */ }
        ),
        ClassFeature(
            name = "Channel Divinity: Preserve Life",
            description = "Starting at 2nd level, you can use your Channel Divinity to heal the badly injured.",
            levelRequired = 2,
            trigger = Trigger.OnTurnStart,
            effect = { /* AoE Heal logic */ }
        ),
        ClassFeature(
            name = "Blessed Healer",
            description = "Beginning at 6th level, the healing spells you cast on others heal you as well.",
            levelRequired = 6,
            trigger = Trigger.OnSpellCast,
            effect = { /* Self heal logic */ }
        ),
        ClassFeature(
            name = "Divine Strike",
            description = "At 8th level, you gain the ability to infuse your weapon strikes with divine energy.",
            levelRequired = 8,
            trigger = Trigger.OnDamageCalculation,
            effect = { /* Extra radiant damage */ }
        ),
        ClassFeature(
            name = "Supreme Healing",
            description = "Starting at 17th level, when you would normally roll one or more dice to restore hit points with a spell, you instead use the highest number possible for each die.",
            levelRequired = 17,
            trigger = Trigger.OnSpellCast,
            effect = { /* Maximize healing */ }
        )
    )
}
