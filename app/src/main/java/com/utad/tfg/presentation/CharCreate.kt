package com.utad.tfg.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.utad.tfg.model.Ability
import com.utad.tfg.model.DndRace
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Class as DndClass
import com.utad.tfg.remote.JsonResource

import androidx.compose.ui.tooling.preview.Preview
import com.utad.tfg.ui.theme.TFGTheme

@Composable
fun CharCreateScreen(
    onCharacterCreated: () -> Unit = {}
) {
    val vm = hiltViewModel<MainViewModel>()
    val races by (vm.races.collectAsState())
    val classes by (vm.classes.collectAsState())
    val backgrounds by (vm.backgrounds.collectAsState())
    val subraces by (vm.subraces.collectAsState())
    val subclasses by (vm.subclasses.collectAsState())

    CharCreateContent(
        races = races,
        classes = classes,
        backgrounds = backgrounds,
        subraces = subraces,
        subclasses = subclasses,
        onRaceSelected = { vm.fetchSubraces(it) },
        onClassSelected = { vm.fetchSubclasses(it); vm.fetchSpells(it) },
        onSaveCharacter = { name, race, subrace, background, dndClass, subclass, scores ->
            vm.saveCharacter(name, race, subrace, background, dndClass, subclass, scores)
            onCharacterCreated()
        }
    )
}

@Composable
fun CharCreateContent(
    races: List<DndRace>,
    classes: List<DndClass>,
    backgrounds: List<JsonResource>,
    subraces: List<DndRace>,
    subclasses: List<Subclass>,
    onRaceSelected: (String) -> Unit,
    onClassSelected: (String) -> Unit,
    onSaveCharacter: (String, DndRace, DndRace?, JsonResource?, DndClass, Subclass?, Map<Ability, Int>) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedRace by remember { mutableStateOf<DndRace?>(null) }
    var selectedSubrace by remember { mutableStateOf<DndRace?>(null) }
    var selectedClass by remember { mutableStateOf<DndClass?>(null) }
    var selectedSubclass by remember { mutableStateOf<Subclass?>(null) }
    var selectedBackground by remember { mutableStateOf<JsonResource?>(null) }

    val abilityScores = remember { 
        mutableStateMapOf<Ability, Int>().apply {
            Ability.allAbilities.forEach { put(it, 10) }
        }
    }
    
    val totalPoints = 27
    val usedPoints = abilityScores.values.sumOf { calculatePointCost(it) }
    val remainingPoints = totalPoints - usedPoints

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Character Creation", style = MaterialTheme.typography.headlineMedium)

        // Name
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Character Name") },
            modifier = Modifier.fillMaxWidth()
        )

        // Race
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

        // Subrace
        if (subraces.isNotEmpty()) {
            DropdownSelector(
                label = "Subrace",
                options = subraces,
                selectedOption = selectedSubrace,
                optionName = { it.subraceName ?: it.raceName },
                onOptionSelected = { selectedSubrace = it }
            )
        }

        // Background
        DropdownSelector(
            label = "Background/Origin",
            options = backgrounds,
            selectedOption = selectedBackground,
            optionName = { it.name },
            onOptionSelected = { selectedBackground = it }
        )

        // Class
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

        // Subclass
        if (subclasses.isNotEmpty()) {
            DropdownSelector(
                label = "Subclass",
                options = subclasses,
                selectedOption = selectedSubclass,
                optionName = { it.subclassName },
                onOptionSelected = { selectedSubclass = it }
            )
        }

        // Ability Scores Section
        Text("Ability Scores (Points: $remainingPoints/$totalPoints)", style = MaterialTheme.typography.titleLarge)
        Ability.allAbilities.forEach { ability ->
            val score = abilityScores[ability] ?: 10
            AbilityScoreRow(
                ability = ability.abbreviation,
                score = score,
                canIncrease = remainingPoints >= getNextPointCost(score) && score < 15,
                canDecrease = score > 8,
                onIncrease = { abilityScores[ability] = score + 1 },
                onDecrease = { abilityScores[ability] = score - 1 }
            )
        }

        // TODO: Proficiencies and Level 1 Spells (Dynamic based on choices)
        Text("Proficiencies & Spells", style = MaterialTheme.typography.titleLarge)
        Text("Selection for these will be based on Class choices above.", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = {
                onSaveCharacter(
                    name,
                    selectedRace!!,
                    selectedSubrace,
                    selectedBackground,
                    selectedClass!!,
                    selectedSubclass,
                    abilityScores
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = name.isNotBlank() && selectedRace != null && selectedClass != null && remainingPoints == 0
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
            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth()
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
    onDecrease: () -> Unit
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
            Text("$score", modifier = Modifier.padding(horizontal = 8.dp), fontSize = 18.sp)
            IconButton(onClick = onIncrease, enabled = canIncrease) {
                Icon(Icons.Default.Add, contentDescription = "Increase")
            }
        }
        Text("${Ability.calculateModifier(score)}", modifier = Modifier.width(60.dp))
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
            backgrounds = emptyList(),
            subraces = emptyList(),
            subclasses = emptyList(),
            onRaceSelected = {},
            onClassSelected = {},
            onSaveCharacter = { _, _, _, _, _, _, _ -> }
        )
    }
}
