package com.utad.tfg.presentation

import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Person
import com.utad.tfg.remote.MonsterAction
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.utad.tfg.R
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items as lazyItems
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.utad.tfg.model.Ability
import com.utad.tfg.local.entities.Enemy


@Composable
fun BestiaryScreen() {
    val vm = hiltViewModel<MainViewModel>(LocalContext.current as ComponentActivity)
    val monsters by vm.monsters.collectAsStateWithLifecycle()
    val localEnemies by vm.localEnemies.collectAsStateWithLifecycle()
    var searchString by remember { mutableStateOf("") }
    
    var showDialog by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        vm.fetchMonsters()
    }

    if (showDialog) {
        MonsterInfoDialog(
            onDismiss = { showDialog = false; vm.emptyMonsterDetails() },
            index = selectedIndex
        )
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(8.dp))
        CustomSearchBar(
            placeholder = stringResource(R.string.bestiary_searchbar),
            onSearchTextChanged = { searchString = it }
        )

        val filteredMonsters = monsters.filter {
            it.name.contains(searchString, ignoreCase = true)
        }

        if (monsters.isEmpty() && localEnemies.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (filteredMonsters.isEmpty() && monsters.isNotEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No matches")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(filteredMonsters) { monster ->
                    val isDownloaded = localEnemies.any { it.index == monster.index }
                    OnlineMonsterCard(
                        name = monster.name,
                        isDownloaded = isDownloaded,
                        onDownloadClick = { vm.downloadMonster(monster.index) },
                        onDeleteClick = { vm.deleteSavedMonster(monster.index) },
                        onInfoClick = {
                            selectedIndex = it
                            showDialog = true
                        },
                        index = monster.index,
                    )
                }
            }
        }
    }
}

@Composable
fun OfflineMonsterCard(enemy: Enemy, onInfoClick: (String) -> Unit, onDeleteClick: () -> Unit,) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onInfoClick(enemy.index) }
    ) {

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(0.7f),
                contentAlignment = Alignment.CenterStart
            ) {
                // Invisible text to reserve 3 lines of space
                Text(
                    text = "",
                    style = MaterialTheme.typography.titleMedium,
                    minLines = 3
                )
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = enemy.name,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = enemy.type,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            IconButton(
                onClick = onDeleteClick,
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.download_delete)
                )
            }
        }
    }
}

@Composable
fun OnlineMonsterCard(
    name: String,
    index: String,
    isDownloaded: Boolean,
    onDownloadClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onInfoClick: (index: String) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {onInfoClick(index)}
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                // Invisible text to reserve 3 lines of space
                Text(
                    text = "",
                    style = MaterialTheme.typography.titleMedium,
                    minLines = 3
                )
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            IconButton(
                onClick = if (isDownloaded) onDeleteClick else onDownloadClick,
            ) {
                Icon(
                    imageVector = if (isDownloaded) Icons.Default.Delete else Icons.Default.Download,
                    contentDescription = stringResource(R.string.download_delete)
                )
            }
        }
    }
}

@Composable
fun MonsterInfoDialog(onDismiss: () -> Unit, index: String) {
    val vm = hiltViewModel<MainViewModel>(LocalContext.current as ComponentActivity)
    val monster by vm.monsterDetails.collectAsStateWithLifecycle()

    LaunchedEffect(index) {
        vm.fetchMonsterDetails(index)
    }

    Dialog(onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            if (monster == null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(stringResource(R.string.loading_monster_info))
                }
            } else {
                val m = monster!!
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = m.name,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${m.size} ${m.type}, ${m.alignment}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        StatItem("AC", "${m.armorClass.firstOrNull()?.value ?: 10}")
                        StatItem("HP", "${m.hitPoints}")
                        StatItem("CR", "${m.challengeRating}")
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        AbilityScore("STR", m.strength)
                        AbilityScore("DEX", m.dexterity)
                        AbilityScore("CON", m.constitution)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        AbilityScore("INT", m.intelligence)
                        AbilityScore("WIS", m.wisdom)
                        AbilityScore("CHA", m.charisma)
                    }

                    if (!m.actions.isNullOrEmpty()) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Text(text = "Actions", style = MaterialTheme.typography.titleMedium)
                        m.actions.forEach { action ->
                            ExpandableActionItem(action)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExpandableActionItem(action: MonsterAction) {
    var expanded by remember { mutableStateOf(false) }
    var isExpandable by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (isExpandable) Modifier.clickable { expanded = !expanded } else Modifier)
            .padding(vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = action.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            if (isExpandable) {
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand"
                )
            }
        }
        Text(
            text = action.desc,
            style = MaterialTheme.typography.bodySmall,
            maxLines = if (expanded) Int.MAX_VALUE else 1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResult ->
                if (!expanded && textLayoutResult.hasVisualOverflow) {
                    isExpandable = true
                }
            },
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun AbilityScore(label: String, score: Int) {
    val modifier = Ability.calculateModifier(score)
    val modText = if (modifier >= 0) "+$modifier" else "$modifier"
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
        Text(text = "$score ($modText)", style = MaterialTheme.typography.bodyMedium)
    }
}


@Composable
fun CustomSearchBar(
    placeholder: String,
    onSearchTextChanged: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            onSearchTextChanged(it)
        },
        placeholder = { Text(placeholder) },
        modifier = Modifier.fillMaxWidth(0.9f),
        shape = RoundedCornerShape(15.dp),
        singleLine = true
    )
}
