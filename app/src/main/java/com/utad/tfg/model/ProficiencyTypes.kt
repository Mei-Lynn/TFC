package com.utad.tfg.model

sealed class ArmorType(val name: String) {
    data object Light : ArmorType("Light Armor")
    data object Medium : ArmorType("Medium Armor")
    data object Heavy : ArmorType("Heavy Armor")
    data object Shields : ArmorType("Shields")

    companion object {
        val all: List<ArmorType> get() = listOf(Light, Medium, Heavy, Shields)
    }
}

sealed class WeaponType(val name: String) {
    data object Simple : WeaponType("Simple Weapons")
    data object Martial : WeaponType("Martial Weapons")
    // Specific weapons could be added here if needed, but categories are most generic
    
    companion object {
        val all: List<WeaponType> get() = listOf(Simple, Martial)
    }
}
