package com.utad.tfg.remote

import com.google.gson.annotations.SerializedName

data class DndResourceList(
    val count: Int,
    val results: List<DndResource>
)

data class DndResource(
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
    @SerializedName("starting_proficiencies") val startingProficiencies: List<DndResource>,
    @SerializedName("languages") val languages: List<DndResource>,
    @SerializedName("traits") val traits: List<DndResource>,
    @SerializedName("subraces") val subraces: List<DndResource>
)

data class AbilityBonus(
    @SerializedName("ability_score") val abilityScore: DndResource,
    val bonus: Int
)

data class DndClassResponse(
    val index: String,
    val name: String,
    @SerializedName("hit_die") val hitDie: Int,
    @SerializedName("proficiency_choices") val proficiencyChoices: List<ProficiencyChoice>,
    @SerializedName("proficiencies") val proficiencies: List<DndResource>,
    @SerializedName("saving_throws") val savingThrows: List<DndResource>,
    @SerializedName("starting_equipment") val startingEquipment: List<EquipmentEntry>,
    @SerializedName("class_levels") val classLevels: String,
    @SerializedName("subclasses") val subclasses: List<DndResource>,
    val spells: String? = null
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
    val item: DndResource
)

data class EquipmentEntry(
    val equipment: DndResource,
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
    val school: DndResource,
    val classes: List<DndResource>,
    val subclasses: List<DndResource>,
    val url: String
)

data class SpellDamage(
    @SerializedName("damage_type") val damageType: DndResource? = null,
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
