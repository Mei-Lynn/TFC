package com.utad.tfg.presentation

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.utad.tfg.model.Ability
import com.utad.tfg.model.DndRace
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Class as DndClass

import androidx.compose.ui.tooling.preview.Preview
import com.utad.tfg.local.entities.SpellEntity
import com.utad.tfg.ui.theme.TFGTheme

@Composable
fun CharCreateScreen(
    onCharacterCreated: () -> Unit = {}
) {
    val vm = hiltViewModel<MainViewModel>()
    val races by (vm.races.collectAsState())
    val classes by (vm.classes.collectAsState())
    val subraces by (vm.subraces.collectAsState())
    val subclasses by (vm.subclasses.collectAsState())
    val filteredSpells by (vm.filteredSpells.collectAsState())

    CharCreateContent(
        races = races,
        classes = classes,
        subraces = subraces,
        subclasses = subclasses,
        filteredSpells = filteredSpells,
        onRaceSelected = { vm.getSubraces(it) },
        onClassSelected = { vm.getSubclasses(it); vm.filterSpells(0..1, it) },
        onSaveCharacter = { name, race, subrace, dndClass, subclass, scores ->
            vm.saveCharacter(name, race, subrace, dndClass, subclass, scores)
            onCharacterCreated()
        }
    )
}

@Composable
fun CharCreateContent(
    races: List<DndRace>,
    classes: List<DndClass>,
    subraces: List<DndRace>,
    subclasses: List<Subclass>,
    filteredSpells: List<SpellEntity>,
    onRaceSelected: (String) -> Unit,
    onClassSelected: (String) -> Unit,
    onSaveCharacter: (String, DndRace, DndRace?, DndClass, Subclass?, Map<Ability, Int>) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedRace by remember { mutableStateOf<DndRace?>(null) }
    var selectedSubrace by remember { mutableStateOf<DndRace?>(null) }
    var selectedClass by remember { mutableStateOf<DndClass?>(null) }
    var selectedSubclass by remember { mutableStateOf<Subclass?>(null) }

    val abilityScores = remember { 
        mutableStateMapOf<Ability, Int>().apply {
            Ability.allAbilities.forEach { put(it, 10) }
        }
    }
    
    val totalPoints = 27
    val usedPoints = abilityScores.values.sumOf { calculatePointCost(it) }
    val remainingPoints = totalPoints - usedPoints

    var modPlus1 : Ability? by remember { mutableStateOf(null) }
    var modPlus2 : Ability? by remember { mutableStateOf(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Character Creation", style = MaterialTheme.typography.headlineMedium)

        // Nombre
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Character Name") },
            modifier = Modifier.fillMaxWidth()
        )

        // Raza
        DropdownSelector(
            label = "Race",
            options = races,
            selectedOption = selectedRace,
            optionName = { it.raceName },
            onOptionSelected = {
                selectedRace = it
                selectedSubrace = null
                it?.let { onRaceSelected(it.raceName) }
            }
        )

        // Subraza
        if (subraces.isNotEmpty()) {
            DropdownSelector(
                label = "Subrace",
                options = subraces,
                selectedOption = selectedSubrace,
                optionName = { it.subraceName ?: it.raceName },
                onOptionSelected = { selectedSubrace = it }
            )
        }

        // Clase
        DropdownSelector(
            label = "Class",
            options = classes,
            selectedOption = selectedClass,
            optionName = { it.className },
            onOptionSelected = {
                selectedClass = it
                selectedSubclass = null
                it?.let { onClassSelected(it.classIndex) }
            }
        )

        // Subclase
        if (subclasses.isNotEmpty()) {
            DropdownSelector(
                label = "Subclass",
                options = subclasses,
                selectedOption = selectedSubclass,
                optionName = { it.subclassName },
                onOptionSelected = { selectedSubclass = it }
            )
        }

        //Distribución de puntos de habilidad
        Text("Ability Scores (Points: $remainingPoints/$totalPoints)", style = MaterialTheme.typography.titleLarge)

        //Indicadores de +1 y +2
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("+1", modifier = Modifier.width(48.dp), style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.width(16.dp))
            Text("+2", modifier = Modifier.width(48.dp), style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Center)
        }

        Ability.allAbilities.forEach { ability ->
            val score = abilityScores[ability] ?: 10
            AbilityScoreRow(
                ability = ability.abbreviation,
                score = score,
                canIncrease = remainingPoints >= getNextPointCost(score) && score < 15,
                canDecrease = score > 8,
                onIncrease = { abilityScores[ability] = score + 1 },
                onDecrease = { abilityScores[ability] = score - 1 },
                modPlus1 = modPlus1 == ability,
                modPlus2 = modPlus2 == ability,
                onModPlus1Click = { if (modPlus2 != ability) modPlus1 = if (modPlus1 == ability) null else ability},
                onModPlus2Click = { if (modPlus1 != ability) modPlus2 = if (modPlus2 == ability) null else ability}
            )
        }

        Text("Spells", style = MaterialTheme.typography.titleLarge)
        if (filteredSpells.isNotEmpty() && selectedClass!!.spells[1].isNotEmpty()) {
            val spellsByLevel = filteredSpells.groupBy { it.level }

            spellsByLevel[0]?.let { cantrips ->
                Text("Cantrips (x/${selectedClass!!.cantrips[1]}):", style = MaterialTheme.typography.titleSmall)
                cantrips.forEach { Text("- ${it.name}") }
            }

            spellsByLevel[1]?.let { lvl1 ->
                Text("Level 1 Spells (x/${selectedClass!!.spells[1][1]}):", style = MaterialTheme.typography.titleSmall)
                lvl1.forEach { Text("- ${it.name}") }
            }
        } else if (selectedClass != null) {
            Text("This class doesn't have any spells available.", style = MaterialTheme.typography.bodyMedium)
        } else {
            Text("Selection for these will be based on Class choices above.", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = {
                modPlus1?.let { ability ->
                    abilityScores[ability] = (abilityScores[ability] ?: 10) + 1
                }
                modPlus2?.let { ability ->
                    abilityScores[ability] = (abilityScores[ability] ?: 10) + 2
                }

                onSaveCharacter(
                    name,
                    selectedRace!!,
                    selectedSubrace,
                    selectedClass!!,
                    selectedSubclass,
                    abilityScores
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = name.isNotBlank() && selectedRace != null && selectedClass != null && remainingPoints == 0 && modPlus1 != null && modPlus2 != null
        ) {
            Text("Create Character")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropdownSelector(
    label: String,
    options: List<T>,
    selectedOption: T?,
    optionName: (T) -> String,
    onOptionSelected: (T?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedOption?.let { optionName(it) } ?: "Select $label",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(optionName(option)) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun AbilityScoreRow(
    ability: String,
    score: Int,
    canIncrease: Boolean,
    canDecrease: Boolean,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    modPlus1: Boolean,
    modPlus2: Boolean,
    onModPlus1Click: (Boolean) -> Unit,
    onModPlus2Click: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(ability, modifier = Modifier.width(60.dp), fontWeight = FontWeight.Bold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onDecrease, enabled = canDecrease) {
                Icon(Icons.Default.Remove, contentDescription = "Decrease")
            }

            Text("$score", modifier = Modifier
                .padding(horizontal = 8.dp)
                .widthIn(32.dp), fontSize = 18.sp)

            val modifier = Ability.calculateModifier(score)
            Text("(${
                if (modifier >= 0) modifier
                else "Min"
            })")

            IconButton(onClick = onIncrease, enabled = canIncrease) {
                Icon(Icons.Default.Add, contentDescription = "Increase")
            }
        }

        // +1 Innato
        Checkbox(modPlus1, onModPlus1Click)

        // +2 Innato
        Checkbox(modPlus2,onModPlus2Click)
    }
}

fun calculatePointCost(score: Int): Int {
    return when (score) {
        8 -> 0
        9 -> 1
        10 -> 2
        11 -> 3
        12 -> 4
        13 -> 5
        14 -> 7
        15 -> 9
        else -> 0
    }
}

fun getNextPointCost(currentScore: Int): Int {
    return when (currentScore) {
        8, 9, 10, 11, 12 -> 1
        13, 14 -> 2
        else -> 0
    }
}

@Preview(showBackground = true)
@Composable
fun CharCreatePreview() {
    TFGTheme {
        CharCreateContent(
            races = listOf(com.utad.tfg.model.DndRace.Human.Standard),
            classes = listOf(com.utad.tfg.model.classes.barbarian.Barbarian()),
            subraces = emptyList(),
            subclasses = emptyList(),
            filteredSpells = emptyList(),
            onRaceSelected = {},
            onClassSelected = {},
            onSaveCharacter = { _, _, _, _, _, _ -> }
        )
    }
}
