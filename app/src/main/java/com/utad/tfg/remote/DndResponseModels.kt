package com.utad.tfg.remote

import com.google.gson.annotations.SerializedName
import com.utad.tfg.local.entities.Enemy

data class JsonResourceList(
    val count: Int,
    val results: List<JsonResource>
)

data class JsonResource(
    val index: String,
    val name: String,
    val url: String
)

data class DndRaceResponse(
    val index: String,
    val name: String,
    @SerializedName("speed") val speed: Int,
    @SerializedName("ability_bonuses") val abilityBonuses: List<AbilityBonus>,
    @SerializedName("alignment") val alignment: String,
    @SerializedName("age") val age: String,
    @SerializedName("size") val size: String,
    @SerializedName("starting_proficiencies") val startingProficiencies: List<JsonResource>,
    @SerializedName("languages") val languages: List<JsonResource>,
    @SerializedName("traits") val traits: List<JsonResource>,
    @SerializedName("subraces") val subraces: List<JsonResource>,
    @SerializedName("starting_proficiency_options") val startingProficiencyOptions: ProficiencyChoice? = null
)

data class DndSubraceResponse(
    val index: String,
    val name: String,
    val race: JsonResource,
    val desc: String,
    @SerializedName("ability_bonuses") val abilityBonuses: List<AbilityBonus>,
    @SerializedName("starting_proficiencies") val startingProficiencies: List<JsonResource>,
    @SerializedName("languages") val languages: List<JsonResource>,
    @SerializedName("racial_traits") val racialTraits: List<JsonResource>
)

data class AbilityBonus(
    @SerializedName("ability_score") val abilityScore: JsonResource,
    val bonus: Int
)

data class DndClassResponse(
    val index: String,
    val name: String,
    @SerializedName("hit_die") val hitDie: Int,
    @SerializedName("proficiency_choices") val proficiencyChoices: List<ProficiencyChoice>,
    @SerializedName("proficiencies") val proficiencies: List<JsonResource>,
    @SerializedName("saving_throws") val savingThrows: List<JsonResource>,
    @SerializedName("starting_equipment") val startingEquipment: List<EquipmentEntry>,
    @SerializedName("class_levels") val classLevels: String,
    @SerializedName("subclasses") val subclasses: List<JsonResource>,
    val spells: String? = null
)

data class DndSubclassResponse(
    val index: String,
    val name: String,
    @SerializedName("class") val dndClass: JsonResource,
    @SerializedName("subclass_flavor") val subclassFlavor: String,
    val desc: List<String>,
    val spells: List<JsonResource>? = null
)

data class ProficiencyChoice(
    val desc: String,
    val choose: Int,
    val type: String,
    val from: ProficiencyFrom
)

data class ProficiencyFrom(
    @SerializedName("option_set_type") val optionSetType: String,
    val options: List<ProficiencyOption>
)

data class ProficiencyOption(
    @SerializedName("option_type") val optionType: String,
    val item: JsonResource
)

data class EquipmentEntry(
    val equipment: JsonResource,
    val quantity: Int
)

data class DndSpellResponse(
    val index: String,
    val name: String,
    val desc: List<String>,
    @SerializedName("higher_level") val higherLevel: List<String>? = null,
    val range: String,
    val components: List<String>,
    val material: String? = null,
    val ritual: Boolean,
    val duration: String,
    val concentration: Boolean,
    @SerializedName("casting_time") val castingTime: String,
    val level: Int,
    @SerializedName("attack_type") val attackType: String? = null,
    val damage: SpellDamage? = null,
    val school: JsonResource,
    val classes: List<JsonResource>,
    val subclasses: List<JsonResource>,
    val url: String
)

data class SpellDamage(
    @SerializedName("damage_type") val damageType: JsonResource? = null,
    @SerializedName("damage_at_slot_level") val damageAtSlotLevel: Map<String, String>? = null,
    @SerializedName("damage_at_character_level") val damageAtCharacterLevel: Map<String, String>? = null
)

data class DndMonsterResponse(
    val index: String,
    val name: String,
    val size: String,
    val type: String,
    val alignment: String,
    @SerializedName("hit_points") val hitPoints: Int,
    @SerializedName("armor_class") val armorClass: List<ArmorClass>,
    val strength: Int,
    val dexterity: Int,
    val constitution: Int,
    val intelligence: Int,
    val wisdom: Int,
    val charisma: Int,
    val speed: MonsterSpeed,
    @SerializedName("challenge_rating") val challengeRating: Float,
    val xp: Int,
    val actions: List<MonsterAction>? = null,
    val image: String? = null
)

fun DndMonsterResponse.toEnemy(): Enemy {
    return Enemy(
        index = this.index,
        name = this.name,
        type = this.type,
        size = this.size,
        alignment = this.alignment,
        maxHp = this.hitPoints,
        currentHp = this.hitPoints,
        armorClass = this.armorClass.firstOrNull()?.value ?: 10,
        speed = this.speed.walk ?: "0 ft.",
        strength = this.strength,
        dexterity = this.dexterity,
        constitution = this.constitution,
        intelligence = this.intelligence,
        wisdom = this.wisdom,
        charisma = this.charisma,
        challengeRating = this.challengeRating,
        xp = this.xp,
        actions = this.actions,
        imgUri = this.image?.let { "https://www.dnd5eapi.co$it" }
    )
}

data class MonsterSpeed(
    val walk: String? = null,
    val fly: String? = null,
    val swim: String? = null,
    val climb: String? = null
)

data class MonsterAction(
    val name: String,
    val desc: String
)

data class ArmorClass(
    val type: String,
    val value: Int
)
