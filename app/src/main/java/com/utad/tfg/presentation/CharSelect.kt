package com.utad.tfg.presentation

import androidx.activity.ComponentActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.utad.tfg.local.entities.Character
import com.utad.tfg.model.CharState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.utad.tfg.R

@Composable
fun CharSelectScreen(onAddCharacter: () -> Unit, onCharacterClick: () -> Unit) {
    val vm = hiltViewModel<MainViewModel>(LocalContext.current as ComponentActivity)
    val auth = hiltViewModel<AuthViewModel>(LocalContext.current as ComponentActivity)
    var selectMode by remember { mutableStateOf(false) }
    val characters by vm.localCharacters.collectAsStateWithLifecycle()
    val selectedIDs = remember { mutableStateListOf<Long>() }
    val syncState by vm.charSyncState.collectAsStateWithLifecycle()
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { CharSelectTopbar { auth.signOut() } },
        floatingActionButton = { 
            if (selectMode) RemoveCharacterButton(onClick = {showDeleteDialog = true})
            else AddCharacterButton(onClick = onAddCharacter) 
        },
    ) { paddingValues ->

        when (syncState) {
            CharSyncState.Done -> LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
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
            CharSyncState.Syncing -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        if (showDeleteDialog) {
            DeleteDialog({showDeleteDialog = false},  {vm.deleteCharactersByID(selectedIDs, {selectedIDs.clear()}); selectMode = false})
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
    val race = character.raceIndex.replaceFirstChar { it.uppercase() } +
            (character.subraceIndex?.let { " (${it.replaceFirstChar { char -> char.uppercase() }})" } ?: "")
    val dndClass = character.classIndex.replaceFirstChar { it.uppercase() } +
            (character.subclassIndex?.let { " (${it.replaceFirstChar { char -> char.uppercase() }})" } ?: "")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    if (isSelectMode) onToggleSelect()
                    else onClick(character)
                },
                onLongClick = onToggleSelect
            )
            .then(
                if (isSelected) Modifier.border(
                    2.dp,
                    MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(16.dp)
                ) else Modifier
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Left: Circular character image
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)
                            )
                        )
                    )
                    .border(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (character.imgUri != null) {
                    AsyncImage(
                        model = character.imgUri,
                        contentDescription = character.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = character.name,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Center: Name + Class info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    //text = "$race · $dndClass",
                    text = dndClass,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Right: Level badge
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${character.level}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = "LVL",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                    )
                }
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

@Composable
fun DeleteDialog(onDissmiss: () -> Unit, onAccept: () -> Unit) {
    Dialog(onDissmiss) {

        Column(Modifier
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outline, androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
            .padding(8.dp)
        ) {

            Text(stringResource(R.string.delete_message))

            Spacer(Modifier.size(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDissmiss) {
                    Text(stringResource(R.string.cancel))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    onAccept()
                    onDissmiss()
                }) {
                    Text(stringResource(R.string.delete))
                }
            }
        }
    }
}