package com.utad.tfg.presentation

import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.utad.tfg.local.entities.Character
import com.utad.tfg.model.CharState
import androidx.compose.ui.res.stringResource
import com.utad.tfg.R

@Composable
fun CharSelectScreen(onAddCharacter: () -> Unit, onCharacterClick: () -> Unit) {
    val vm = hiltViewModel<MainViewModel>(LocalContext.current as ComponentActivity)
    val auth = hiltViewModel<AuthViewModel>(LocalContext.current as ComponentActivity)
    var selectMode by remember { mutableStateOf(false) }
    val characters by vm.localCharacters.collectAsStateWithLifecycle()
    val selectedIDs = remember { mutableStateListOf<Long>() }

    Scaffold(
        topBar = { CharSelectTopbar { auth.signOut() } },
        floatingActionButton = { 
            if (selectMode) RemoveCharacterButton(onClick = {vm.deleteCharactersByID(selectedIDs, {selectedIDs.clear()}); selectMode = false})
            else AddCharacterButton(onClick = onAddCharacter) 
        },
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            //item {Text("SelectedIDs: ${selectedIDs.joinToString(", ")}")}

            items(characters) { character ->
                CharacterCard(
                    character = character,
                    isSelected = selectedIDs.contains(character.id),
                    isSelectMode = selectMode,
                    onToggleSelect = {
                        if (selectedIDs.contains(character.id)) {
                            selectedIDs.remove(character.id)
                        } else {
                            selectedIDs.add(character.id)
                        }
                        selectMode = selectedIDs.isNotEmpty()
                    },
                    onClick = { vm.setSelectedCharacter(it); onCharacterClick() }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CharacterCard(
    character: Character,
    isSelected: Boolean,
    isSelectMode: Boolean,
    onToggleSelect: () -> Unit,
    onClick: (Character) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    if (isSelectMode) onToggleSelect()
                    else onClick(character)
                },
                onLongClick = onToggleSelect
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary
                             else MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleLarge
                )
                /*val (statusText, statusColor) = when (character.state) {
                    CharState.active -> "Active" to Color(0xFF4CAF50)
                    CharState.dead -> "Dead" to Color(0xFFF44336)
                    CharState.noCampaign -> "Waiting" to Color(0xFF2196F3)
                    CharState.retired -> "Finished" to Color(0xFF9E9E9E)
                }
                Text(
                    text = statusText,
                    color = statusColor,
                    style = MaterialTheme.typography.labelLarge
                )*/
            }

            Spacer(Modifier.height(8.dp))

            val race = character.raceIndex.replaceFirstChar { it.uppercase() } +
                    (character.subraceIndex?.let { " (${it.replaceFirstChar { char -> char.uppercase() }})" } ?: "")
            val dndClass = character.classIndex.replaceFirstChar { it.uppercase() } +
                    (character.subclassIndex?.let { " (${it.replaceFirstChar { char -> char.uppercase() }})" } ?: "")

            Text(
                text = "Level ${character.level} $race $dndClass",
                style = MaterialTheme.typography.bodyMedium,
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "HP: ${character.currentHp}/${character.maxHp}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "AC: ${character.armorClass}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharSelectTopbar(onLogout: () -> Unit) {
    TopAppBar(
        title = { Text(stringResource(R.string.characters)) },
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        actions = {
            TextButton(onLogout) { Icon(Icons.AutoMirrored.Filled.Logout, "Logout icon")}
        }
    )
}

@Composable
fun AddCharacterButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        shape = CircleShape
    ) {
        Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_character))
    }
}

@Composable
fun RemoveCharacterButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        shape = CircleShape
    ) {
        Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.remove_character))
    }
}
