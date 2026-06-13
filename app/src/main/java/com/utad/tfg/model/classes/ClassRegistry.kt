package com.utad.tfg.model.classes

import com.utad.tfg.model.classes.barbarian.*
import com.utad.tfg.model.classes.bard.*
import com.utad.tfg.model.classes.cleric.*
import com.utad.tfg.model.classes.druid.*
import com.utad.tfg.model.classes.fighter.*
import com.utad.tfg.model.classes.monk.*
import com.utad.tfg.model.classes.paladin.*
import com.utad.tfg.model.classes.ranger.*
import com.utad.tfg.model.classes.rogue.*
import com.utad.tfg.model.classes.sorcerer.*
import com.utad.tfg.model.classes.warlock.*
import com.utad.tfg.model.classes.wizard.*

object ClassRegistry {
    val classes: List<Class> = listOf(
        Barbarian(),
        Bard(),
        Cleric(),
        Druid(),
        Fighter(),
        Monk(),
        Paladin(),
        Ranger(),
        Rogue(),
        Sorcerer(),
        Warlock(),
        Wizard()
    )

    fun getSubclasses(classIndex: String): List<Subclass> {
        return when (classIndex) {
            "barbarian" -> listOf(Berserker(), TotemWarrior())
            "bard" -> listOf(Lore(), Valor())
            "cleric" -> listOf(
                LifeDomain(), 
                KnowledgeDomain(), 
                LightDomain(), 
                NatureDomain(), 
                TempestDomain(), 
                TrickeryDomain(), 
                WarDomain()
            )
            "druid" -> listOf(Land(), Moon())
            "fighter" -> listOf(Champion(), BattleMaster(), EldritchKnight())
            "monk" -> listOf(OpenHand(), Shadow(), FourElements())
            "paladin" -> listOf(DevotionOath(), AncientsOath(), VengeanceOath())
            "ranger" -> listOf(Hunter(), BeastMaster())
            "rogue" -> listOf(Thief(), Assassin(), ArcaneTrickster())
            "sorcerer" -> listOf(DraconicBloodline(), WildMagic())
            "warlock" -> listOf(Fiend(), Archfey(), GreatOldOne())
            "wizard" -> listOf(
                Evocation(), 
                Abjuration(), 
                Conjuration(), 
                Divination(), 
                Enchantment(), 
                Illusion(), 
                Necromancy(), 
                Transmutation()
            )
            else -> emptyList()
        }
    }

    fun createClass(classIndex: String, level: Int = 1): Class? {
        return when (classIndex) {
            "barbarian" -> Barbarian(level)
            "bard" -> Bard(level)
            "cleric" -> Cleric(level)
            "druid" -> Druid(level)
            "fighter" -> Fighter(level)
            "monk" -> Monk(level)
            "paladin" -> Paladin(level)
            "ranger" -> Ranger(level)
            "rogue" -> Rogue(level)
            "sorcerer" -> Sorcerer(level)
            "warlock" -> Warlock(level)
            "wizard" -> Wizard(level)
            else -> null
        }
    }
}
