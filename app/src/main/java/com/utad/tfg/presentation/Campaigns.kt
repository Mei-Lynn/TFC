package com.utad.tfg.presentation

import androidx.activity.ComponentActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import android.net.Uri
import android.widget.Toast
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import coil3.compose.AsyncImage
import com.utad.tfg.R
import com.utad.tfg.local.entities.Campaign
import com.utad.tfg.local.entities.Character
import com.utad.tfg.model.CharState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CampaignsScreen(onCharacterClick: () -> Unit) {
    val vm = hiltViewModel<MainViewModel>(LocalContext.current as ComponentActivity)
    val auth = hiltViewModel<AuthViewModel>(LocalContext.current as ComponentActivity)
    
    val campaigns by vm.campaigns.collectAsStateWithLifecycle()
    val currentUser by auth.currentUser.collectAsStateWithLifecycle()
    
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var showActionDialog by remember { mutableStateOf(false) }
    var selectedCampaign by remember { mutableStateOf<Campaign?>(null) }
    
    val uid = currentUser?.uid ?: ""

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showActionDialog = true }, shape = CircleShape) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_campaign))
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = { Text(stringResource(R.string.dm)) }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { Text(stringResource(R.string.player)) }
                )
            }

            val filteredCampaigns = if (selectedTabIndex == 0) {
                campaigns.filter { it.ownerId == uid }
            } else {
                campaigns.filter { it.playersIds.contains(uid) }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredCampaigns) { campaign ->
                    CampaignCard(campaign = campaign, onClick = { selectedCampaign = campaign })
                }
            }
        }
        
        if (showActionDialog) {
            CampaignActionDialog(
                onDismiss = { showActionDialog = false },
                onCreate = { name, uri -> vm.createCampaign(name, uri) },
                onJoin = { id -> vm.joinCampaign(id) }
            )
        }
        
        if (selectedCampaign != null) {
            val currentCampaign = campaigns.find { it.id == selectedCampaign!!.id }
            if (currentCampaign != null) {
                CampaignDetailsDialog(
                    campaign = currentCampaign,
                    onDismiss = { selectedCampaign = null },
                    onCharacterClick = onCharacterClick
                )
            } else {
                selectedCampaign = null
            }
        }
    }
}

@Composable
fun CampaignCard(campaign: Campaign, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)
                            )
                        )
                    )
                    .border(2.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (campaign.imgUri != null) {
                    AsyncImage(
                        model = campaign.imgUri,
                        contentDescription = campaign.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        Icons.Default.Group,
                        contentDescription = campaign.name,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = campaign.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "ID: ${campaign.id}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${campaign.playersIds.size}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(R.string.players),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampaignActionDialog(
    onDismiss: () -> Unit,
    onCreate: (String, String?) -> Unit,
    onJoin: (String) -> Unit
) {
    var mode by remember { mutableStateOf("Create") }
    var campaignName by remember { mutableStateOf("") }
    var campaignImgUri by remember { mutableStateOf("") }
    var joinCampaignId by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TabRow(selectedTabIndex = if (mode == "Create") 0 else 1) {
                    Tab(
                        selected = mode == "Create",
                        onClick = { mode = "Create" },
                        text = { Text(stringResource(R.string.create)) }
                    )
                    Tab(
                        selected = mode == "Join",
                        onClick = { mode = "Join" },
                        text = { Text(stringResource(R.string.join)) }
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))

                if (mode == "Create") {
                    OutlinedTextField(
                        value = campaignName,
                        onValueChange = { campaignName = it },
                        label = { Text(stringResource(R.string.campaign_name)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = campaignImgUri,
                        onValueChange = { campaignImgUri = it },
                        label = { Text(stringResource(R.string.image_url_optional)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    OutlinedTextField(
                        value = joinCampaignId,
                        onValueChange = { joinCampaignId = it },
                        label = { Text(stringResource(R.string.campaign_id)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (mode == "Create" && campaignName.isNotBlank()) {
                                onCreate(campaignName, campaignImgUri.ifBlank { null })
                                onDismiss()
                            } else if (mode == "Join" && joinCampaignId.isNotBlank()) {
                                onJoin(joinCampaignId)
                                onDismiss()
                            }
                        }
                    ) {
                        Text(if (mode == "Create") stringResource(R.string.create) else stringResource(R.string.join))
                    }
                }
            }
        }
    }
}

@Composable
fun CampaignDetailsDialog(campaign: Campaign, onDismiss: () -> Unit, onCharacterClick: () -> Unit) {
    val vm = hiltViewModel<MainViewModel>(LocalContext.current as ComponentActivity)
    val auth = hiltViewModel<AuthViewModel>(LocalContext.current as ComponentActivity)
    val context = LocalContext.current
    
    LaunchedEffect(campaign) {
        vm.loadCampaignCharacters(campaign)
    }

    val players by vm.campaignPlayers.collectAsStateWithLifecycle()
    val localCharacters by vm.localCharacters.collectAsStateWithLifecycle()
    val eligibleCharacters = localCharacters.filter { it.state == CharState.noCampaign }
    var showAddCharacterDialog by remember { mutableStateOf(false) }

    val syncState by vm.campaignSyncState.collectAsStateWithLifecycle()
    val monsters by vm.monsters.collectAsStateWithLifecycle()
    val currentUser by auth.currentUser.collectAsStateWithLifecycle()
    val clipboard = LocalClipboardManager.current
    
    val isOwner = currentUser?.uid == campaign.ownerId

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            val internalUri = copyImageToInternalStorage(context, it)
            vm.updateCampaignImage(campaign, internalUri?.toString())
        }
    }

    var selectedTab by remember { mutableIntStateOf(0) }
    var showMonsterInfo by remember { mutableStateOf<String?>(null) }
    
    if (showMonsterInfo != null) {
        MonsterInfoDialog(
            onDismiss = { showMonsterInfo = null; vm.emptyMonsterDetails() },
            index = showMonsterInfo!!
        )
    }

    if (showAddCharacterDialog) {
        Dialog(onDismissRequest = { showAddCharacterDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(stringResource(R.string.add_character), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    if (eligibleCharacters.isEmpty()) {
                        Text(stringResource(R.string.no_eligible_characters), style = MaterialTheme.typography.bodyMedium)
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.heightIn(max = 200.dp)
                        ) {
                            items(eligibleCharacters) { char ->
                                androidx.compose.material3.OutlinedButton(
                                    onClick = {
                                        vm.addCharacterToCampaign(campaign.id, char.id)
                                        showAddCharacterDialog = false
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(char.name)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = { showAddCharacterDialog = false }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().fillMaxSize(0.8f)
        ) {
            Column(modifier = Modifier.padding(16.dp).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                // Campaign Image
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        .then(if (isOwner) Modifier.clickable { 
                            imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) 
                        } else Modifier),
                    contentAlignment = Alignment.Center
                ) {
                    if (campaign.imgUri != null) {
                        AsyncImage(
                            model = campaign.imgUri,
                            contentDescription = campaign.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            Icons.Default.Group,
                            contentDescription = campaign.name,
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(text = campaign.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                
                // Copy ID
                val idCopiedMsg = stringResource(R.string.id_copied)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("ID: ${campaign.id}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    androidx.compose.material3.IconButton(
                        onClick = { 
                            clipboard.setText(AnnotatedString(campaign.id))
                            Toast.makeText(context, idCopiedMsg, Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = stringResource(R.string.copy_id), modifier = Modifier.size(16.dp))
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                if (isOwner) {
                    TabRow(selectedTabIndex = selectedTab) {
                        Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text(stringResource(R.string.characters)) })
                        Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text(stringResource(R.string.monsters)) })
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                if (syncState == CharSyncState.Syncing && (!isOwner || selectedTab == 0)) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (!isOwner || selectedTab == 0) {
                    if (players.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(stringResource(R.string.no_players_found), style = MaterialTheme.typography.bodyMedium)
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            currentUser?.displayName?.let { currentUsername ->
                                if (players.containsKey(currentUsername)) {
                                    item {
                                        PlayerCard(
                                            name = currentUsername,
                                            isUser = true,
                                            chosenCharacter = players[currentUsername],
                                            onCharacterClick = { char ->
                                                vm.setSelectedCharacter(char)
                                                onCharacterClick()
                                                onDismiss()
                                            },
                                            onAddCharacter = {
                                                showAddCharacterDialog = true
                                            }
                                        )
                                    }
                                }
                            }

                            items(players.keys.toList().filter { it != currentUser?.displayName }) { playerName ->
                                PlayerCard(
                                    name = playerName,
                                    isUser = false,
                                    chosenCharacter = players[playerName],
                                    onCharacterClick = { char ->
                                        vm.setSelectedCharacter(char)
                                        onCharacterClick()
                                        onDismiss()
                                    },
                                    onAddCharacter = {}
                                )
                            }
                        }
                    }
                } else if (isOwner && selectedTab == 1) {
                    val campaignMonsters = monsters.filter { campaign.monstersIds.contains(it.index) }
                    if (campaignMonsters.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(stringResource(R.string.no_monsters_added), style = MaterialTheme.typography.bodyMedium)
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(campaignMonsters) { monster ->
                                CampaignMonsterCard(
                                    name = monster.name,
                                    onInfoClick = { showMonsterInfo = monster.index },
                                    onRemoveClick = { vm.removeMonsterFromCampaign(campaign.id, monster.index) }
                                )
                            }
                        }
                    }
                }
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.close))
                    }
                }
            }
        }
    }
}

@Composable
fun PlayerCard(
    name: String,
    isUser: Boolean,
    chosenCharacter: Character?,
    onCharacterClick: (Character) -> Unit,
    onAddCharacter: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = stringResource(R.string.player_label, name),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (chosenCharacter != null) {
                CharacterCard(
                    character = chosenCharacter,
                    isSelected = false,
                    isSelectMode = false,
                    onToggleSelect = {},
                    onClick = {
                        onCharacterClick(chosenCharacter)
                    }
                )
            } else if (isUser) {
                Button(
                    onClick = onAddCharacter,
                    modifier = Modifier.fillMaxWidth().padding(start = 24.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.add_character))
                }
            } else {
                Text(
                    text = stringResource(R.string.no_character_yet),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 24.dp)
                )
            }
        }
    }
}

@Composable
fun CampaignMonsterCard(
    name: String,
    onInfoClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onInfoClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            androidx.compose.material3.IconButton(onClick = onRemoveClick) {
                Icon(Icons.Default.Delete, contentDescription = "Remove")
            }
        }
    }
}