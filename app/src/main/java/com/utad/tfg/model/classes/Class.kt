package com.utad.tfg.model.classes

import com.utad.tfg.model.Ability
import com.utad.tfg.model.ArmorType
import com.utad.tfg.model.WeaponType
import com.utad.tfg.local.entities.Character

interface Class {
    val className: String //Duh
    val classIndex: String //Index from the API
    var level: Int //Nivel actual en la clase, para navegar las listas
    
    val primaryAbility: Ability //Stat principal de escalado
    val savingThrows: List<Ability>
    
    val armorProficiencies: List<ArmorType>
    val weaponProficiencies: List<WeaponType>
    
    val hitDie: Int
    
    val cantrips: List<Int> //Número de trucos disponibles por nivel
    fun getPreparedSpellsLimit(char: Character): Int //Función que calcula el número máximo de hechizos preparados posibles
    val spellSlots: List<List<Int>> //Número máximo de espacios de hechizo, misma lógica
    //Ej.: Spells[2][3] -> Número de hechizos de nivel 3 que tiene la clase al nivel 2.
    
    val uniqueResources: List<ClassResource>
    
    val baseFeatures: List<ClassFeature>
    var selectedSubclass: Subclass?
    
    var actionsPerTurn: Int
    var bonusActionsPerTurn: Int

    val subclassProgression: List<Int>
    
    var attacksPerAction: Int

}
