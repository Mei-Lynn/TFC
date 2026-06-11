package com.utad.tfg.model.classes.rogue

import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Trigger

class Thief : Subclass {
    override val subclassName: String = "Thief"
    
    override val features: List<ClassFeature> = listOf(
        ClassFeature(
            name = "Fast Hands",
            description = "Starting at 3rd level, you can use the bonus action granted by your Cunning Action to make a Dexterity (Sleight of Hand) check, use your thieves' tools to disarm a trap or open a lock, or take the Use an Object action.",
            levelRequired = 3,
            trigger = Trigger.OnTurnStart,
            effect = { /* Object use logic */ }
        ),
        ClassFeature(
            name = "Second-Story Work",
            description = "When you choose this archetype at 3rd level, you gain the ability to climb faster than normal.",
            levelRequired = 3,
            trigger = Trigger.OnTurnStart,
            effect = { /* Climbing logic */ }
        ),
        ClassFeature(
            name = "Supreme Sneak",
            description = "Starting at 9th level, you have advantage on a Dexterity (Stealth) check if you move no more than half your speed on the same turn.",
            levelRequired = 9,
            trigger = Trigger.OnTurnStart,
            effect = { /* Stealth advantage logic */ }
        ),
        ClassFeature(
            name = "Use Magic Device",
            description = "By 13th level, you have learned enough about the workings of magic that you can improvise the use of items even when they are not intended for you.",
            levelRequired = 13,
            trigger = Trigger.OnTurnStart,
            effect = { /* Magic item logic */ }
        ),
        ClassFeature(
            name = "Thief's Reflexes",
            description = "When you reach 17th level, you have become adept at laying ambushes and quickly escaping danger. You can take two turns during the first round of any combat.",
            levelRequired = 17,
            trigger = Trigger.OnTurnStart,
            effect = { /* Double turn logic */ }
        )
    )
}
