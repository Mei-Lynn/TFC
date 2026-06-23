package com.utad.tfg.model

object RaceRegistry {
    val races: List<DndRace> = listOf(
        DndRace.Human,
        DndRace.Elf.High,
        DndRace.Dwarf.Hill,
        DndRace.Halfling.Lightfoot,
        DndRace.Dragonborn.Chromatic,
        DndRace.Gnome.Forest,
        DndRace.HalfElf.Standard,
        DndRace.HalfOrc,
        DndRace.Tiefling.Asmodeus,
        DndRace.Aasimar.Protector,
        DndRace.Tabaxi,
        DndRace.Goliath,
        DndRace.Firbolg,
        DndRace.Kenku,
        DndRace.Triton,
        DndRace.Tortle,
        DndRace.Warforged,
        DndRace.Changeling,
        DndRace.Lizardfolk
    )

    fun getSubraces(raceName: String): List<DndRace> {
        return when (raceName) {
            "Elf" -> listOf(DndRace.Elf.High, DndRace.Elf.Wood, DndRace.Elf.Drow, DndRace.Elf.Eladrin, DndRace.Elf.Sea, DndRace.Elf.ShadarKai)
            "Dwarf" -> listOf(DndRace.Dwarf.Hill, DndRace.Dwarf.Mountain, DndRace.Dwarf.Duergar)
            "Halfling" -> listOf(DndRace.Halfling.Lightfoot, DndRace.Halfling.Stout, DndRace.Halfling.Ghostwise)
            "Dragonborn" -> listOf(DndRace.Dragonborn.Chromatic, DndRace.Dragonborn.Metallic, DndRace.Dragonborn.Gem)
            "Gnome" -> listOf(DndRace.Gnome.Forest, DndRace.Gnome.Rock, DndRace.Gnome.Deep)
            "Half-Elf" -> listOf(DndRace.HalfElf.Standard, DndRace.HalfElf.AquaticDescent, DndRace.HalfElf.DrowDescent, DndRace.HalfElf.HighElfDescent, DndRace.HalfElf.WoodElfDescent)
            "Tiefling" -> listOf(DndRace.Tiefling.Asmodeus, DndRace.Tiefling.Feral, DndRace.Tiefling.Baalzebul, DndRace.Tiefling.Dispater, DndRace.Tiefling.Fierna, DndRace.Tiefling.Glasya, DndRace.Tiefling.Levistus, DndRace.Tiefling.Mammon, DndRace.Tiefling.Mephistopheles, DndRace.Tiefling.Zariel)
            "Aasimar" -> listOf(DndRace.Aasimar.Protector, DndRace.Aasimar.Scourge, DndRace.Aasimar.Fallen)
            else -> emptyList()
        }
    }
}
