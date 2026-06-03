package com.utad.tfg.model

sealed class DndRace(val raceName: String, val subraceName: String? = null) {
    // Core Races
    sealed class Human(subrace: String? = null) : DndRace("Human", subrace) {
        data object Standard : Human()
        data object Variant : Human("Variant")
    }

    sealed class Elf(subrace: String) : DndRace("Elf", subrace) {
        data object High : Elf("High Elf")
        data object Wood : Elf("Wood Elf")
        data object Drow : Elf("Drow")
        data object Eladrin : Elf("Eladrin")
        data object Sea : Elf("Sea Elf")
        data object ShadarKai : Elf("Shadar-kai")
    }

    sealed class Dwarf(subrace: String) : DndRace("Dwarf", subrace) {
        data object Hill : Dwarf("Hill Dwarf")
        data object Mountain : Dwarf("Mountain Dwarf")
        data object Duergar : Dwarf("Duergar")
    }

    sealed class Halfling(subrace: String) : DndRace("Halfling", subrace) {
        data object Lightfoot : Halfling("Lightfoot")
        data object Stout : Halfling("Stout")
        data object Ghostwise : Halfling("Ghostwise")
    }

    sealed class Dragonborn(subrace: String? = null) : DndRace("Dragonborn", subrace) {
        data object Chromatic : Dragonborn("Chromatic")
        data object Metallic : Dragonborn("Metallic")
        data object Gem : Dragonborn("Gem")
    }

    sealed class Gnome(subrace: String) : DndRace("Gnome", subrace) {
        data object Forest : Gnome("Forest Gnome")
        data object Rock : Gnome("Rock Gnome")
        data object Deep : Gnome("Deep Gnome (Svirfneblin)")
    }

    sealed class HalfElf(subrace: String? = null) : DndRace("Half-Elf", subrace) {
        data object Standard : HalfElf()
        // Sword Coast Adventurer's Guide variants
        data object AquaticDescent : HalfElf("Aquatic Descent")
        data object DrowDescent : HalfElf("Drow Descent")
        data object HighElfDescent : HalfElf("High Elf Descent")
        data object WoodElfDescent : HalfElf("Wood Elf Descent")
    }

    data object HalfOrc : DndRace("Half-Orc")

    sealed class Tiefling(subrace: String? = null) : DndRace("Tiefling", subrace) {
        // Player's Handbook
        data object Asmodeus : Tiefling("Asmodeus")
        // Sword Coast Adventurer's Guide
        data object Feral : Tiefling("Feral")
        // Mordenkainen's Tome of Foes — infernal bloodlines
        data object Baalzebul : Tiefling("Baalzebul")
        data object Dispater : Tiefling("Dispater")
        data object Fierna : Tiefling("Fierna")
        data object Glasya : Tiefling("Glasya")
        data object Levistus : Tiefling("Levistus")
        data object Mammon : Tiefling("Mammon")
        data object Mephistopheles : Tiefling("Mephistopheles")
        data object Zariel : Tiefling("Zariel")
    }
    data object Drow : DndRace("Drow")

    // Additional Races
    sealed class Aasimar(subrace: String) : DndRace("Aasimar", subrace) {
        // Volo's Guide to Monsters
        data object Protector : Aasimar("Protector")
        data object Scourge : Aasimar("Scourge")
        data object Fallen : Aasimar("Fallen")
    }
    data object Tabaxi : DndRace("Tabaxi")
    data object Goliath : DndRace("Goliath")
    data object Firbolg : DndRace("Firbolg")
    data object Kenku : DndRace("Kenku")
    data object Triton : DndRace("Triton")
    data object Tortle : DndRace("Tortle")
    data object Warforged : DndRace("Warforged")
    data object Changeling : DndRace("Changeling")
    data object Lizardfolk : DndRace("Lizardfolk")

    // Homebrew
    data class Homebrew(val customRace: String, val customSubrace: String? = null) : DndRace(customRace, customSubrace)
}
