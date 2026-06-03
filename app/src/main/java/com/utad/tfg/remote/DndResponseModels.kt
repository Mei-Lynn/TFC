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
