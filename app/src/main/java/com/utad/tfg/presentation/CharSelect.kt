package com.utad.tfg.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun CharSelectScreen(onAddCharacter: () -> Unit) {
    val vm = hiltViewModel<MainViewModel>()
    var selectMode by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { CharSelectTopbar() },
        floatingActionButton = { 
            if (selectMode) RemoveCharacterButton() 
            else AddCharacterButton(onClick = onAddCharacter) 
        },
    ) { paddingValues ->

        LazyColumn(modifier = Modifier.padding(paddingValues).padding(16.dp)) {

        }
    }
}

@Composable
fun CharacterCard() {
    Card() {

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharSelectTopbar() {
    TopAppBar(
        title = { Text("") },
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    )
}

@Composable
fun AddCharacterButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        shape = CircleShape
    ) {
        Icon(Icons.Default.Add, contentDescription = "Add character")
    }
}

@Composable
fun RemoveCharacterButton() {
    FloatingActionButton(
        onClick = { /* TODO */ },
        shape = CircleShape
    ) {
        Icon(Icons.Default.Delete, contentDescription = "Remove character")
    }
}
