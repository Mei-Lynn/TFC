package com.utad.tfg.presentation

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.utad.tfg.local.entities.Character
import com.utad.tfg.local.entities.SpellEntity
import com.utad.tfg.model.Ability
import com.utad.tfg.model.classes.ClassFeature
import com.utad.tfg.model.classes.ClassRegistry
import com.utad.tfg.model.classes.Subclass
import com.utad.tfg.model.classes.Class as DndClass
import androidx.compose.ui.res.stringResource
import com.utad.tfg.R

private val KNOWN_CASTERS = setOf("bard", "sorcerer", "warlock", "ranger")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelUpScreen(onComplete: () -> Unit = {}) {
    val vm = hiltViewModel<MainViewModel>(LocalContext.current as ComponentActivity)
    val character by vm.selectedCharacter.collectAsStateWithLifecycle()

    character?.let { char ->
        val newLevel = char.level + 1
        val dndClass = remember(char) { ClassRegistry.createClass(char.classIndex, char.level) }
        val dndClassNewLevel = remember(char) { ClassRegistry.createClass(char.classIndex, newLevel) }

        if (dndClass == null || dndClassNewLevel == null) return@let

        // ─── Determine which steps to show ───
        val isAsiLevel = newLevel in dndClass.asiLevels
        val needsSubclass = char.subclassIndex == null &&
                dndClass.subclassProgression.isNotEmpty() &&
                newLevel == dndClass.subclassProgression.first()
        val isKnownCaster = char.classIndex in KNOWN_CASTERS

        val oldCantripCount = dndClass.cantrips.getOrElse(char.level) { 0 }
        val newCantripCount = dndClass.cantrips.getOrElse(newLevel) { 0 }
        val gainsCantrips = newCantripCount > oldCantripCount

        val oldSpellsKnown = dndClass.getPreparedSpellsLimit(char)
        val newSpellsKnown = dndClassNewLevel.getPreparedSpellsLimit(char.copy(level = newLevel))
        val gainsSpells = isKnownCaster && newSpellsKnown > oldSpellsKnown

        val hasSpellStep = gainsCantrips || gainsSpells || isKnownCaster

        // Build ordered step list
        val steps = remember(isAsiLevel, needsSubclass, hasSpellStep) {
            buildList {
                add(LevelUpStep.HP)
                if (isAsiLevel) add(LevelUpStep.ASI)
                if (needsSubclass) add(LevelUpStep.SUBCLASS)
                if (hasSpellStep) add(LevelUpStep.SPELLS)
                add(LevelUpStep.SUMMARY)
            }
        }

        var currentStepIndex by remember { mutableIntStateOf(0) }
        val currentStep = steps[currentStepIndex]

        // ─── Shared state across steps ───
        val conMod = Ability.calculateModifier(char.constitution)
        val hpGain = remember { dndClass.hitDie / 2 + 1 + conMod }

        val asiChoices = remember { mutableStateMapOf<Ability, Int>() }

        var selectedSubclass by remember { mutableStateOf<Subclass?>(null) }

        // Cantrips: start with current cantrips
        val currentCantrips = remember { mutableStateListOf<String>().apply { addAll(char.cantrips) } }
        // Spells: start with current prepared spells
        val currentSpells = remember { mutableStateListOf<String>().apply { addAll(char.preparedSpells) } }

        // Spell swap tracking
        var spellToSwap by remember { mutableStateOf<String?>(null) }
        var hasSwapped by remember { mutableStateOf(false) }

        // Load spells for selection
        val filteredSpells by vm.filteredSpells.collectAsStateWithLifecycle()

        LaunchedEffect(char.classIndex) {
            val maxSpellLevel = vm.getMaxSpellLevelForClass(char.classIndex, newLevel)
            if (maxSpellLevel > 0 || gainsCantrips) {
                vm.filterSpells(0..maxSpellLevel, char.classIndex)
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.level_up_title, char.name)) },
                    navigationIcon = {
                        IconButton(onClick = onComplete) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cancel))
                        }
                    }
                )
            },
            bottomBar = {
                LevelUpBottomBar(
                    currentStepIndex = currentStepIndex,
                    totalSteps = steps.size,
                    canAdvance = when (currentStep) {
                        LevelUpStep.ASI -> asiChoices.values.sum() == 2
                        LevelUpStep.SUBCLASS -> selectedSubclass != null
                        else -> true
                    },
                    isLastStep = currentStepIndex == steps.lastIndex,
                    onBack = { currentStepIndex-- },
                    onNext = { currentStepIndex++ },
                    onConfirm = {
                        vm.levelUp(
                            character = char,
                            hpGain = hpGain,
                            abilityIncreases = asiChoices.toMap(),
                            newSubclassIndex = selectedSubclass?.subclassName
                                ?.lowercase()?.replace(" ", "-"),
                            newCantrips = currentCantrips.toList(),
                            newPreparedSpells = currentSpells.toList()
                        )
                        onComplete()
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Step indicator
                StepIndicator(steps = steps, currentIndex = currentStepIndex)

                Spacer(modifier = Modifier.height(8.dp))

                when (currentStep) {
                    LevelUpStep.HP -> HpGainStep(
                        char = char,
                        dndClass = dndClass,
                        hpGain = hpGain,
                        newLevel = newLevel
                    )

                    LevelUpStep.ASI -> AsiStep(
                        char = char,
                        asiChoices = asiChoices
                    )

                    LevelUpStep.SUBCLASS -> SubclassStep(
                        classIndex = char.classIndex,
                        selectedSubclass = selectedSubclass,
                        onSubclassSelected = { selectedSubclass = it }
                    )

                    LevelUpStep.SPELLS -> SpellsStep(
                        char = char,
                        dndClass = dndClass,
                        newLevel = newLevel,
                        gainsCantrips = gainsCantrips,
                        newCantripCount = newCantripCount,
                        gainsSpells = gainsSpells,
                        newSpellCount = newSpellsKnown - oldSpellsKnown,
                        totalSpellsAllowed = newSpellsKnown,
                        isKnownCaster = isKnownCaster,
                        filteredSpells = filteredSpells,
                        currentCantrips = currentCantrips,
                        currentSpells = currentSpells,
                        spellToSwap = spellToSwap,
                        hasSwapped = hasSwapped,
                        onInitiateSwap = { spellToSwap = it },
                        onCancelSwap = { spellToSwap = null },
                        onConfirmSwap = { oldSpell, newSpell ->
                            currentSpells.remove(oldSpell)
                            currentSpells.add(newSpell)
                            spellToSwap = null
                            hasSwapped = true
                        },
                        maxSpellLevel = vm.getMaxSpellLevelForClass(char.classIndex, newLevel)
                    )

                    LevelUpStep.SUMMARY -> SummaryStep(
                        char = char,
                        newLevel = newLevel,
                        hpGain = hpGain,
                        asiChoices = asiChoices,
                        selectedSubclass = selectedSubclass,
                        dndClass = dndClass,
                        dndClassNewLevel = dndClassNewLevel,
                        currentCantrips = currentCantrips,
                        currentSpells = currentSpells,
                        originalCantrips = char.cantrips,
                        originalSpells = char.preparedSpells
                    )
                }
            }
        }
    } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

private enum class LevelUpStep(val label: String) {
    HP("HP"),
    ASI("ASI"),
    SUBCLASS("Subclass"),
    SPELLS("Spells"),
    SUMMARY("Summary")
}

@Composable
private fun StepIndicator(steps: List<LevelUpStep>, currentIndex: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, step ->
            val isActive = index == currentIndex
            val isDone = index < currentIndex
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = when {
                        isActive -> MaterialTheme.colorScheme.primary
                        isDone -> MaterialTheme.colorScheme.primaryContainer
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (isDone) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        } else {
                            Text(
                                "${index + 1}",
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isActive) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    step.label,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun LevelUpBottomBar(
    currentStepIndex: Int,
    totalSteps: Int,
    canAdvance: Boolean,
    isLastStep: Boolean,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onConfirm: () -> Unit
) {
    Surface(tonalElevation = 3.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (currentStepIndex > 0) {
                OutlinedButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(R.string.back))
                }
            } else {
                Spacer(modifier = Modifier.width(1.dp))
            }

            if (isLastStep) {
                Button(onClick = onConfirm) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(R.string.confirm_level_up))
                }
            } else {
                Button(onClick = onNext, enabled = canAdvance) {
                    Text(stringResource(R.string.next))
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
private fun HpGainStep(
    char: Character,
    dndClass: DndClass,
    hpGain: Int,
    newLevel: Int
) {
    val conMod = Ability.calculateModifier(char.constitution)
    val conModText = if (conMod >= 0) "+$conMod" else "$conMod"

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                "Level ${char.level} → $newLevel",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            HorizontalDivider()
            Text(stringResource(R.string.hit_die, dndClass.hitDie), style = MaterialTheme.typography.bodyLarge)
            Text(
                "Average roll: ${dndClass.hitDie / 2 + 1}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                "Constitution modifier: $conModText",
                style = MaterialTheme.typography.bodyMedium
            )
            HorizontalDivider()
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "HP Gain: ",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "+$hpGain",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                "${char.maxHp} → ${char.maxHp + hpGain} HP",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }

    // Proficiency bonus change
    val oldProfBonus = 2 + (char.level - 1) / 4
    val newProfBonus = 2 + (newLevel - 1) / 4
    if (newProfBonus > oldProfBonus) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Proficiency Bonus increases!",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "+$oldProfBonus → +$newProfBonus",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

@Composable
private fun AsiStep(
    char: Character,
    asiChoices: MutableMap<Ability, Int>
) {
    Text(
        "Ability Score Improvement",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold
    )
    Text(
        "Choose how to distribute your ability score increase.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(8.dp))

    Ability.allAbilities.forEach { ability ->
        val currentScore = when (ability) {
            Ability.Strength -> char.strength
            Ability.Dexterity -> char.dexterity
            Ability.Constitution -> char.constitution
            Ability.Intelligence -> char.intelligence
            Ability.Wisdom -> char.wisdom
            Ability.Charisma -> char.charisma
        }
        val increase = asiChoices[ability] ?: 0
        val newScore = currentScore + increase

        val totalAssigned = asiChoices.values.sum()
        val canIncrease = increase < 2 && newScore < 20 && totalAssigned < 2

        AsiAbilityRow(
            ability = ability,
            currentScore = currentScore,
            increase = increase,
            canIncrease = canIncrease,
            canDecrease = increase > 0,
            onIncrease = { asiChoices[ability] = increase + 1 },
            onDecrease = {
                if (increase <= 1) asiChoices.remove(ability)
                else asiChoices[ability] = increase - 1
            }
        )
    }

    // Hint
    val remaining = 2 - asiChoices.values.sum()
    if (remaining > 0) {
        Text(
            "$remaining point${if (remaining > 1) "s" else ""} remaining",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun AsiAbilityRow(
    ability: Ability,
    currentScore: Int,
    increase: Int,
    canIncrease: Boolean,
    canDecrease: Boolean,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    val newScore = currentScore + increase
    val mod = Ability.calculateModifier(newScore)
    val modText = if (mod >= 0) "+$mod" else "$mod"

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = if (increase > 0) CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ) else CardDefaults.cardColors()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(ability.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                Text(
                    if (increase > 0) "$currentScore → $newScore ($modText)"
                    else "$currentScore ($modText)",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                FilledIconButton(
                    onClick = onDecrease,
                    enabled = canDecrease,
                    modifier = Modifier.size(36.dp)
                ) {
                    Text("−", style = MaterialTheme.typography.titleMedium)
                }
                Text(
                    if (increase > 0) "+$increase" else "—",
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .widthIn(min = 28.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                FilledIconButton(
                    onClick = onIncrease,
                    enabled = canIncrease,
                    modifier = Modifier.size(36.dp)
                ) {
                    Text("+", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@Composable
private fun SubclassStep(
    classIndex: String,
    selectedSubclass: Subclass?,
    onSubclassSelected: (Subclass) -> Unit
) {
    val subclasses = remember(classIndex) { ClassRegistry.getSubclasses(classIndex) }

    Text(
        "Choose Your Subclass",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold
    )
    Text(
        "This choice will shape your abilities going forward.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(8.dp))

    subclasses.forEach { subclass ->
        val isSelected = selectedSubclass == subclass
        val firstFeature = subclass.features.firstOrNull()

        Card(
            onClick = { onSubclassSelected(subclass) },
            modifier = Modifier.fillMaxWidth(),
            colors = if (isSelected) CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) else CardDefaults.cardColors()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isSelected) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(20.dp)
                                .padding(end = 4.dp)
                        )
                    }
                    Text(
                        subclass.subclassName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (firstFeature != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        firstFeature.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        firstFeature.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 3
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SpellsStep(
    char: Character,
    dndClass: DndClass,
    newLevel: Int,
    gainsCantrips: Boolean,
    newCantripCount: Int,
    gainsSpells: Boolean,
    newSpellCount: Int,
    totalSpellsAllowed: Int,
    isKnownCaster: Boolean,
    filteredSpells: List<SpellEntity>,
    currentCantrips: MutableList<String>,
    currentSpells: MutableList<String>,
    spellToSwap: String?,
    hasSwapped: Boolean,
    onInitiateSwap: (String) -> Unit,
    onCancelSwap: () -> Unit,
    onConfirmSwap: (String, String) -> Unit,
    maxSpellLevel: Int
) {
    val spellsByLevel = remember(filteredSpells) { filteredSpells.groupBy { it.level } }

    Text(
        "Spells",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold
    )

    if (gainsCantrips) {
        val availableCantrips = (spellsByLevel[0] ?: emptyList())

        Text(
            "New Cantrips Available",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            "Choose $newCantripCount total cantrips (you currently know ${char.cantrips.size}).",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        SpellSelector(
            title = stringResource(R.string.cantrips),
            maxSelections = newCantripCount,
            availableSpells = availableCantrips,
            selectedSpells = availableCantrips.filter { currentCantrips.contains(it.index) },
            onToggleSpell = { spell ->
                if (currentCantrips.contains(spell.index)) {
                    currentCantrips.remove(spell.index)
                } else if (currentCantrips.size < newCantripCount) {
                    currentCantrips.add(spell.index)
                }
            }
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
    }

    if (gainsSpells) {
        Text(
            "Learn New Spells",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            "You can know up to $totalSpellsAllowed spells (currently ${char.preparedSpells.size}). Pick $newSpellCount new spell${if (newSpellCount > 1) "s" else ""}.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        for (lvl in 1..maxSpellLevel) {
            val availableForLevel = (spellsByLevel[lvl] ?: emptyList())
            if (availableForLevel.isNotEmpty()) {
                SpellSelector(
                    title = stringResource(R.string.level_x_spells, lvl),
                    maxSelections = totalSpellsAllowed,
                    availableSpells = availableForLevel,
                    selectedSpells = availableForLevel.filter { currentSpells.contains(it.index) },
                    allowSwaps = !isKnownCaster,
                    onToggleSpell = { spell ->
                        if (currentSpells.contains(spell.index)) {
                            currentSpells.remove(spell.index)
                        } else if (currentSpells.size < totalSpellsAllowed) {
                            currentSpells.add(spell.index)
                        }
                    }
                )
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
    }

    if (isKnownCaster) {
        Text(
            "Spell Swap",
            style = MaterialTheme.typography.titleMedium
        )

        if (hasSwapped) {
            Text(
                "You've already swapped a spell this level-up.",
                style = MaterialTheme.typography.bodySmall,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else if (spellToSwap != null) {
            // Choosing replacement
            val swapSpell = filteredSpells.find { it.index == spellToSwap }
            val swapLevel = swapSpell?.level ?: 1

            Text(
                "Swapping: ${swapSpell?.name ?: spellToSwap}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
            Text(
                "Choose a replacement spell of level $swapLevel or lower:",
                style = MaterialTheme.typography.bodySmall
            )

            for (lvl in 1..minOf(swapLevel, maxSpellLevel)) {
                val replacements = (spellsByLevel[lvl] ?: emptyList())
                    .filter { !currentSpells.contains(it.index) && it.index != spellToSwap }
                if (replacements.isNotEmpty()) {
                    SpellSelector(
                        title = stringResource(R.string.level_x, lvl),
                        maxSelections = 1,
                        availableSpells = replacements,
                        selectedSpells = emptyList(),
                        onToggleSpell = { replacement ->
                            onConfirmSwap(spellToSwap, replacement.index)
                        }
                    )
                }
            }

            TextButton(onClick = onCancelSwap) {
                Text(stringResource(R.string.cancel_swap))
            }
        } else {
            Text(
                "You may swap one known spell for another of a level you can cast.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Show current spells as swappable
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                currentSpells.forEach { spellIndex ->
                    val spell = filteredSpells.find { it.index == spellIndex }
                    AssistChip(
                        onClick = { onInitiateSwap(spellIndex) },
                        label = { Text(spell?.name ?: spellIndex) },
                        leadingIcon = {
                            Icon(Icons.Default.SwapHoriz, contentDescription = stringResource(R.string.swap), modifier = Modifier.size(18.dp))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SummaryStep(
    char: Character,
    newLevel: Int,
    hpGain: Int,
    asiChoices: Map<Ability, Int>,
    selectedSubclass: Subclass?,
    dndClass: DndClass,
    dndClassNewLevel: DndClass,
    currentCantrips: List<String>,
    currentSpells: List<String>,
    originalCantrips: List<String>,
    originalSpells: List<String>
) {
    Text(
        "Level-Up Summary",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SummaryRow("Level", "${char.level} → $newLevel")
            SummaryRow("HP", "${char.maxHp} → ${char.maxHp + hpGain} (+$hpGain)")

            val oldProfBonus = 2 + (char.level - 1) / 4
            val newProfBonus = 2 + (newLevel - 1) / 4
            if (newProfBonus > oldProfBonus) {
                SummaryRow("Proficiency Bonus", "+$oldProfBonus → +$newProfBonus")
            }

            if (asiChoices.isNotEmpty()) {
                val asiText = asiChoices.entries.joinToString(", ") { "${it.key.abbreviation} +${it.value}" }
                SummaryRow("Ability Scores", asiText)
            }

            if (selectedSubclass != null) {
                SummaryRow("Subclass", selectedSubclass.subclassName)
            }

            val newCantripIndices = currentCantrips.filter { it !in originalCantrips }
            if (newCantripIndices.isNotEmpty()) {
                SummaryRow("New Cantrips", "${newCantripIndices.size} learned")
            }

            val newSpellIndices = currentSpells.filter { it !in originalSpells }
            val removedSpellIndices = originalSpells.filter { it !in currentSpells }
            if (newSpellIndices.isNotEmpty()) {
                SummaryRow("New Spells", "${newSpellIndices.size} learned")
            }
            if (removedSpellIndices.isNotEmpty()) {
                SummaryRow("Swapped Out", "${removedSpellIndices.size} spell${if (removedSpellIndices.size > 1) "s" else ""}")
            }
        }
    }

    val newBaseFeatures = dndClass.baseFeatures.filter { it.levelRequired == newLevel }
    val subclass = selectedSubclass ?: char.subclassIndex?.let { index ->
        ClassRegistry.getSubclasses(char.classIndex).find {
            it.subclassName.lowercase().replace(" ", "-") == index
        }
    }
    val newSubclassFeatures = subclass?.features?.filter { it.levelRequired == newLevel } ?: emptyList()
    val allNewFeatures = newBaseFeatures + newSubclassFeatures

    if (allNewFeatures.isNotEmpty()) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "New Features Unlocked",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        allNewFeatures.forEach { feature ->
            FeatureCard(feature)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        Text(value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun FeatureCard(feature: ClassFeature) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                feature.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                feature.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }
}