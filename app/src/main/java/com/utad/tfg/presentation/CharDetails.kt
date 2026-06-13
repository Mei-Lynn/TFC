package com.utad.tfg.presentation

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.utad.tfg.local.entities.Character
import com.utad.tfg.local.entities.SpellEntity
import com.utad.tfg.model.Ability
import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.ClassRegistry
import com.utad.tfg.model.classes.ClassResource
import com.utad.tfg.model.classes.Class as DndClass
import com.utad.tfg.model.equipment.Armor
import com.utad.tfg.model.equipment.Equipment
import com.utad.tfg.model.equipment.EquipmentRegistry
import com.utad.tfg.model.equipment.Weapon
import com.utad.tfg.model.equipment.WeaponProperty
import com.utad.tfg.remote.DndSpellResponse
import androidx.compose.ui.res.stringResource
import com.utad.tfg.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharDetailsScreen(
    onBack: () -> Unit = {},
    onLevelUp: () -> Unit = {}
) {
    val vm = hiltViewModel<MainViewModel>(LocalContext.current as ComponentActivity)
    val character by vm.selectedCharacter.collectAsStateWithLifecycle()
    val characterSpells by vm.fullSelectedCharacterSpells.collectAsStateWithLifecycle()
    
    var showSpellDialog by remember { mutableStateOf(false) }
    var selectedSpellIndex by remember { mutableStateOf<String?>(null) }
    var showSpellChangeDialog by remember { mutableStateOf(false) }

    val canPrepareSpells = character?.classIndex in listOf("wizard", "cleric", "druid")

    LaunchedEffect(character) {
        vm.loadSelectedCharacterSpells()
    }

    if (showSpellDialog && selectedSpellIndex != null) {
        SpellInfoDialog(
            onDismiss = { showSpellDialog = false; vm.clearSpellDetails() },
            index = selectedSpellIndex!!
        )
    }

    if (showSpellChangeDialog && character != null) {
        SpellChangeDialog(
            onDissmiss = { showSpellChangeDialog = false },
            character = character!!
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(0.95f),
                title = { Text(character?.name ?: "Character Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                actions = {
                    character?.let { char ->
                        if (char.level < 20) {
                            OutlinedButton(onClick = onLevelUp) {
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Text(stringResource(R.string.level_up))
                                    Icon(Icons.Default.Upgrade, contentDescription = stringResource(R.string.level_up))
                                }
                            }
                        }
                    }
                }
            )
        }
    ) { padding ->
        character?.let { char ->
            val dndClass = remember(char) {
                ClassRegistry.createClass(char.classIndex, char.level)
            }

            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Info
                HeaderSection(char)

                HorizontalDivider()

                // HP and AC
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    StatBox("AC", "${char.armorClass}")
                    StatBox("HP", "${char.currentHp}/${char.maxHp}")
                    StatBox("Init", if (Ability.calculateModifier(char.dexterity) >= 0) "+${Ability.calculateModifier(char.dexterity)}" else "${Ability.calculateModifier(char.dexterity)}")
                }

                HorizontalDivider()

                // Ability Scores
                AbilityScoresGrid(char)

                // Saving Throws
                if (dndClass != null) {
                    SavingThrowsSection(char, dndClass)
                }

                HorizontalDivider()

                // Resources (Spell Slots, etc.)
                if (dndClass != null) {
                    ResourcesSection(char, dndClass)
                }

                HorizontalDivider()

                // Equipment
                EquipmentSection(char)

                HorizontalDivider()

                // Features
                if (dndClass != null) {
                    FeaturesSection(char, dndClass)
                    HorizontalDivider()
                }

                // Spells
                if (characterSpells.isNotEmpty()) {
                    SpellsSection(
                        spells = characterSpells,
                        onSpellClick = {
                            selectedSpellIndex = it
                            showSpellDialog = true
                        }
                    )
                }

                if (canPrepareSpells) {
                    Button(
                        onClick = { showSpellChangeDialog = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.change_prepared_spells))
                    }
                }
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun HeaderSection(char: Character) {
    Column {
        val race = char.raceIndex.replaceFirstChar { it.uppercase() } +
                (char.subraceIndex?.let { " (${it.replaceFirstChar { char -> char.uppercase() }})" } ?: "")
        val dndClass = char.classIndex.replaceFirstChar { it.uppercase() } +
                (char.subclassIndex?.let { " (${it.replaceFirstChar { char -> char.uppercase() }})" } ?: "")

        Text(
            text = "Level ${char.level} $race $dndClass",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun StatBox(label: String, value: String) {
    Card(
        modifier = Modifier.width(120.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, style = MaterialTheme.typography.labelSmall)
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AbilityScoresGrid(char: Character) {
    val scores = listOf(
        "STR" to char.strength,
        "DEX" to char.dexterity,
        "CON" to char.constitution,
        "INT" to char.intelligence,
        "WIS" to char.wisdom,
        "CHA" to char.charisma
    )

    Column {
        Text(stringResource(R.string.ability_scores), style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            scores.take(3).forEach { (label, score) ->
                AbilityScoreItem(label, score)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            scores.drop(3).forEach { (label, score) ->
                AbilityScoreItem(label, score)
            }
        }
    }
}

@Composable
fun AbilityScoreItem(label: String, score: Int) {
    val mod = Ability.calculateModifier(score)
    val modText = if (mod >= 0) "+$mod" else "$mod"
    Column(
        modifier = Modifier.width(60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
        Text("$score", style = MaterialTheme.typography.bodyLarge)
        Text("($modText)", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun SavingThrowsSection(char: Character, dndClass: DndClass) {
    val profBonus = 2 + (char.level - 1) / 4
    val proficiencies = dndClass.savingThrows

    Column {
        Text(stringResource(R.string.saving_throws), style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        
        val rows = listOf(
            listOf(Ability.Strength, Ability.Dexterity, Ability.Constitution),
            listOf(Ability.Intelligence, Ability.Wisdom, Ability.Charisma)
        )

        rows.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                row.forEach { ability ->
                    val score = when(ability) {
                        Ability.Strength -> char.strength
                        Ability.Dexterity -> char.dexterity
                        Ability.Constitution -> char.constitution
                        Ability.Intelligence -> char.intelligence
                        Ability.Wisdom -> char.wisdom
                        Ability.Charisma -> char.charisma
                    }
                    val isProficient = proficiencies.contains(ability)
                    val mod = Ability.calculateModifier(score) + (if (isProficient) profBonus else 0)
                    val modText = if (mod >= 0) "+$mod" else "$mod"

                    Row(
                        modifier = Modifier.width(100.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isProficient) "●" else "○",
                            color = if (isProficient) MaterialTheme.colorScheme.primary else Color.Gray,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text(ability.abbreviation, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                        Text(modText, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun ResourcesSection(char: Character, dndClass: DndClass) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Spell Slots
        val slots = dndClass.spellSlots[char.level]
        if (slots.isNotEmpty()) {
            Text(stringResource(R.string.spell_slots), style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                slots.forEachIndexed { index, count ->
                    if (count > 0) {
                        SlotItem("Lvl ${index + 1}", count, MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        // Unique Resources
        if (dndClass.uniqueResources.isNotEmpty()) {
            Text(stringResource(R.string.class_resources), style = MaterialTheme.typography.titleMedium)
            dndClass.uniqueResources.forEach { resource ->
                when (resource) {
                    is ClassResource.SimplePool -> {
                        val amount = resource.amount.getOrElse(char.level) { 0 }
                        if (amount > 0) {
                            Text("${resource.name}: $amount", style = MaterialTheme.typography.bodyMedium)
                        } else {
                            Text(stringResource(R.string.go_up_levels_unlock), style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    is ClassResource.LevelledSlots -> {
                        SlotItem("Lvl ${resource.level[char.level]}", resource.count[char.level], Color(191, 64, 191))
                    }
                    is ClassResource.DicePool -> {
                        Text("${resource.name}: d${resource.diceSize(char)} x ${resource.count(char)}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun SlotItem(label: String, count: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelSmall)
        Row {
            repeat(count) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .padding(2.dp)
                        .background(color, RoundedCornerShape(2.dp))
                )
            }
        }
    }
}

@Composable
fun EquipmentSection(char: Character) {
    val mainHand = EquipmentRegistry.getWeaponByIndex(char.mainHandIndex ?: "")
    val offHand = EquipmentRegistry.getWeaponByIndex(char.offHandIndex ?: "") ?: EquipmentRegistry.getArmorByIndex(char.offHandIndex ?: "")
    val armor = EquipmentRegistry.getArmorByIndex(char.armorIndex ?: "")

    Column {
        Text(stringResource(R.string.equipment), style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        
        EquipmentItem("Main Hand", mainHand?.weaponName ?: "Empty")
        EquipmentItem("Off Hand", when(offHand) {
            is Weapon -> offHand.weaponName
            is Armor -> offHand.armorName
            else -> if (mainHand!= null && mainHand.properties.contains(WeaponProperty.TWO_HANDED)) "Two-handed" else "Empty"
        })
        EquipmentItem("Armor", armor?.armorName ?: "Unarmored")
    }
}

@Composable
fun EquipmentItem(label: String, name: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        Text(name, style = MaterialTheme.typography.bodyMedium)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SpellsSection(spells: List<SpellEntity>, onSpellClick: (String) -> Unit) {
    Column {
        Text(stringResource(R.string.prepared_spells_and_cantrips), style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            spells.forEach { spell ->
                Card(
                    modifier = Modifier.clickable { onSpellClick(spell.index) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (spell.level == 0) MaterialTheme.colorScheme.tertiaryContainer 
                                         else MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text(
                        text = spell.name,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
fun SpellInfoDialog(onDismiss: () -> Unit, index: String) {
    val vm = hiltViewModel<MainViewModel>(LocalContext.current as ComponentActivity)
    val spell by vm.spellDetails.collectAsStateWithLifecycle()

    LaunchedEffect(index) {
        vm.fetchSpellDetails(index)
    }

    Dialog(onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            if (spell == null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(stringResource(R.string.loading_spell_details))
                }
            } else {
                val s = spell!!
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = s.name,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${if (s.level == 0) "Cantrip" else "Level ${s.level}"} ${s.school.name}${if (s.ritual) " (Ritual)" else ""}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = FontStyle.Italic
                    )

                    HorizontalDivider()

                    DetailItem("Casting Time", s.castingTime)
                    DetailItem("Range", s.range)
                    DetailItem("Components", s.components.joinToString(", "))
                    if (s.material != null) {
                        DetailItem("Material", s.material)
                    }
                    DetailItem("Duration", s.duration + (if (s.concentration) " (Concentration)" else ""))

                    HorizontalDivider()

                    Text(text = s.desc.joinToString("\n\n"), style = MaterialTheme.typography.bodyMedium)

                    if (!s.higherLevel.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(stringResource(R.string.at_higher_levels), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Text(text = s.higherLevel.joinToString("\n"), style = MaterialTheme.typography.bodySmall)
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(onClick = onDismiss, modifier = Modifier.align(Alignment.End)) {
                        Text(stringResource(R.string.close))
                    }
                } 
            }
        }
    }
}

@Composable
fun SpellChangeDialog(onDissmiss: () -> Unit, character: Character) {
    val vm = hiltViewModel<MainViewModel>(LocalContext.current as ComponentActivity)

    LaunchedEffect(Unit) {
        vm.filterSpells(0..character.level, character.classIndex)
    }

    val filteredSpells by vm.filteredSpells.collectAsStateWithLifecycle()
    val spellsByLevel = remember(filteredSpells) { filteredSpells.groupBy { it.level } }

    val dndClass = remember(character) {
        ClassRegistry.createClass(character.classIndex, character.level)
    }

    val currentCantrips = remember { mutableStateListOf<SpellEntity>().apply { 
        addAll(filteredSpells.filter { it.level == 0 && character.cantrips.contains(it.index) })
    } }
    val currentSpells = remember { mutableStateListOf<SpellEntity>().apply {
        addAll(filteredSpells.filter { it.level > 0 && character.preparedSpells.contains(it.index) })
    } }

    // Update local lists when filtered spells load for the first time
    LaunchedEffect(filteredSpells) {
        if (currentCantrips.isEmpty()) {
            currentCantrips.addAll(filteredSpells.filter { it.level == 0 && character.cantrips.contains(it.index) })
        }
        if (currentSpells.isEmpty()) {
            currentSpells.addAll(filteredSpells.filter { it.level > 0 && character.preparedSpells.contains(it.index) })
        }
    }

    Dialog(onDissmiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Text(
                    text = "Prepare Spells",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(stringResource(R.string.max_cantrips_spells, dndClass?.cantrips?.getOrNull(character.level) ?: 0, dndClass?.getPreparedSpellsLimit(character) ?: 0))

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (dndClass != null) {
                        val maxCantrips = dndClass.cantrips.getOrNull(character.level) ?: 0
                        if (maxCantrips > 0) {
                            SpellSelector(
                                title = stringResource(R.string.cantrips),
                                maxSelections = maxCantrips,
                                availableSpells = spellsByLevel[0] ?: emptyList(),
                                selectedSpells = currentCantrips,
                                onToggleSpell = { spell ->
                                    if (currentCantrips.contains(spell)) {
                                        currentCantrips.remove(spell)
                                    } else if (currentCantrips.size < maxCantrips) {
                                        currentCantrips.add(spell)
                                    }
                                }
                            )
                        }

                        val maxSpells = dndClass.getPreparedSpellsLimit(character)
                        val availableSpells = maxSpells - currentSpells.size

                        if (maxSpells > 0) {
                            SpellSelector(
                                title = stringResource(R.string.level_1_spells),
                                maxSelections = availableSpells,
                                availableSpells = spellsByLevel[1] ?: emptyList(),
                                selectedSpells = currentSpells,
                                onToggleSpell = { spell ->
                                    if (currentSpells.contains(spell)) {
                                        currentSpells.remove(spell)
                                    } else if (currentSpells.size < maxSpells) {
                                        currentSpells.add(spell)
                                    }
                                }
                            )
                        }
                        
                        // If character is high enough level, show more spell selectors
                        for (lvl in 2..9) {
                            val availableForLvl = spellsByLevel[lvl] ?: emptyList()
                            if (availableForLvl.isNotEmpty()) {
                                SpellSelector(
                                    title = stringResource(R.string.level_x_spells, lvl),
                                    maxSelections = availableSpells,
                                    availableSpells = availableForLvl,
                                    selectedSpells = currentSpells,
                                    onToggleSpell = { spell ->
                                        if (currentSpells.contains(spell)) {
                                            currentSpells.remove(spell)
                                        } else if (currentSpells.size < maxSpells) {
                                            currentSpells.add(spell)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDissmiss) {
                        Text(stringResource(R.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        vm.updateCharacterSpells(
                            character = character,
                            newCantrips = currentCantrips.map { it.index },
                            newPreparedSpells = currentSpells.map { it.index }
                        )
                        onDissmiss()
                    }) {
                        Text(stringResource(R.string.save))
                    }
                }
            }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Row {
        Text("$label: ", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
        Text(value, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun FeaturesSection(char: Character, dndClass: DndClass) {
    val activeFeatures = remember(char, dndClass) {
        val baseFeatures = dndClass.baseFeatures.filter { it.levelRequired <= char.level }
        val subclass = char.subclassIndex?.let { index ->
            ClassRegistry.getSubclasses(char.classIndex).find {
                it.subclassName.lowercase().replace(" ", "-") == index
            }
        }
        val subclassFeatures = subclass?.features?.filter { it.levelRequired <= char.level } ?: emptyList()
        (baseFeatures + subclassFeatures).sortedBy { it.levelRequired }
    }

    if (activeFeatures.isNotEmpty()) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                "Class & Subclass Features",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            activeFeatures.forEach { feature ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                feature.name,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "Level ${feature.levelRequired}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            feature.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
