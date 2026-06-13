package com.utad.tfg.model.equipment

import com.utad.tfg.model.ArmorType
import com.utad.tfg.model.WeaponType

object EquipmentRegistry {
    val weapons = listOf(
        // Simple Melee
        Weapon("Club", "club", "1d4", "bludgeoning", WeaponType.Simple, listOf(WeaponProperty.LIGHT)),
        Weapon("Dagger", "dagger", "1d4", "piercing", WeaponType.Simple, listOf(WeaponProperty.FINESSE, WeaponProperty.LIGHT, WeaponProperty.THROWN)),
        Weapon("Greatclub", "greatclub", "1d8", "bludgeoning", WeaponType.Simple, listOf(WeaponProperty.TWO_HANDED)),
        Weapon("Handaxe", "handaxe", "1d6", "slashing", WeaponType.Simple, listOf(WeaponProperty.LIGHT, WeaponProperty.THROWN)),
        Weapon("Javelin", "javelin", "1d6", "piercing", WeaponType.Simple, listOf(WeaponProperty.THROWN)),
        Weapon("Light Hammer", "light-hammer", "1d4", "bludgeoning", WeaponType.Simple, listOf(WeaponProperty.LIGHT, WeaponProperty.THROWN)),
        Weapon("Mace", "mace", "1d6", "bludgeoning", WeaponType.Simple, emptyList()),
        Weapon("Quarterstaff", "quarterstaff", "1d6", "bludgeoning", WeaponType.Simple, listOf(WeaponProperty.VERSATILE)),
        Weapon("Sickle", "sickle", "1d4", "slashing", WeaponType.Simple, listOf(WeaponProperty.LIGHT)),
        Weapon("Spear", "spear", "1d6", "piercing", WeaponType.Simple, listOf(WeaponProperty.THROWN, WeaponProperty.VERSATILE)),

        // Simple Ranged
        Weapon("Crossbow, light", "crossbow-light", "1d8", "piercing", WeaponType.Simple, listOf(WeaponProperty.AMMUNITION, WeaponProperty.LOADING, WeaponProperty.TWO_HANDED)),
        Weapon("Dart", "dart", "1d4", "piercing", WeaponType.Simple, listOf(WeaponProperty.FINESSE, WeaponProperty.THROWN)),
        Weapon("Shortbow", "shortbow", "1d6", "piercing", WeaponType.Simple, listOf(WeaponProperty.AMMUNITION, WeaponProperty.TWO_HANDED)),
        Weapon("Sling", "sling", "1d4", "bludgeoning", WeaponType.Simple, listOf(WeaponProperty.AMMUNITION)),

        // Martial Melee
        Weapon("Battleaxe", "battleaxe", "1d8", "slashing", WeaponType.Martial, listOf(WeaponProperty.VERSATILE)),
        Weapon("Flail", "flail", "1d8", "bludgeoning", WeaponType.Martial, emptyList()),
        Weapon("Glaive", "glaive", "1d10", "slashing", WeaponType.Martial, listOf(WeaponProperty.HEAVY, WeaponProperty.REACH, WeaponProperty.TWO_HANDED), reach = 10),
        Weapon("Greataxe", "greataxe", "1d12", "slashing", WeaponType.Martial, listOf(WeaponProperty.HEAVY, WeaponProperty.TWO_HANDED)),
        Weapon("Greatsword", "greatsword", "2d6", "slashing", WeaponType.Martial, listOf(WeaponProperty.HEAVY, WeaponProperty.TWO_HANDED)),
        Weapon("Halberd", "halberd", "1d10", "slashing", WeaponType.Martial, listOf(WeaponProperty.HEAVY, WeaponProperty.REACH, WeaponProperty.TWO_HANDED), reach = 10),
        Weapon("Lance", "lance", "1d12", "piercing", WeaponType.Martial, listOf(WeaponProperty.REACH), reach = 10),
        Weapon("Longsword", "longsword", "1d8", "slashing", WeaponType.Martial, listOf(WeaponProperty.VERSATILE)),
        Weapon("Maul", "maul", "2d6", "bludgeoning", WeaponType.Martial, listOf(WeaponProperty.HEAVY, WeaponProperty.TWO_HANDED)),
        Weapon("Morningstar", "morningstar", "1d8", "piercing", WeaponType.Martial, emptyList()),
        Weapon("Pike", "pike", "1d10", "piercing", WeaponType.Martial, listOf(WeaponProperty.HEAVY, WeaponProperty.REACH, WeaponProperty.TWO_HANDED), reach = 10),
        Weapon("Rapier", "rapier", "1d8", "piercing", WeaponType.Martial, listOf(WeaponProperty.FINESSE)),
        Weapon("Scimitar", "scimitar", "1d6", "slashing", WeaponType.Martial, listOf(WeaponProperty.FINESSE, WeaponProperty.LIGHT)),
        Weapon("Shortsword", "shortsword", "1d6", "piercing", WeaponType.Martial, listOf(WeaponProperty.FINESSE, WeaponProperty.LIGHT)),
        Weapon("Trident", "trident", "1d6", "piercing", WeaponType.Martial, listOf(WeaponProperty.THROWN, WeaponProperty.VERSATILE)),
        Weapon("War Pick", "war-pick", "1d8", "piercing", WeaponType.Martial, emptyList()),
        Weapon("Warhammer", "warhammer", "1d8", "bludgeoning", WeaponType.Martial, listOf(WeaponProperty.VERSATILE)),
        Weapon("Whip", "whip", "1d4", "slashing", WeaponType.Martial, listOf(WeaponProperty.FINESSE, WeaponProperty.REACH), reach = 10),

        // Martial Ranged
        Weapon("Blowgun", "blowgun", "1", "piercing", WeaponType.Martial, listOf(WeaponProperty.AMMUNITION, WeaponProperty.LOADING)),
        Weapon("Crossbow, hand", "crossbow-hand", "1d6", "piercing", WeaponType.Martial, listOf(WeaponProperty.AMMUNITION, WeaponProperty.LIGHT, WeaponProperty.LOADING)),
        Weapon("Crossbow, heavy", "crossbow-heavy", "1d10", "piercing", WeaponType.Martial, listOf(WeaponProperty.AMMUNITION, WeaponProperty.HEAVY, WeaponProperty.LOADING, WeaponProperty.TWO_HANDED)),
        Weapon("Longbow", "longbow", "1d8", "piercing", WeaponType.Martial, listOf(WeaponProperty.AMMUNITION, WeaponProperty.HEAVY, WeaponProperty.TWO_HANDED))
    )

    val armors = listOf(
        // Light Armor
        Armor("Padded", "padded", 11, ArmorType.Light, stealthDisadvantage = true),
        Armor("Leather", "leather", 11, ArmorType.Light),
        Armor("Studded Leather", "studded-leather", 12, ArmorType.Light),

        // Medium Armor
        Armor("Hide", "hide", 12, ArmorType.Medium),
        Armor("Chain Shirt", "chain-shirt", 13, ArmorType.Medium),
        Armor("Scale Mail", "scale-mail", 14, ArmorType.Medium, stealthDisadvantage = true),
        Armor("Breastplate", "breastplate", 14, ArmorType.Medium),
        Armor("Half Plate", "half-plate", 15, ArmorType.Medium, stealthDisadvantage = true),

        // Heavy Armor
        Armor("Ring Mail", "ring-mail", 14, ArmorType.Heavy, stealthDisadvantage = true),
        Armor("Chain Mail", "chain-mail", 16, ArmorType.Heavy, strengthRequired = 13, stealthDisadvantage = true),
        Armor("Splint", "splint", 17, ArmorType.Heavy, strengthRequired = 15, stealthDisadvantage = true),
        Armor("Plate", "plate", 18, ArmorType.Heavy, strengthRequired = 15, stealthDisadvantage = true),

        // Shield
        Armor("Shield", "shield", 2, ArmorType.Shields)
    )

    fun getWeaponByIndex(index: String): Weapon? = weapons.find { it.weaponIndex == index }
    fun getArmorByIndex(index: String): Armor? = armors.find { it.armorIndex == index }
}
