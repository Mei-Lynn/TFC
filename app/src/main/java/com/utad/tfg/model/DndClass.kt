package com.utad.tfg.model

sealed class DndClass(val className: String, val subclass: String? = null) {

    sealed class Artificer(subclass: String) : DndClass("Artificer", subclass) {
        data object Alchemist : Artificer("Alchemist")
        data object Armorer : Artificer("Armorer")
        data object Artillerist : Artificer("Artillerist")
        data object BattleSmith : Artificer("Battle Smith")
    }

    sealed class Barbarian(subclass: String) : DndClass("Barbarian", subclass) {
        data object PathOfTheBerserker : Barbarian("Path of the Berserker")
        data object PathOfTheTotemWarrior : Barbarian("Path of the Totem Warrior")
        data object PathOfTheAncestralGuardian : Barbarian("Path of the Ancestral Guardian")
        data object PathOfTheStormHerald : Barbarian("Path of the Storm Herald")
        data object PathOfTheZealot : Barbarian("Path of the Zealot")
        data object PathOfTheBeast : Barbarian("Path of the Beast")
        data object PathOfWildMagic : Barbarian("Path of Wild Magic")
    }

    sealed class Bard(subclass: String) : DndClass("Bard", subclass) {
        data object CollegeOfLore : Bard("College of Lore")
        data object CollegeOfValor : Bard("College of Valor")
        data object CollegeOfGlamour : Bard("College of Glamour")
        data object CollegeOfSwords : Bard("College of Swords")
        data object CollegeOfWhispers : Bard("College of Whispers")
        data object CollegeOfCreation : Bard("College of Creation")
        data object CollegeOfEloquence : Bard("College of Eloquence")
    }

    sealed class Cleric(subclass: String) : DndClass("Cleric", subclass) {
        data object KnowledgeDomain : Cleric("Knowledge Domain")
        data object LifeDomain : Cleric("Life Domain")
        data object LightDomain : Cleric("Light Domain")
        data object NatureDomain : Cleric("Nature Domain")
        data object TempestDomain : Cleric("Tempest Domain")
        data object TrickeryDomain : Cleric("Trickery Domain")
        data object WarDomain : Cleric("War Domain")
        data object ForgeDomain : Cleric("Forge Domain")
        data object GraveDomain : Cleric("Grave Domain")
        data object OrderDomain : Cleric("Order Domain")
        data object PeaceDomain : Cleric("Peace Domain")
        data object TwilightDomain : Cleric("Twilight Domain")
    }

    sealed class Druid(subclass: String) : DndClass("Druid", subclass) {
        data object CircleOfTheLand : Druid("Circle of the Land")
        data object CircleOfTheMoon : Druid("Circle of the Moon")
        data object CircleOfDreams : Druid("Circle of Dreams")
        data object CircleOfTheShepherd : Druid("Circle of the Shepherd")
        data object CircleOfSpores : Druid("Circle of Spores")
        data object CircleOfStars : Druid("Circle of Stars")
        data object CircleOfWildfire : Druid("Circle of Wildfire")
    }

    sealed class Fighter(subclass: String) : DndClass("Fighter", subclass) {
        data object Champion : Fighter("Champion")
        data object BattleMaster : Fighter("Battle Master")
        data object EldritchKnight : Fighter("Eldritch Knight")
        data object ArcaneArcher : Fighter("Arcane Archer")
        data object Cavalier : Fighter("Cavalier")
        data object Samurai : Fighter("Samurai")
        data object EchoKnight : Fighter("Echo Knight")
        data object PsiWarrior : Fighter("Psi Warrior")
        data object RuneKnight : Fighter("Rune Knight")
    }

    sealed class Monk(subclass: String) : DndClass("Monk", subclass) {
        data object WayOfTheOpenHand : Monk("Way of the Open Hand")
        data object WayOfShadow : Monk("Way of Shadow")
        data object WayOfTheFourElements : Monk("Way of the Four Elements")
        data object WayOfTheLongDeath : Monk("Way of the Long Death")
        data object WayOfTheSunSoul : Monk("Way of the Sun Soul")
        data object WayOfLazyDrunkenMaster : Monk("Way of the Drunken Master")
        data object WayOfKensei : Monk("Way of Kensei")
        data object WayOfMercy : Monk("Way of Mercy")
        data object WayOfTheAstralSelf : Monk("Way of the Astral Self")
    }

    sealed class Paladin(subclass: String) : DndClass("Paladin", subclass) {
        data object OathOfDevotion : Paladin("Oath of Devotion")
        data object OathOfAncients : Paladin("Oath of the Ancients")
        data object OathOfVengeance : Paladin("Oath of Vengeance")
        data object OathOfTheCrown : Paladin("Oath of the Crown")
        data object OathOfConquest : Paladin("Oath of Conquest")
        data object OathOfRedemption : Paladin("Oath of Redemption")
        data object OathOfGlory : Paladin("Oath of Glory")
        data object OathOfTheWatchers : Paladin("Oath of the Watchers")
        data object Oathbreaker : Paladin("Oathbreaker")
    }

    sealed class Ranger(subclass: String) : DndClass("Ranger", subclass) {
        data object Hunter : Ranger("Hunter")
        data object BeastMaster : Ranger("Beast Master")
        data object GloomStalker : Ranger("Gloom Stalker")
        data object HorizonWalker : Ranger("Horizon Walker")
        data object MonsterSlayer : Ranger("Monster Slayer")
        data object FeyWanderer : Ranger("Fey Wanderer")
        data object Swarmkeeper : Ranger("Swarmkeeper")
        data object Drakewarden : Ranger("Drakewarden")
    }

    sealed class Rogue(subclass: String) : DndClass("Rogue", subclass) {
        data object Thief : Rogue("Thief")
        data object Assassin : Rogue("Assassin")
        data object ArcaneTrickster : Rogue("Arcane Trickster")
        data object Inquisitive : Rogue("Inquisitive")
        data object Mastermind : Rogue("Mastermind")
        data object Scout : Rogue("Scout")
        data object Swashbuckler : Rogue("Swashbuckler")
        data object Phantom : Rogue("Phantom")
        data object Soulknife : Rogue("Soulknife")
    }

    sealed class Sorcerer(subclass: String) : DndClass("Sorcerer", subclass) {
        data object DraconicBloodline : Sorcerer("Draconic Bloodline")
        data object WildMagic : Sorcerer("Wild Magic")
        data object DivineSoul : Sorcerer("Divine Soul")
        data object StormSorcery : Sorcerer("Storm Sorcery")
        data object ShadowMagic : Sorcerer("Shadow Magic")
        data object AberrantMind : Sorcerer("Aberrant Mind")
        data object ClockworkSoul : Sorcerer("Clockwork Soul")
    }

    sealed class Warlock(subclass: String) : DndClass("Warlock", subclass) {
        data object TheArchfey : Warlock("The Archfey")
        data object TheFiend : Warlock("The Fiend")
        data object TheGreatOldOne : Warlock("The Great Old One")
        data object TheCelestial : Warlock("The Celestial")
        data object TheHexblade : Warlock("The Hexblade")
        data object TheFathomless : Warlock("The Fathomless")
        data object TheGenie : Warlock("The Genie")
        data object TheUndead : Warlock("The Undead")
    }

    sealed class Wizard(subclass: String) : DndClass("Wizard", subclass) {
        data object SchoolOfAbjuration : Wizard("School of Abjuration")
        data object SchoolOfConjuration : Wizard("School of Conjuration")
        data object SchoolOfDivination : Wizard("School of Divination")
        data object SchoolOfEnchantment : Wizard("School of Enchantment")
        data object SchoolOfEvocation : Wizard("School of Evocation")
        data object SchoolOfIllusion : Wizard("School of Illusion")
        data object SchoolOfNecromancy : Wizard("School of Necromancy")
        data object SchoolOfTransmutation : Wizard("School of Transmutation")
        data object WarMagic : Wizard("War Magic")
        data object Bladesinging : Wizard("Bladesinging")
        data object OrderOfScribes : Wizard("Order of Scribes")
    }
}
