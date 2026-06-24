package com.utad.tfg.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.utad.tfg.model.Ability
import com.utad.tfg.model.ArmorType
import com.utad.tfg.model.DndRace
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.equipment.Armor
import com.utad.tfg.model.equipment.Equipment
import com.utad.tfg.model.equipment.Weapon
import com.utad.tfg.model.equipment.WeaponProperty
import com.utad.tfg.model.classes.Class as DndClass

import androidx.compose.ui.tooling.preview.Preview
import com.utad.tfg.local.entities.Character
import com.utad.tfg.local.entities.SpellEntity
import com.utad.tfg.model.classes.barbarian.Barbarian
import com.utad.tfg.ui.theme.TFGTheme
import androidx.compose.ui.res.stringResource
import com.utad.tfg.R
import java.io.File
import java.util.UUID

@Composable
fun CharCreateScreen(
    onCharacterCreated: () -> Unit = {}
) {
    val vm = hiltViewModel<MainViewModel>(LocalContext.current as ComponentActivity)
    val races by vm.races.collectAsState()
    val classes by vm.classes.collectAsState()
    val subraces by vm.subraces.collectAsState()
    val subclasses by vm.subclasses.collectAsState()
    val filteredSpells by vm.filteredSpells.collectAsState()
    val weapons by vm.weapons.collectAsState()
    val armors by vm.armors.collectAsState()
    val context = LocalContext.current

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            val internalUri = copyImageToInternalStorage(context, it)
            selectedImageUri = internalUri
        }
    }

    CharCreateContent(
        races = races,
        classes = classes,
        subraces = subraces,
        subclasses = subclasses,
        filteredSpells = filteredSpells,
        weapons = weapons,
        armors = armors,
        imageUri = selectedImageUri,
        onPickImage = {
            imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        },
        onRaceSelected = { vm.getSubraces(it) },
        onClassSelected = { vm.getSubclasses(it); vm.filterSpells(0..1, it) },
        onSaveCharacter = { name, race, subrace, dndClass, subclass, scores, cantrips, spells, mainHand, offHand, armor ->
            vm.saveCharacter(name, race, subrace, dndClass, subclass, scores, cantrips, spells, mainHand, offHand, armor, imgUri = selectedImageUri?.toString())
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
    weapons: List<Weapon>,
    armors: List<Armor>,
    imageUri: Uri? = null,
    onPickImage: () -> Unit = {},
    onRaceSelected: (String) -> Unit,
    onClassSelected: (String) -> Unit,
    onSaveCharacter: (String, DndRace, DndRace?, DndClass, Subclass?, Map<Ability, Int>, List<SpellEntity>, List<SpellEntity>, String?, String?, String?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedRace by remember { mutableStateOf<DndRace?>(null) }
    var selectedSubrace by remember { mutableStateOf<DndRace?>(null) }
    var selectedClass by remember { mutableStateOf<DndClass?>(null) }
    var selectedSubclass by remember { mutableStateOf<Subclass?>(null) }

    var selectedMainHand by remember { mutableStateOf<Weapon?>(null) }
    var selectedOffHand by remember { mutableStateOf<Equipment?>(null) }
    var selectedArmor by remember { mutableStateOf<Armor?>(null) }

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

    val chosenCantrips = remember { mutableStateListOf<SpellEntity>() }
    val chosenSpells = remember { mutableStateListOf<SpellEntity>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(stringResource(R.string.character_creation), style = MaterialTheme.typography.headlineMedium)

        // Character Image Picker
        Box(
            modifier = Modifier
                .size(96.dp)
                .align(Alignment.CenterHorizontally)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .clickable { onPickImage() },
            contentAlignment = Alignment.Center
        ) {
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = stringResource(R.string.character_name),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    Icons.Default.Person,
                    contentDescription = stringResource(R.string.add_character),
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Text(
            text = if (imageUri != null) "" else "Tap to add image",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        // Nombre
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(stringResource(R.string.character_name)) },
            modifier = Modifier.fillMaxWidth()
        )

        // Raza
        DropdownSelector(
            label = stringResource(R.string.race),
            options = races,
            selectedOption = selectedRace,
            optionName = { it.raceName },
            onOptionSelected = {
                selectedRace = it
                selectedSubrace = null
                it?.let { onRaceSelected(it.raceName) }
            }
        )
        ItemImageSlot(selectedRace?.imgUri)

        // Subraza
        if (subraces.isNotEmpty()) {
            DropdownSelector(
                label = stringResource(R.string.subrace),
                options = subraces,
                selectedOption = selectedSubrace,
                optionName = { it.subraceName ?: it.raceName },
                onOptionSelected = { selectedSubrace = it }
            )
            //ItemImageSlot(selectedSubrace?.imgUri)
        }

        // Clase
        DropdownSelector(
            label = stringResource(R.string.class_name),
            options = classes,
            selectedOption = selectedClass,
            optionName = { it.className },
            onOptionSelected = {
                selectedClass = it
                selectedSubclass = null
                chosenCantrips.clear()
                chosenSpells.clear()
                it?.let { onClassSelected(it.classIndex) }
            }
        )
        ItemImageSlot(selectedClass?.imgUri)

        // Subclase
        if (subclasses.isNotEmpty() && selectedClass?.subclassProgression?.minOrNull() == 1) {
            DropdownSelector(
                label = stringResource(R.string.subclass),
                options = subclasses,
                selectedOption = selectedSubclass,
                optionName = { it.subclassName },
                onOptionSelected = { selectedSubclass = it }
            )
        }

        EquipmentEditor(
            weaponsSource = weapons,
            selectedMainHand = selectedMainHand,
            onMainHandChange = { selectedMainHand = it },
            armorsSource = armors,
            selectedOffHand = selectedOffHand,
            onOffHandChange = { selectedOffHand = it },
            selectedArmor = selectedArmor,
            onArmorChange = { selectedArmor = it }
        )

        //Distribución de puntos de habilidad
        Text(stringResource(R.string.ability_scores_points, remainingPoints, totalPoints), style = MaterialTheme.typography.titleLarge)

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

        Text(stringResource(R.string.prepared_spells), style = MaterialTheme.typography.titleLarge)
        if (selectedClass != null && filteredSpells.isNotEmpty()) {
            val spellsByLevel = filteredSpells.groupBy { it.level }

            val maxCantrips = selectedClass!!.cantrips[1]

            SpellSelector(
                "Cantrips (Pick $maxCantrips)",
                maxCantrips,
                spellsByLevel[0] ?: emptyList(),
                chosenCantrips
            ) {
                if (chosenCantrips.contains(it)) {
                    chosenCantrips.remove(it)
                } else if (chosenCantrips.size < maxCantrips) {
                    chosenCantrips.add(it)
                }
            }

            val maxSpells = selectedClass!!.getPreparedSpellsLimit(Character(
                level = 1,
                strength = abilityScores[Ability.Strength] ?: 10,
                intelligence = abilityScores[Ability.Intelligence] ?: 10,
                wisdom = abilityScores[Ability.Wisdom] ?: 10,
                charisma = abilityScores[Ability.Charisma] ?: 10,
                dexterity = abilityScores[Ability.Dexterity] ?: 10,
                constitution = abilityScores[Ability.Constitution] ?: 10,
                name = "",
                raceIndex = "",
                classIndex = "",
                maxHp = 999,
                currentHp = 999,
                armorClass = 999,
            ))

            SpellSelector(
                "Level 1 Spells (Pick $maxSpells)",
                maxSpells,
                spellsByLevel[1] ?: emptyList(),
                chosenSpells
            ) {
                if (chosenSpells.contains(it)) {
                    chosenSpells.remove(it)
                } else if (chosenSpells.size < maxSpells) {
                    chosenSpells.add(it)
                }
            }
        } else if (selectedClass != null) {
            Text(stringResource(R.string.no_spells_available), style = MaterialTheme.typography.bodyMedium)
        } else {
            Text(stringResource(R.string.spells_selection_info), style = MaterialTheme.typography.bodyMedium)
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
                    abilityScores,
                    chosenCantrips.toList(),
                    chosenSpells.toList(),
                    selectedMainHand?.weaponIndex,
                    if (selectedMainHand?.properties?.contains(WeaponProperty.TWO_HANDED) == false) selectedOffHand?.index else null,
                    selectedArmor?.armorIndex
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = name.isNotBlank() && selectedRace != null && selectedClass != null && remainingPoints == 0 && modPlus1 != null && modPlus2 != null
        ) {
            Text(stringResource(R.string.create_character))
        }
    }
}
@Composable
fun EquipmentEditor(
    weaponsSource: List<Weapon>,
    selectedMainHand: Weapon?,
    onMainHandChange: (Weapon?) -> Unit,
    armorsSource: List<Armor>,
    selectedOffHand: Equipment?,
    onOffHandChange: (Equipment?) -> Unit,
    selectedArmor: Armor?,
    onArmorChange: (Armor?) -> Unit
) {
    Text(stringResource(R.string.equipment), style = MaterialTheme.typography.titleLarge)

    val weapons : MutableList<Weapon?> = weaponsSource.toMutableList()
    weapons.add(null)

    val armors : MutableList<Armor?> = armorsSource.toMutableList()
    armors.add(null)

    DropdownSelector(
        label = stringResource(R.string.main_hand),
        options = weapons,
        selectedOption = selectedMainHand,
        optionName = { it?.weaponName ?: "Nothing" },
        onOptionSelected = { onMainHandChange(it) }
    )
    ItemImageSlot(selectedMainHand?.imgUri)

    if (selectedMainHand?.properties?.contains(WeaponProperty.TWO_HANDED) == false) {
        val offHandOptions = remember(selectedMainHand, weapons, armors) {
            val lightWeapons = weapons.filter { it?.properties?.contains(WeaponProperty.LIGHT) == true }
            val shields = armors.filter { it?.armorType == ArmorType.Shields }
            lightWeapons + shields
        }

        DropdownSelector(
            label = stringResource(R.string.off_hand),
            options = offHandOptions,
            selectedOption = selectedOffHand,
            optionName = {
                when (it) {
                    is Weapon -> it.weaponName
                    is Armor -> it.armorName
                    else -> "Unknown"
                }
            },
            onOptionSelected = { onOffHandChange(it) }
        )
        ItemImageSlot(selectedOffHand?.imgUri)
    }

    DropdownSelector(
        label = stringResource(R.string.armor),
        options = armors.filter { armor -> armor?.armorType != ArmorType.Shields },
        selectedOption = selectedArmor,
        optionName = { it?.armorName ?: "Nothing" },
        onOptionSelected = { onArmorChange(it) }
    )
    ItemImageSlot(selectedArmor?.imgUri)
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
                Icon(Icons.Default.Remove, contentDescription = stringResource(R.string.decrease))
            }

            Text("${score + if (modPlus1) 1 else 0 + if (modPlus2) 2 else 0}", modifier = Modifier
                .padding(horizontal = 8.dp)
                .widthIn(32.dp), fontSize = 18.sp)

            Text("(${Ability.calculateModifier(score)})")

            IconButton(onClick = onIncrease, enabled = canIncrease) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.increase))
            }
        }

        // +1 Innato
        Checkbox(modPlus1, onModPlus1Click)

        // +2 Innato
        Checkbox(modPlus2,onModPlus2Click)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpellSelector(
    title: String,
    maxSelections: Int,
    availableSpells: List<SpellEntity>,
    selectedSpells: Collection<SpellEntity>,
    allowSwaps : Boolean = true,
    onToggleSpell: (SpellEntity) -> Unit,
) {
    val originalSelection = remember { selectedSpells.toList() }

    Column {
        Text(title, style = MaterialTheme.typography.titleSmall)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            availableSpells.forEach { spell ->
                val isSelected = selectedSpells.contains(spell)
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        if (allowSwaps) {
                            if (selectedSpells.size < maxSelections || isSelected)
                                onToggleSpell(spell)
                        } else if (!originalSelection.contains(spell))
                            if (selectedSpells.size < maxSelections || isSelected)
                                onToggleSpell(spell)
                    },
                    label = { Text(spell.name) },
                    leadingIcon = if (isSelected) {
                        { Icon(Icons.Default.Check, contentDescription = stringResource(R.string.spell_selected), modifier = Modifier.size(18.dp)) }
                    } else null
                )
            }
        }
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

@Composable
fun ItemImageSlot(imgUri: String?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, MaterialTheme.colorScheme.outline, androidx.compose.foundation.shape.RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (!imgUri.isNullOrEmpty()) {
            AsyncImage(
                model = imgUri,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = "No image",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.image_placeholder),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CharCreatePreview() {
    TFGTheme {
        CharCreateContent(
            races = listOf(DndRace.Human),
            classes = listOf(Barbarian()),
            subraces = emptyList(),
            subclasses = emptyList(),
            filteredSpells = emptyList(),
            weapons = emptyList(),
            armors = emptyList(),
            onRaceSelected = {},
            onClassSelected = {},
            onSaveCharacter = { _, _, _, _, _, _, _, _, _, _, _ -> }
        )
    }
}

/**
 * Copies an image from a content URI to the app's internal storage.
 * Resizes to max 1024px on the longest side to save space.
 * Returns the file:// URI of the internal copy.
 */
fun copyImageToInternalStorage(context: Context, sourceUri: Uri): Uri? {
    return try {
        val dir = File(context.filesDir, "character_images")
        if (!dir.exists()) dir.mkdirs()
        val filename = "char_${UUID.randomUUID()}.jpg"
        val destFile = File(dir, filename)

        context.contentResolver.openInputStream(sourceUri)?.use { input ->
            // Decode and resize
            val original = BitmapFactory.decodeStream(input)
            val maxDimension = 1024
            val scaled = if (original.width > maxDimension || original.height > maxDimension) {
                val ratio = maxDimension.toFloat() / maxOf(original.width, original.height)
                Bitmap.createScaledBitmap(
                    original,
                    (original.width * ratio).toInt(),
                    (original.height * ratio).toInt(),
                    true
                )
            } else original

            destFile.outputStream().use { output ->
                scaled.compress(Bitmap.CompressFormat.JPEG, 85, output)
            }
            if (scaled !== original) scaled.recycle()
            original.recycle()
        }

        Uri.fromFile(destFile)
    } catch (e: Exception) {
        Log.e("CharCreate", "Failed to copy image", e)
        null
    }
}
